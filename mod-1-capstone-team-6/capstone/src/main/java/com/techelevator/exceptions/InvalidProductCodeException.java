package com.techelevator.exceptions;

public class InvalidProductCodeException extends Exception {

    /**
     *  constructs an InvalidProductCodeException with no details message
     */
    public InvalidProductCodeException() {
        super();
    }

    /**
     *
     * @param message  constructs an InvalidProductCodeException with specified details message
     */
    public InvalidProductCodeException(String message) {
        super(message);
    }

}
