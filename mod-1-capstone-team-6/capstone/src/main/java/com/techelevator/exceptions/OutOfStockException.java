package com.techelevator.exceptions;

public class OutOfStockException extends Exception {

    /**
     *  constructs an OutOfStockException with no details message
     */
    public OutOfStockException() {
        super();
    }

    /**
     *
     * @param message constructs an OutOfStockException with specified details message
     */
    public OutOfStockException(String message) {
        super(message);
    }

}
