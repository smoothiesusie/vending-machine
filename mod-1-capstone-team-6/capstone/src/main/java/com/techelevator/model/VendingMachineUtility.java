package com.techelevator.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class VendingMachineUtility {

    //change values
    private static final int NICKLE = 5;
    private static final int DIME = 10;
    private static final int QUARTER = 25;


    /**
     * @param num double representing a dollar amount
     * @return amount of pennies
     */
    public static int doubleToPennies(double num) {
        double result = num * 100;
        return (int) result;
    }

    /**
     * @param num amount of pennies
     * @return string representation of dollar amount
     */
    public static String penniesToDollarString(int num) {
        DecimalFormat df = new DecimalFormat("0.00");
        double result = num / 100.0;
        return "$" + df.format(result);
    }

    /**
     * @param filePath    path to save the file
     * @param action      string identifier to be output on a line in the audit
     * @param moneyChange amount of money changed in the vending machine
     * @param newBalance  updated balance of the vending machine
     * @param timestamp   date and time to be output on a line in the audit
     * @throws IOException thrown when there is an error writing to the specified file path
     */
    public static void auditTransaction(String filePath, String action, int moneyChange, int newBalance, LocalDateTime timestamp) throws IOException {
        String formatDate = VendingMachineUtility.getFormattedDateAndTime(timestamp, 2);
        File logFile = new File(filePath);
        logFile.createNewFile();
        try (PrintWriter dataOutput = new PrintWriter(new FileOutputStream(logFile, true))) {
            String outputString = formatDate + " " + action + ": " + VendingMachineUtility.penniesToDollarString(moneyChange) + " " + VendingMachineUtility.penniesToDollarString(newBalance);
            dataOutput.println(outputString);
        }
    }

    /**
     * @param ex       exception thrown
     * @param filepath path to save the file
     */
    public static void logException(Exception ex, String filepath) {
        File errorFile = new File(filepath);
        try {
            errorFile.createNewFile();
            try (PrintWriter dataOutput = new PrintWriter(new FileOutputStream(errorFile, true))) {
                String outputString = ex.getMessage();
                dataOutput.println(outputString);
            }
        } catch (IOException e) {
            //we're already trying to log an error so if there's an issue with writing the error file just stop
        }
    }

    /**
     * @param timeStamp input date and time
     * @param style     input 1 for yyyy-MM-dd HH-mm-ss format , input 2 for MM/dd/yyyy h:mm:ss a format
     * @return string representation of the date and time based on input style
     */
    public static String getFormattedDateAndTime(LocalDateTime timeStamp, int style) {
        if (timeStamp == null) {
            timeStamp = LocalDateTime.now();
        }

        String dateTime = timeStamp.toString();

        if (style == 1) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            dateTime = timeStamp.format(formatter);
        }
        if (style == 2) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a");
            dateTime = timeStamp.format(formatter);
        }
        return dateTime;
    }

    /**
     * @param change pennies to be converted
     * @return String representation of quarters, nickles, dimes, and pennies to be returned to the user
     */
    public static String convertPenniesIntoLargeCoins(int change) {
        int pennyCount = change;
        int nickleCount = 0;
        int dimeCount = 0;
        int quarterCount = 0;

        String outputString = "";

        quarterCount = pennyCount / QUARTER; //if quarter has a remainder we'll automatically floor it by nature of it being an int, rinse and repeat for all coin types

        pennyCount -= quarterCount * QUARTER;

        dimeCount = pennyCount / DIME;

        pennyCount -= dimeCount * DIME;

        nickleCount = pennyCount / NICKLE;

        pennyCount -= nickleCount * NICKLE;


        outputString = String.format("Returning Change! You received %1$s quarters(s), %2$s dime(s), %3$s nickle(s), " +
                "and %4$s penny(s).", quarterCount, dimeCount, nickleCount, pennyCount);

        return outputString;
    }

    /**
     *
     * @param inputDollar dollar value to be checked
     * @param validBills integer array containing valid dollar values
     * @return true if inputDollar exists in validBills
     */
    public static boolean validateMoneyInput(int inputDollar, int[] validBills) {
        boolean result = false;
        for (int bill : validBills) {
            if (bill == inputDollar) {
                result = true;
            }
        }
        return result;
    }
}
