/**
 *
 */
package com.nasa.model;

import com.robots.exceptions.LimitAreaExceededException;

/**
 * @author cleiton.cardoso
 *
 */
public class RobotPosition {

    private int x = 0;
    private int y = 0;
    private Compass compass = Compass.N;

    public Compass getCompass() {
        return this.compass;
    }

    public void setCompass(final Compass compass) {
        this.compass = compass;
    }

    public int getX() {
        return this.x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    /**
     * @throws LimitAreaExceededException
     *
     */
    public void moveForward(final MarsField field) throws LimitAreaExceededException {
        switch (this.compass) {
        case N:
            this.y++;
            break;
        case S:
            this.y--;
            break;
        case E:
            this.x++;
            break;
        case W:
            this.x--;
        }
        if (this.x < 0 || this.y < 0 || this.x > field.getXLimit() || this.y > field.getYLimit()) {
            throw new LimitAreaExceededException(this.x, this.y);
        }
    }

    /**
     *
     */
    public void turnLeft() {
        this.compass = this.compass.rotate(-90);
    }

    /**
     *
     */
    public void turnRight() {
        this.compass = this.compass.rotate(90);
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + "," + this.compass + ")";
    }

}
