package com.techelevator.model;

public class Drink extends Item {

    /**
     *
     * @param productName name of the item
     * @param price price of the item
     */
    public Drink(String productName, int price){
        super(productName, price);
    }

    /**
     *
     * @return - output "Glug Glug, Yum" for any Candy object
     */
    @Override
    public String getJingle(){
        return  "Glug Glug, Yum!";
    }

}
