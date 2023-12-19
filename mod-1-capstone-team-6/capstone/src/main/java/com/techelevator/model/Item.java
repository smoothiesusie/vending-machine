package com.techelevator.model;

import java.util.Objects;

public abstract class Item {
    private String productName;
    private int price;

    /**
     *
     * @return name of the product
     *
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @return price of Item
     */
    public int getPrice(){
        return price;
    }

    /**
     *
     * @param productName name of product
     * @param price price of product
     */
    public Item(String productName, int price){
        this.productName = productName;
        this.price = price;
    }

    /**
     *
     * @return jingle of item purchased
     */
    public abstract String getJingle();

    /**
     *
     * @return string representation of item object
     */
    @Override
    public String toString(){
        return this.getProductName() + " " + this.getPrice();
    }

    /**
     *
     * @param o object to be compared
     * @return true if the objects are equal; false if they are not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return price == item.price && Objects.equals(productName, item.productName);
    }

    /**
     *
     * @return returns a hash representation of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(productName, price);
    }

}

