package com.techelevator.model;

public class Gum extends Item {

    /**
     *
     * @param productName name of the item
     * @param price price of the item
     */
    public Gum(String productName, int price){
        super(productName, price);
    }

    /**
     *
     * @return - output "Chew Chew, Yum" for any Candy object
     */
    @Override
    public String getJingle(){
        return  "Chew Chew, Yum!";
    }

}
