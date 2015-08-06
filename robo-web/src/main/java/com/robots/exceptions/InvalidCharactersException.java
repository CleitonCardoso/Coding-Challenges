package com.robots.exceptions;

/**
 *
 * @author cleiton.cardoso
 *
 */
public class InvalidCharactersException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8885944820332368240L;

    public InvalidCharactersException(final char character) {
        super("Algo deu errado e nao pudemos processar sua informacao, verifique o comando:" + character + ".\nUse os comandos: \n\"M\" para andar a frente, \n\"R\" para girar em sentido horario,\n\"L\" para girar em sentido anti-horario.");
    }
}
