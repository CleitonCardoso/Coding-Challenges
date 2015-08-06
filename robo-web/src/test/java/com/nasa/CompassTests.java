package com.nasa;

import org.junit.Assert;
import org.junit.Test;

import com.nasa.model.Compass;

/**
 * @author cleiton.cardoso
 *
 */
public class CompassTests {

    @Test
    public void rotateRight() {
        Assert.assertEquals(Compass.N, Compass.W.rotate(90));
        Assert.assertEquals(Compass.E, Compass.N.rotate(90));
        Assert.assertEquals(Compass.S, Compass.E.rotate(90));
        Assert.assertEquals(Compass.W, Compass.S.rotate(90));
        Assert.assertEquals(Compass.E, Compass.W.rotate(4614684));

    }

    @Test
    public void rotateLeft() {
        Assert.assertEquals(Compass.W, Compass.N.rotate(-90));
        Assert.assertEquals(Compass.N, Compass.E.rotate(-90));
        Assert.assertEquals(Compass.E, Compass.S.rotate(-90));
        Assert.assertEquals(Compass.S, Compass.W.rotate(-90));
        Assert.assertEquals(Compass.E, Compass.W.rotate(-8687683));
    }

}
