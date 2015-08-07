/**
 *
 */
package com.nasa.model;

import com.robots.exceptions.InvalidCharactersException;
import com.robots.exceptions.LimitAreaExceededException;

/**
 * @author cleiton.cardoso
 *
 */
public class Robot {

    private final RobotPosition position;
    private final MarsField field;

    /**
     *
     * @param field
     */
    public Robot(final MarsField field) {
        this.field = field;
        this.position = new RobotPosition();
    }

    /**
     * @param steps
     * @throws LimitAreaExceededException
     * @throws InvalidCharactersException
     */
    public void move(final char[] steps) throws InvalidCharactersException, LimitAreaExceededException {
        for (final char step : steps) {
            this.move(step);
        }
    }

    /**
     *
     * @param move
     * @throws InvalidCharactersException
     * @throws LimitAreaExceededException
     */
    public void move(final char move) throws InvalidCharactersException, LimitAreaExceededException {
        switch (Character.toUpperCase(move)) {
        case 'M':
            this.position.moveForward(this.getField());
            break;
        case 'L':
            this.position.turnLeft();
            break;
        case 'R':
            this.position.turnRight();
            break;
        default:
            throw new InvalidCharactersException(move);
        }
    }

    public RobotPosition getPosition() {
        return this.position;
    }

    public MarsField getField() {
        return this.field;
    }

}
