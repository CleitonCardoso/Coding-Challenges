package com.nasa.model;

/**
 *
 * @author cleiton.cardoso
 *
 */
public enum Compass {

    N(360), E(90), S(180), W(270);

    private int angle;

    private Compass(final int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return this.angle;
    }

    /**
     * Rotaciona da posição atual até até quantos graus forem recebidos e
     * retorna um enum com o valor mais aproximado; <br>
     * <b>Numeros negativos rodam para a esquerda, positivos rodam para a
     * direita;</b>
     *
     * @param value
     * @return
     */
    public Compass rotate(int value) {
        value = value + this.angle;
        final int circleAngle = 360;
        if (value < 0) {
            value = circleAngle + value % circleAngle;
        }
        final Compass[] directions = Compass.values();
        return directions[Math.round(value % circleAngle / (circleAngle / 4))];
    }

}
