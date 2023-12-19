package com.techelevator.model;

public class Chip extends Item {

    /**
     *
     * @param productName name of the item
     * @param price price of the item
     */
    public Chip(String productName, int price){
        super(productName, price);

    }
    /**
     *
     * @return - output "Crunch Crunch, Yum" for any Candy object
     */
    @Override
    public String getJingle() {
        return "Crunch Crunch, Yum!";
    }

}
