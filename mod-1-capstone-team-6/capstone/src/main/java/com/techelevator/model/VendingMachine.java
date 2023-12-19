package com.techelevator.model;

import com.techelevator.exceptions.InsufficientFundsException;
import com.techelevator.exceptions.InvalidProductCodeException;
import com.techelevator.exceptions.OutOfStockException;

import java.io.*;
import java.util.*;

public class VendingMachine {


    //field values
    private int currentBalance = 0;
    private final Map<String, List<Item>> currentInventory = new TreeMap<>();
    private final Map<String, Item> itemMenu = new TreeMap<>();


    //static save file locations
    private static final String DEFAULT_INV_FILE = "vendingmachine.csv";
    private static final String DEFAULT_AUDIT_FILE = "Log.txt";
    private static final String DEFAULT_SALES_FILE = "SalesReport";
    private static final String DEFAULT_ERROR_FILE = "error.txt";


    //string identifiers for item subclasses
    private static final String CHIP_NAME = "Chip";
    private static final String CANDY_NAME = "Candy";
    private static final String DRINK_NAME = "Drink";
    private static final String GUM_NAME = "Gum";


    //string audit action identifiers
    private static final String FEED_MONEY = "FEED MONEY";
    private static final String GIVE_CHANGE = "GIVE CHANGE";


    //misc.
    private static final int STOCK_NUMBER = 5;


    //getters and setters
    /**
     *
     * @return vending machine balance
     */
    public int getCurrentBalance() {
        return currentBalance;
    }

    /**
     *
     * @param currentBalance value to set the vending machine balance to
     */
    private void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     *
     * @return map representation of current items in stock
     */
    public Map<String, List<Item>> getCurrentInventory() {
        return currentInventory;
    }

    /**
     *
     * @return map representation of items available to purchase
     */
    public Map<String, Item> getItemMenu() {
        return itemMenu;
    }

    //constructor
    public VendingMachine() {
    }


    //public methods

    /**
     * stocks vending machine object with items to sell based on specified CSV file
     */
    public void loadInventory() {

        try {
            File dataFile = new File(DEFAULT_INV_FILE);
            try (Scanner fileScanner = new Scanner(dataFile)) {
                while (fileScanner.hasNext()) {
                    String[] data = fileScanner.nextLine().split("\\|");
                    for (int i = 0; i < STOCK_NUMBER; i++) {
                        Item currentItem = processItem(data[3], data[1], data[2]);
                        putItem(data[0], currentItem);
                    }
                }
            }
        } catch (IOException ex) {
            VendingMachineUtility.logException(ex, DEFAULT_ERROR_FILE);
        }
    }

    /**
     *
     * @return returns list of objects sold
     */
    public String generateSalesReport() {

        String formatDate = VendingMachineUtility.getFormattedDateAndTime(null,1);
        String updatedPath = DEFAULT_SALES_FILE + " " + formatDate + ".txt";

        try {

            File salesFile = new File(updatedPath);
            salesFile.createNewFile();

            try (PrintWriter dataOutput = new PrintWriter(new FileOutputStream(salesFile, true))) {

                int totalValueSold = 0;

                for (Map.Entry<String, Item> itemEntry : getItemMenu().entrySet()) {

                    String slotLocation = itemEntry.getKey();
                    Item currentItem = itemEntry.getValue();
                    int amountSold = STOCK_NUMBER - getCurrentInventory().get(slotLocation).size();

                    totalValueSold = totalValueSold + currentItem.getPrice() * amountSold;
                    String name = currentItem.getProductName();

                    String outputLine = name + "|" + amountSold;
                    dataOutput.println(outputLine);
                }

                dataOutput.println("\n**TOTAL SALES** " + VendingMachineUtility.penniesToDollarString(totalValueSold));

            }
        } catch (IOException ex) {
            VendingMachineUtility.logException(ex, DEFAULT_ERROR_FILE);
        }
        return updatedPath;
    }

    /**
     *
     * @param slotLocation String representation of the slot location to retrieve the requested item from a map
     * @return String that contains the item name, item price, updated user balance, and the associated jingle with the type of item
     * @throws OutOfStockException thrown if vending machine doesn't contain anymore of the requested item
     * @throws InsufficientFundsException thrown if user has not input enough money to buy requested item
     * @throws InvalidProductCodeException thrown if user enters gibberish or a slot location that doesn't exist
     */
    public String purchaseItemAndPlayJingle(String slotLocation) throws OutOfStockException, InsufficientFundsException, InvalidProductCodeException {
        String returnString = null;

        if (getItemMenu().get(slotLocation) == null){
            throw new InvalidProductCodeException("Invalid slot has been selected");
        }
        int itemPrice = getItemMenu().get(slotLocation).getPrice();

        if (getCurrentBalance() >= itemPrice) { //they have enough money
            Item returnItem = outputItem(slotLocation);
            int newBalance = getCurrentBalance() - itemPrice;
            returnString = "\n" + returnItem.getProductName() + " " + VendingMachineUtility.penniesToDollarString(returnItem.getPrice())
                    + " " + VendingMachineUtility.penniesToDollarString(newBalance) + " " + "\n"+ returnItem.getJingle();
            setCurrentBalance(newBalance);
            try {
                VendingMachineUtility.auditTransaction(DEFAULT_AUDIT_FILE, returnItem.getProductName() + " " + slotLocation, itemPrice, newBalance, null);
            }
            catch (IOException ex){
                VendingMachineUtility.logException(ex, DEFAULT_ERROR_FILE);
            }
        } else {
            throw new InsufficientFundsException("Not enough money in balance to make purchase");
        }
        return returnString;
    }

    /**
     *
     * @param inputMoney penny amount added to vending machine's current balance
     */
    public void FeedMoney(int inputMoney) {
        int newBalance = getCurrentBalance() + inputMoney;
        setCurrentBalance(newBalance);
        try {
            VendingMachineUtility.auditTransaction(DEFAULT_AUDIT_FILE, FEED_MONEY, inputMoney, newBalance, null);
        }
        catch (IOException ex){
            VendingMachineUtility.logException(ex, DEFAULT_ERROR_FILE);
        }
    }

    /**
     *
     * @return returns vending machine balance and sets vending machine's balance to zero
     */
    public int giveChange() {
        int change = getCurrentBalance();
        setCurrentBalance(0);
        try {
            VendingMachineUtility.auditTransaction(DEFAULT_AUDIT_FILE, GIVE_CHANGE, change, 0, null);
        }
        catch (IOException ex){
            VendingMachineUtility.logException(ex, DEFAULT_ERROR_FILE);
        }
        return change;
    }

    /**
     *
     * @return string list of items currently stocked in vending machine, including location, price, name, and current amount
     */
    public String displayInventory(){

        Map<String, List<Item>> currentInventory = getCurrentInventory();

        String outputString = "";

        for (Map.Entry<String, Item> itemEntry : getItemMenu().entrySet()){
            String slotLocation = itemEntry.getKey();
            Item currentItem = itemEntry.getValue();
            String productName = currentItem.getProductName();
            String dollarPrice = VendingMachineUtility.penniesToDollarString(currentItem.getPrice());
            int itemCount = currentInventory.get(slotLocation).size();

            if (itemCount < 1){
                outputString += "\n" + slotLocation + " " + productName + " " + dollarPrice + " " + "Sold Out";
            }
            else {
                outputString += "\n" + slotLocation + " " + productName + " " + dollarPrice + " " + itemCount + " Remaining";
            }

        }
        return outputString;
    }


    //helper methods

    /**
     *
     * @param slotLocation location to retrieve item from vending machine
     * @return retrieved item
     * @throws OutOfStockException item is no longer in stock
     */
    public Item outputItem(String slotLocation) throws OutOfStockException {
        Item returnItem = null;
        List<Item> inventoryList = currentInventory.get(slotLocation);
        if (inventoryList.size() == 0){
            throw new OutOfStockException("Item is out of stock");
        }
        returnItem = inventoryList.remove(0);
        return returnItem;
    }

    /**
     *
     * @param slotLocation location to stock item in vending machine
     * @param item item to be stocked
     */
    public void putItem(String slotLocation, Item item) {
        List<Item> inventoryList = currentInventory.get(slotLocation);
        if (inventoryList == null) {
            inventoryList = new ArrayList<>();
            currentInventory.put(slotLocation, inventoryList);
        }
        inventoryList.add(item);
        getItemMenu().put(slotLocation, item);
    }

    /**
     *
     * @param itemType string representation of item subclass to create
     * @param itemName name of item
     * @param itemPrice price of item
     * @return newly instantiated item
     */
    public Item processItem(String itemType, String itemName, String itemPrice) {
        Item item = null;
        int pennyPrice = VendingMachineUtility.doubleToPennies(Double.parseDouble(itemPrice));

        if (itemType.equals(CHIP_NAME)) {
            item = new Chip(itemName, pennyPrice);
        }
        if (itemType.equals(CANDY_NAME)) {
            item = new Candy(itemName, pennyPrice);
        }
        if (itemType.equals(DRINK_NAME)) {
            item = new Drink(itemName, pennyPrice);
        }
        if (itemType.equals(GUM_NAME)) {
            item = new Gum(itemName, pennyPrice);
        }
        return item;
    }


    //generic overridden methods
    /**
     *
     * @param o object to compare
     * @return returns true if vending machines compared have the same contents; false if they don't
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendingMachine that = (VendingMachine) o;
        return Objects.equals(currentInventory, that.currentInventory);
    }

    /**
     *
     * @return returns a hash representation of a vending machine
     */
    @Override
    public int hashCode() {
        return Objects.hash(currentInventory);
    }

    /**
     *
     * @return returns a string representation of a vending machine
     */
    @Override
    public String toString() {
        return "VendingMachine{" +
                "currentBalance=" + currentBalance +
                ", currentInventory=" + currentInventory +
                '}';
    }

}
