/**
 *
 */
package com.nasa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nasa.resources.RobotResource;
import com.robots.exceptions.InvalidCharactersException;
import com.robots.exceptions.LimitAreaExceededException;

/**
 * @author cleiton.cardoso
 *
 */
public class ResourcesTest {

    private RobotResource robotResource;

    @Before
    public void createControllerInstance() {
        this.robotResource = new RobotResource();
    }

    /**
     *
     * @throws InvalidCharactersException
     * @throws LimitAreaExceededException
     */
    @Test
    public void testMovesReturn() throws InvalidCharactersException, LimitAreaExceededException {
        Assert.assertEquals("(0,1,N)", this.robotResource.walk("M"));
        Assert.assertEquals("(0,0,W)", this.robotResource.walk("RMLLM"));
        Assert.assertEquals("(1,0,E)", this.robotResource.walk("RM"));
        Assert.assertEquals("(0,1,S)", this.robotResource.walk("MMLLM"));
    }

    @Test(expected = InvalidCharactersException.class)
    public void testCharactersException() throws InvalidCharactersException, LimitAreaExceededException {
        System.out.println(this.robotResource.walk("MMLLLL888888"));
    }

    @Test(expected = LimitAreaExceededException.class)
    public void testLimitException() throws InvalidCharactersException, LimitAreaExceededException {
        System.out.println(this.robotResource.walk("MMMMMMMMM"));
    }

    @Test
    public void testWalkWithEmptyValue() throws InvalidCharactersException, LimitAreaExceededException {
        this.robotResource.walk("");
    }
}
