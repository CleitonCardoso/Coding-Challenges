/**
 *
 */
package com.nasa.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.nasa.model.MarsField;
import com.nasa.model.Robot;
import com.robots.exceptions.InvalidCharactersException;
import com.robots.exceptions.LimitAreaExceededException;

/**
 * @author cleiton.cardoso
 *
 */
@Path("mars")
public class RobotResource {

    @POST
    @Produces("text/plain")
    @Path("{stepstomove}")
    public String walk(@PathParam("stepstomove")
    final String stepstomove) throws InvalidCharactersException, LimitAreaExceededException {
        final char[] steps = stepstomove.toCharArray();
        final Robot robot = new Robot(new MarsField(5, 5));
        robot.move(steps);
        return robot.getPosition().toString();
    }

}
