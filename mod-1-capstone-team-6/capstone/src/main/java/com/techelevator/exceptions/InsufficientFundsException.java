package com.techelevator.exceptions;

public class InsufficientFundsException extends Exception {

    /**
     * constructs an InsufficientFundsException with no details message
     */
    public InsufficientFundsException() {
        super();
    }

    /**
     *
     * @param message constructs an InsufficientFundsException with specified details message
     */
    public InsufficientFundsException(String message) {
        super(message);
    }

}
