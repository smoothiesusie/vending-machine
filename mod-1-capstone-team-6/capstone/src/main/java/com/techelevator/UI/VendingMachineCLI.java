package com.techelevator.UI;

import com.techelevator.exceptions.InsufficientFundsException;
import com.techelevator.exceptions.InvalidProductCodeException;
import com.techelevator.exceptions.OutOfStockException;
import com.techelevator.model.VendingMachine;
import com.techelevator.model.VendingMachineUtility;

import java.util.Scanner;

public class VendingMachineCLI {

    //misc
    private static final String HIDDEN_MENU_FLAG = "***"; //append to any menu option that should be hidden to the user
    private static final int MENU_OFFSET = 1;
    private static final int[] ACCEPTED_BILLS = {1, 5, 10, 20};

    //main menu
    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String MAIN_MENU_OPTION_SALES_REPORT = "Generate Sales Report";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT,
            HIDDEN_MENU_FLAG + MAIN_MENU_OPTION_SALES_REPORT};

    //purchase menu
    private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
    private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
    private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
    private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION};

    private Scanner userInput;
    private VendingMachine vm;

    public VendingMachineCLI() {
        userInput = new Scanner(System.in);
    }

    /**
     * handles user CLI of the vending machine
     */
    public void run() {
        vm = new VendingMachine();
        vm.loadInventory();

        displayMainMenu();
    }

    /**
     * handles the main menu in vending machine CLI
     */
    private void displayMainMenu(){
        boolean runMenu = true;
        String[] currentMenu = MAIN_MENU_OPTIONS;

        while (runMenu) {

            System.out.println(writeMenu(currentMenu));;

            System.out.print("\nPlease make a selection: ");
            String selection = userInput.nextLine();

            try {
                int selectionIndex = Integer.parseInt(selection) - MENU_OFFSET; //user display is shifted up 1 from index, subtract 1 to match

                String menuOption = currentMenu[selectionIndex];

                if (menuOption.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                    System.out.println(vm.displayInventory());
                }
                if (menuOption.equals(MAIN_MENU_OPTION_PURCHASE)) {
                    displayPurchaseMenu();
                }
                if (menuOption.equals(MAIN_MENU_OPTION_EXIT)) {
                    runMenu = false; //Terminate While Loop
                    System.out.println("- Have a nice day! -");
                }
                if (menuOption.contains(MAIN_MENU_OPTION_SALES_REPORT)) {//do contains because it'll have *** in front
                    vm.generateSalesReport();
                    System.out.println("\nGenerating Sales Report!");
                }
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                System.out.printf("'%s' is not a valid option%n", selection);
            }
        }

    }

    /**
     * handles the purchase menu in the vending machine CLI
     */
    private void displayPurchaseMenu() {
        String[] currentMenu = PURCHASE_MENU_OPTIONS;

        boolean runMenu = true;
        while (runMenu) {

            System.out.println("\nCurrent Money Provided: " + VendingMachineUtility.penniesToDollarString(vm.getCurrentBalance()));

            System.out.println(writeMenu(currentMenu));;

            System.out.print("\nPlease make a selection: ");
            String selection = userInput.nextLine();

            try {
                int selectionIndex = Integer.parseInt(selection) - MENU_OFFSET; //user display is shifted up 1 from index, subtract 1 to match

                String menuOption = currentMenu[selectionIndex];

                if (menuOption.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
                    System.out.print("\nThis machine only accepts $1, $5, $10, and $20. Please enter in a valid dollar amount: ");
                    selection = userInput.nextLine();
                    selection = selection.replace("$", ""); //if they input $1, $5, etc. remove the $
                    selection = selection.replace(".00", ""); //if they input 10.00 change into 10
                    try {
                        int inputDollar = Integer.parseInt(selection);
                        if (VendingMachineUtility.validateMoneyInput(inputDollar, ACCEPTED_BILLS)) {
                            vm.FeedMoney(VendingMachineUtility.doubleToPennies(inputDollar));
                        } else {
                            System.out.println("\nThis machine only accepts $1, $5, $10, and $20 bills. Please try again.");
                        }
                    } catch (NumberFormatException ex) {
                        System.out.printf("\n'%s' is not a valid dollar amount, please try again!%n", selection);
                    }

                }

                if (menuOption.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {

                    System.out.println(vm.displayInventory());

                    System.out.print("\nPlease make a selection: ");
                    selection = userInput.nextLine();

                    try {
                        System.out.println(vm.purchaseItemAndPlayJingle(selection));
                    } catch (OutOfStockException ex) {
                        System.out.printf("\n'%s' selected - item is out of stock%n", selection);
                    } catch (InsufficientFundsException ex) {
                        System.out.printf("\n'%s' selected - not enough money to complete purchase%n", selection);
                    } catch (InvalidProductCodeException ex) {
                        System.out.printf("\n'%s' is not a valid product code%n", selection);
                    }
                }

                if (menuOption.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
                    runMenu = false; //Terminate While Loop
                    int change = vm.giveChange();
                    String coins = VendingMachineUtility.convertPenniesIntoLargeCoins(change);
                    System.out.println(coins);
                }
            } catch (Exception ex) {
                System.out.printf("'%s' Is not a Valid Option%n", selection);
            }
        }
    }

    /**
     *
     * @param menu String array containing menu options
     * @return String menu output to be printed
     */
    public static String writeMenu(String[] menu) {
        String outputString ="";
        outputString += "\n********************************";
        for (int i = 0; i < menu.length; i++) {
            if (!menu[i].startsWith(HIDDEN_MENU_FLAG)) {
               outputString += String.format("\n%1$s) %2$s", i + 1, menu[i]);
            }
        }
        outputString += "\n********************************";
        return outputString;
    }

}

