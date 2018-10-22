package com.n26;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class HttpJsonIT {

    private static final String TIMESTAMP_OFFSET_PROPERTY = "_timestampOffset";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;

    private static final MediaType CONTENT_TYPE_TEXT = MediaType.TEXT_PLAIN;

    private static HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private GenericWebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void getContext() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        assertNotNull(mockMvc);
    }

    @Autowired
    public void setConverters(HttpMessageConverter<?>[] converters) {
        mappingJackson2HttpMessageConverter = Stream.of(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull(mappingJackson2HttpMessageConverter);
    }

    List<String> httpJsonFiles = new ArrayList<>();

    Map<String, String> httpJsonAndTestname = new HashMap<>();

    Map<String, Long> executionTime = new HashMap<>();

    Map<String, Pair<Pair<String, String>, Pair<String, String>>> testFailures = new HashMap<>();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {

    };

    @Rule
    public TestWatcher watchman = new TestWatcher() {

        @Override
        public Statement apply(Statement base, Description description) {
            return super.apply(base, description);
        }

        @Override
        protected void starting(Description description) {
            super.starting(description);
        }

        @Override
        protected void succeeded(Description description) {
            generateReportForProperExecution();
        }

        @Override
        protected void failed(Throwable e, Description description) {
            generateReportForRuntimeFailureExecution();
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
        }
    };

    @Test
    public void dynamicTests() {
        httpJsonFiles = listTestCaseFiles();

        if (!httpJsonFiles.isEmpty()) {
            List<String> testnames = readTestNames();

            if (!testnames.isEmpty()) {
                for (int i = 0; i < testnames.size(); i++) {
                    String[] testname = testnames.get(i).split(": ");
                    httpJsonAndTestname.put(testname[0], testname[1]);
                }

                AtomicInteger processedRequestCount = new AtomicInteger(1);

                httpJsonFiles.forEach(filename -> {
                    if (testFailures.containsKey(filename)) {
                        return;
                    }

                    final List<String> jsonStrings = readJsonCases(filename);

                    System.out.println("-----Executing integration testcases from file " + filename + "-----");

                    jsonStrings.forEach(jsonString -> {
                        if (testFailures.containsKey(filename)) {
                            return;
                        }

                        JsonNode jsonObject = parseJson(jsonString);
                        if (jsonObject.has("sleep")) {
                            executeSleep(jsonObject);
                        } else {
                            executeTestRequest(jsonObject, filename, processedRequestCount);
                        }
                    });

                    executionTime.put(filename, stopwatch.runtime(TimeUnit.MILLISECONDS));
                });
            }
        }
    }

    private void executeSleep(JsonNode sleepCommand) {
        long sleepTimeout = sleepCommand.get("sleep").asLong();
        try {
            System.out.println(Colors.BLUE_BOLD + "Sleeping for " + sleepTimeout + " ms." + Colors.RESET);
            Thread.sleep(sleepTimeout);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new Error(ex);
        }
    }

    private void executeTestRequest(JsonNode jsonObject, String filename, AtomicInteger processedRequestCount) {
        JsonNode request = jsonObject.get("request");
        JsonNode response = jsonObject.get("response");

        String method = request.get("method").asText();
        String url = request.get("url").asText();
        String body = extractBody(request.get("body"));
        String statusCode = response.get("status_code").asText();

        String requestID = Colors.BLUE_BOLD + String.format("Processing request %d ",
                processedRequestCount.get()) + Colors.RESET;
        String requestMessage = String.format("%s %s", method, url);

        if (method.charAt(0) == 'P') {
            requestMessage = String.format("%s %s %s", method, url, body);
        }

        System.out.println(requestID + Colors.WHITE_BOLD + requestMessage + Colors.RESET);

        processedRequestCount.set(processedRequestCount.incrementAndGet());

        switch (method) {
            case "POST": {
                MediaType contentType = MediaType.ALL;
                String type = request.get("headers").get("Content-Type").asText();

                if (type.equals("application/json")) {
                    contentType = CONTENT_TYPE_JSON;
                } else if (type.equals("text/plain")) {
                    contentType = CONTENT_TYPE_TEXT;
                }

                if (!contentType.equals(MediaType.ALL)) {
                    try {
                        ResultActions resultActions = mockMvc.perform(post(url)
                                .content(body)
                                .contentType(CONTENT_TYPE_JSON));
                        MockHttpServletResponse mockResponse = resultActions.andReturn()
                                .getResponse();

                        validateStatusCode(filename, method + " " + url,
                                statusCode, String.valueOf(mockResponse.getStatus()));
                    } catch (Exception ex) {
                        throw new Error(ex);
                    }
                }

                break;
            }
            case "PUT": {
                MediaType contentType = MediaType.ALL;
                String type = request.get("headers").get("Content-Type").asText();

                if (type.equals("application/json")) {
                    contentType = CONTENT_TYPE_JSON;
                } else if (type.equals("text/plain")) {
                    contentType = CONTENT_TYPE_TEXT;
                }

                if (!contentType.equals(MediaType.ALL)) {
                    try {
                        ResultActions resultActions = mockMvc.perform(put(url)
                                .content(body)
                                .contentType(CONTENT_TYPE_JSON));
                        MockHttpServletResponse mockResponse = resultActions.andReturn()
                                .getResponse();

                        validateStatusCode(filename, method + " " + url,
                                statusCode, String.valueOf(mockResponse.getStatus()));
                    } catch (Exception ex) {
                        throw new Error(ex);
                    }
                }

                break;
            }
            case "DELETE":
                try {
                    ResultActions resultActions = mockMvc.perform(delete(url));
                    MockHttpServletResponse mockResponse = resultActions.andReturn()
                            .getResponse();

                    validateStatusCode(filename, method + " " + url,
                            statusCode, String.valueOf(mockResponse.getStatus()));
                } catch (Exception ex) {
                    throw new Error(ex);
                }

                break;
            case "GET":
                try {
                    ResultActions resultActions = mockMvc.perform(get(url));
                    MockHttpServletResponse mockResponse = resultActions.andReturn()
                            .getResponse();

                    if (validateStatusCode(filename, method + " " + url,
                            statusCode, String.valueOf(mockResponse.getStatus()))) {
                        JsonNode expectedType = response.get("headers").get("Content-Type");
                        if (expectedType != null) {
                            if (mockResponse.containsHeader("content-type")) {
                                validateContentType(filename, method + " " + url,
                                        expectedType.asText(), mockResponse.getContentType());
                            }

                            if (statusCode.equals("200")) {
                                String responseBody = mockResponse.getContentAsString();
                                JsonNode expectedResponseBodyJson = response.get("body");

                                if (expectedType.asText().equals("application/json")) {
                                    JsonNode responseBodyJson = OBJECT_MAPPER.readTree
                                            (responseBody);

                                    validateJsonResponse(filename, method + " " + url,
                                            expectedResponseBodyJson, responseBodyJson);
                                } else if (expectedType.asText().equals("text/plain")) {
                                    validateTextResponse(filename, method + " " + url,
                                            expectedResponseBodyJson.toString(), responseBody);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    throw new Error(ex);
                }

                break;
            default:
                break;
        }
    }

    private JsonNode parseJson(String jsonString) {
        try {
            return OBJECT_MAPPER.readTree(jsonString);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    /**
     * Converts timestamp offsets in the json to the actual timestamp since
     * API is required to rely on the current time
     */
    private String extractBody(JsonNode body) {
        if (!body.isObject()) {
            return body.toString();
        }

        ObjectNode bodyObject = (ObjectNode) body;

        if (bodyObject.has(TIMESTAMP_OFFSET_PROPERTY)) {
            long offset = bodyObject.get(TIMESTAMP_OFFSET_PROPERTY).asLong();
            bodyObject.remove(TIMESTAMP_OFFSET_PROPERTY);
            Instant timestamp = Instant.now().plusMillis(offset);
            bodyObject.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(timestamp));
        }
        return bodyObject.toString();
    }

    private List<String> readJsonCases(String filename) {
        ClassPathResource jsonResource = new ClassPathResource("testcases/" + filename);
        try (InputStream inputStream = jsonResource.getInputStream()) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(toList());
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private List<String> readTestNames() {
        List<String> testnames;
        ClassPathResource resource = new ClassPathResource("testcases/description.txt");
        try (InputStream inputStream = resource.getInputStream()) {
            testnames = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(toList());
        } catch (IOException ex) {
            throw new Error(ex);
        }
        return testnames;
    }

    private List<String> listTestCaseFiles() {
        try {
            return Files.list(Paths.get("src/it/resources/testcases"))
                    .filter(Files::isRegularFile)
                    .map(f -> f.getFileName().toString())
                    .filter(f -> f.endsWith(".json"))
                    .collect(toList());
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private boolean validateStatusCode(String filename, String testcase, String expected, String found) {
        if (!expected.equals(found)) {
            String reason = "Status code";
            addTestFailure(filename, new ImmutablePair<>(new ImmutablePair<>(testcase, reason), new ImmutablePair<>
                    (expected, found)));

            return false;
        }

        return true;
    }

    private boolean validateContentType(String filename, String testcase, String expected, String found) {
        if (!found.startsWith(expected)) {
            String reason = "Content type";
            addTestFailure(filename, new ImmutablePair<>(new ImmutablePair<>(testcase, reason), new ImmutablePair<>
                    (expected, found)));

            return false;
        }

        return true;
    }

    private boolean validateTextResponse(String filename, String testcase, String expected, String found) {
        if (!expected.equals(found)) {
            String reason = "Response text does not match with the expected response";
            addTestFailure(filename, new ImmutablePair<>(new ImmutablePair<>(testcase, reason), new ImmutablePair<>
                    (expected, found)));

            return false;
        }

        return true;
    }

    private boolean validateJsonResponse(String filename, String testcase, JsonNode expected, JsonNode found) {
        try {
            List<JsonNode> expectedResponseJsonList = OBJECT_MAPPER.readValue(expected.toString(),
                    new TypeReference<List<JsonNode>>() {

                    });

            List<JsonNode> responseBodyJsonList = OBJECT_MAPPER.readValue(found.toString(),
                    new TypeReference<List<JsonNode>>() {

                    });

            if (expectedResponseJsonList.size() != responseBodyJsonList.size()) {
                String reason = "Response Json array size does not match with the expected array size";
                addTestFailure(filename, new ImmutablePair<>(new ImmutablePair<>(testcase, reason),
                        new ImmutablePair<>(String.valueOf(expectedResponseJsonList.size()),
                                String.valueOf(responseBodyJsonList.size()))));

                return false;
            } else {
                for (int i = 0; i < expectedResponseJsonList.size(); i++) {
                    JsonNode expectedJson = expectedResponseJsonList.get(i);
                    JsonNode foundJson = responseBodyJsonList.get(i);

                    if (!expectedJson.equals(foundJson)) {
                        String reason = String.format("Response Json (at index %d) does not match with the expected " +
                                "Json", i);
                        addTestFailure(filename, new ImmutablePair<>(new ImmutablePair<>(testcase, reason), new
                                ImmutablePair<>(expectedJson.toString(),
                                foundJson.toString())));

                        return false;
                    }
                }
            }
        } catch (IOException ex) {
            if (!expected.equals(found)) {
                String reason = "Response Json does not match with the expected Json";
                addTestFailure(filename, new ImmutablePair<>(new ImmutablePair<>(testcase, reason), new
                        ImmutablePair<>(expected.toString(),
                        found.toString())));

                return false;
            }
        }

        return true;
    }

    private void addTestFailure(String filename, Pair<Pair<String, String>, Pair<String, String>> failure) {
        if (testFailures.containsKey(filename)) {
            throw new Error("I should skip rest of the test cases.");
        }
        System.out.println(String.format("Test Failed: Reason: %s\nExpected: %s\nActual  : %s", failure.getLeft()
                .getRight(), failure.getRight().getLeft(), failure.getRight().getRight()));

        testFailures.put(filename, failure);
    }

    private void generateReportForProperExecution() {
        List<Long> executionTimeInSeconds = executionTime.keySet()
                .stream()
                .sorted()
                .map(filename -> executionTime.get(filename))
                .collect(toList());

        for (int i = 1; i < executionTimeInSeconds.size(); i++) {
            executionTime.put(httpJsonFiles.get(i),
                    (executionTimeInSeconds.get(i) < executionTimeInSeconds.get(i - 1))
                            ? 0
                            : executionTimeInSeconds.get(i) - executionTimeInSeconds.get(i - 1));
        }

        final Set<String> failedTestFiles = testFailures.keySet();

        final String DASHES = "------------------------------------------------------------------------";
        final String ANSI_SUMMARY = DASHES + "\n" + Colors.BLUE_BOLD + "TEST SUMMARY\n" + Colors.RESET + DASHES;
        final String ANSI_RESULT = DASHES + "\n" + Colors.BLUE_BOLD + "TEST RESULT\n" + Colors.RESET + DASHES;
        final String ANSI_REPORT = DASHES + "\n" + Colors.BLUE_BOLD + "FAILURE REPORT %s\n" + Colors.RESET + DASHES;
        final String ANSI_FAILURE = Colors.RED_BOLD + "Failure" + Colors.RESET;
        final String ANSI_SUCCESS = Colors.GREEN_BOLD + "Success" + Colors.RESET;

        File reportFolder = new File("target/customReports");
        reportFolder.mkdir();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("target/customReports/result.txt"))) {
            writer.write(Colors.WHITE_BOLD +
                    " _    _       _ _     _______        _     _____                       _   \n" +
                    "| |  | |     (_) |   |__   __|      | |   |  __ \\                     | |  \n" +
                    "| |  | |_ __  _| |_     | | ___  ___| |_  | |__) |___ _ __   ___  _ __| |_ \n" +
                    "| |  | | '_ \\| | __|    | |/ _ \\/ __| __| |  _  // _ \\ '_ \\ / _ \\| '__| __|\n" +
                    "| |__| | | | | | |_     | |  __/\\__ \\ |_  | | \\ \\  __/ |_) | (_) | |  | |_ \n" +
                    " \\____/|_| |_|_|\\__|    |_|\\___||___/\\__| |_|  \\_\\___| .__/ \\___/|_|   \\__|\n" +
                    "                                                     | |                   \n" +
                    "                                                     |_|                   " +
                    Colors.RESET);
            writer.newLine();

            writer.write(ANSI_SUMMARY);
            writer.newLine();
            writer.write("Tests: " + httpJsonFiles.size());
            writer.write(", ");
            writer.write("Success: " + (httpJsonFiles.size() - failedTestFiles.size()));
            writer.write(", ");

            if (!failedTestFiles.isEmpty()) {
                writer.write("Failure: " + failedTestFiles.size());
                writer.write(", ");
            }

            writer.write("Total time: " + executionTimeInSeconds.get(executionTimeInSeconds.size() - 1) / 1000.0f +
                    "s");
            writer.newLine();
            writer.newLine();

            writer.write(ANSI_RESULT);
            writer.newLine();

            httpJsonFiles.forEach(filename -> {
                if (failedTestFiles.contains(filename)) {
                    try {
                        writer.write(Colors.WHITE_BOLD + filename + ": " + Colors.RESET +
                                ANSI_FAILURE + " (" + executionTime.get(filename) / 1000.0f + "s)");
                        writer.newLine();
                    } catch (IOException ex) {
                        throw new Error(ex);
                    }
                } else {
                    try {
                        writer.write(Colors.WHITE_BOLD + filename + ": " + Colors.RESET +
                                ANSI_SUCCESS + " (" + executionTime.get(filename) / 1000.0f + "s)");
                        writer.newLine();
                    } catch (IOException ex) {
                        throw new Error(ex);
                    }
                }
            });

            writer.newLine();

            final Map<String, String> fileFailureReason = new HashMap<>();

            if (!failedTestFiles.isEmpty()) {
                failedTestFiles.stream()
                        .sorted()
                        .forEachOrdered(filename -> {
                            Pair<Pair<String, String>, Pair<String, String>> report = testFailures.get(filename);

                            String testcase = report.getKey()
                                    .getKey();
                            String reason = report.getKey()
                                    .getValue();
                            String expected = report.getValue()
                                    .getKey();
                            String found = report.getValue()
                                    .getValue();

                            fileFailureReason.put(filename, reason);

                            try {
                                writer.write(String.format(ANSI_REPORT, filename));
                                writer.newLine();
                                writer.write(Colors.WHITE_BOLD + "[Test Case]" + Colors.RESET + " " + testcase);
                                writer.newLine();
                                writer.write(Colors.WHITE_BOLD + "[   Reason]" + Colors.RESET + " " + Colors.RED_BOLD
                                        + reason +
                                        Colors.RESET);
                                writer.newLine();
                                writer.write(Colors.WHITE_BOLD + "[ Expected]" + Colors.RESET + " " + expected);
                                writer.newLine();
                                writer.write(Colors.WHITE_BOLD + "[    Found]" + Colors.RESET + " " + found);
                                writer.newLine();
                                writer.newLine();
                            } catch (IOException ex) {
                                throw new Error(ex);
                            }
                        });
            }
        } catch (IOException ex) {
            throw new Error(ex);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("target/customReports/result.xml"))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write(String.format("<testsuite name=\"%s\" time=\"%f\" tests=\"%d\" errors=\"0\" skipped=\"0\" " +
                            "failures=\"%d\">\n",
                    this.getClass().getName(),
                    executionTimeInSeconds.get(executionTimeInSeconds.size() - 1) / 1000.0f,
                    httpJsonFiles.size(),
                    failedTestFiles.size()));

            httpJsonFiles.stream()
                    .sorted()
                    .forEachOrdered(filename -> {
                        try {
                            if (!failedTestFiles.contains(filename)) {
                                writer.write(String.format("    <testcase name=\"%s\" classname=\"%s\" time=\"%f\"/>\n",
                                        httpJsonAndTestname.get(filename),
                                        this.getClass().getName(),
                                        executionTime.get(filename) / 1000.0f));
                            } else {
                                Pair<Pair<String, String>, Pair<String, String>> report = testFailures.get(filename);
                                String testcase = report.getKey()
                                        .getKey();
                                String reason = report.getKey()
                                        .getValue();

                                writer.write(String.format("    <testcase name=\"%s\" classname=\"%s\" time=\"%f\">\n"
                                                + "        <failure>\n            " + testcase + ": " + reason + ".\n" +
                                                "        </failure>\n    </testcase>\n",
                                        httpJsonAndTestname.get(filename),
                                        this.getClass().getName(),
                                        executionTime.get(filename) / 1000.0f));
                            }
                        } catch (IOException ex) {
                            throw new Error(ex);
                        }
                    });

            writer.write("</testsuite>\n");
        } catch (IOException ex) {
            throw new Error(ex);
        }
        testFailures.entrySet().stream().findFirst().ifPresent(failure -> {
            Pair<Pair<String, String>, Pair<String, String>> error = failure.getValue();
            assertEquals(error.getLeft().getRight(), error.getRight().getLeft(), error.getRight()
                    .getRight());
        });
    }

    private void generateReportForRuntimeFailureExecution() {
        final String DASHES = "------------------------------------------------------------------------";
        final String ANSI_SUMMARY = DASHES + "\n" + Colors.BLUE_BOLD + "TEST SUMMARY\n" + Colors.RESET + DASHES;
        final String ANSI_RESULT = DASHES + "\n" + Colors.BLUE_BOLD + "TEST RESULT\n" + Colors.RESET + DASHES;
        final String ANSI_REPORT = DASHES + "\n" + Colors.BLUE_BOLD + "FAILURE REPORT %s\n" + Colors.RESET + DASHES;
        final String ANSI_FAILURE = Colors.RED_BOLD + "Failure" + Colors.RESET;
        final String ANSI_SUCCESS = Colors.GREEN_BOLD + "Success" + Colors.RESET;

        File reportFolder = new File("target/customReports");
        reportFolder.mkdir();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("target/customReports/result.txt"))) {
            writer.write(Colors.WHITE_BOLD +
                    " _    _       _ _     _______        _     _____                       _   \n" +
                    "| |  | |     (_) |   |__   __|      | |   |  __ \\                     | |  \n" +
                    "| |  | |_ __  _| |_     | | ___  ___| |_  | |__) |___ _ __   ___  _ __| |_ \n" +
                    "| |  | | '_ \\| | __|    | |/ _ \\/ __| __| |  _  // _ \\ '_ \\ / _ \\| '__| __|\n" +
                    "| |__| | | | | | |_     | |  __/\\__ \\ |_  | | \\ \\  __/ |_) | (_) | |  | |_ \n" +
                    " \\____/|_| |_|_|\\__|    |_|\\___||___/\\__| |_|  \\_\\___| .__/ \\___/|_|   \\__|\n" +
                    "                                                     | |                   \n" +
                    "                                                     |_|                   " +
                    Colors.RESET);
            writer.newLine();

            writer.write(ANSI_SUMMARY);
            writer.newLine();
            writer.write("Tests: " + httpJsonFiles.size());
            writer.write(", ");
            writer.write("Success: 0");
            writer.write(", ");
            writer.write("Failure: " + httpJsonFiles.size());
            writer.write(", ");
            writer.write("Total time: 0s");
            writer.newLine();
            writer.newLine();

            writer.write(ANSI_RESULT);
            writer.newLine();

            httpJsonFiles.forEach(filename -> {
                try {
                    writer.write(Colors.WHITE_BOLD + filename + ": " + Colors.RESET +
                            ANSI_FAILURE + " (0s)");
                    writer.newLine();
                } catch (IOException ex) {
                    throw new Error(ex);
                }
            });
        } catch (IOException ex) {
            throw new Error(ex);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("target/customReports/result.xml"))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write(String.format("<testsuite name=\"%s\" time=\"%f\" tests=\"%d\" errors=\"0\" skipped=\"0\" " +
                            "failures=\"%d\">\n",
                    this.getClass().getName(),
                    0.0f,
                    httpJsonFiles.size(),
                    httpJsonFiles.size()));

            httpJsonFiles.stream()
                    .sorted()
                    .forEachOrdered(filename -> {
                        try {
                            writer.write(String.format("    <testcase name=\"%s\" classname=\"%s\" time=\"%f\">\n"
                                            + "        <failure>\n            Runtime Error.\n        </failure>\n   " +
                                            " </testcase>\n",
                                    httpJsonAndTestname.get(filename),
                                    this.getClass().getName(),
                                    0.0f));
                        } catch (IOException ex) {
                            throw new Error(ex);
                        }
                    });

            writer.write("</testsuite>\n");
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private static class Colors {

        public static final String RESET = "\033[0m";

        public static final String BLACK = "\033[0;30m";

        public static final String RED = "\033[0;31m";

        public static final String GREEN = "\033[0;32m";

        public static final String YELLOW = "\033[0;33m";

        public static final String BLUE = "\033[0;34m";

        public static final String WHITE = "\033[0;37m";

        public static final String BLACK_BOLD = "\033[1;30m";

        public static final String RED_BOLD = "\033[1;31m";

        public static final String GREEN_BOLD = "\033[1;32m";

        public static final String YELLOW_BOLD = "\033[1;33m";

        public static final String BLUE_BOLD = "\033[1;34m";

        public static final String WHITE_BOLD = "\033[1;37m";
    }

}
