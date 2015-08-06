package com.robots.exceptions;

/**
 * @author cleiton.cardoso
 */
public class LimitAreaExceededException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8235524411896842183L;

    public LimitAreaExceededException(final int x, final int y) {
        super(String.format("Nao foi possivel percorrer completamente o caminho solicitado, os limites da area foram atingidos [x %x : y %x]", x, y));
    }

}
