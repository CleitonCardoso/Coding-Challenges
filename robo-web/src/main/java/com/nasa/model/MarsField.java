/**
 *
 */
package com.nasa.model;

/**
 * @author cleiton.cardoso
 *
 */
public class MarsField {

    private final int xLimit;
    private final int yLimit;

    /**
     * @param x
     * @param y
     */
    public MarsField(final int x, final int y) {
        this.xLimit = x;
        this.yLimit = y;
    }

    public int getXLimit() {
        return this.xLimit;
    }

    public int getYLimit() {
        return this.yLimit;
    }

}
