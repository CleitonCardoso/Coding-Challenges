/**
 *
 */
package com.nasa;

import org.junit.Assert;
import org.junit.Test;

import com.nasa.model.Compass;
import com.nasa.model.MarsField;
import com.nasa.model.Robot;
import com.nasa.model.RobotPosition;

/**
 * @author cleiton.cardoso
 *
 */
public class ModelTests {

    @Test
    public void createInstance() {
        final RobotPosition robotPosition = new RobotPosition();
        Assert.assertNotNull(robotPosition);
        Assert.assertNotNull(robotPosition.getCompass());
        robotPosition.setCompass(Compass.E);
        Assert.assertNotNull(robotPosition.getX());
        Assert.assertNotNull(robotPosition.getY());

        robotPosition.setX(10);
        robotPosition.setY(15);

        final MarsField marsField = new MarsField(0, 0);
        Assert.assertNotNull(marsField);
        Assert.assertNotNull(marsField.getXLimit());
        Assert.assertNotNull(marsField.getYLimit());

        final Robot robot = new Robot(marsField);
        Assert.assertNotNull(robot);
        Assert.assertNotNull(robot.getPosition());
        Assert.assertNotNull(robot.getPosition().getCompass());
        Assert.assertNotNull(robot.getPosition().getCompass().getAngle());
        Assert.assertNotNull(robot.getPosition().getX());
        Assert.assertNotNull(robot.getPosition().getY());

    }

}
