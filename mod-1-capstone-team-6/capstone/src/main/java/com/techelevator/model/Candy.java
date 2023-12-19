package com.techelevator.model;

public class Candy extends Item {

    /**
     *
     * @param productName name of the item
     * @param price price of the item
     */
    public Candy(String productName, int price){
        super(productName, price);

    }

    /**
     *
     * @return - output "Munch Munch, Yum" for any Candy object
     */
    @Override
    public String getJingle(){
        return  "Munch Munch, Yum!";
    }

}
