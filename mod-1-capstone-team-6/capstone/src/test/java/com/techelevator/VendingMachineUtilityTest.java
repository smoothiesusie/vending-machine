package com.techelevator;

import com.techelevator.model.VendingMachineUtility;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class VendingMachineUtilityTest {

    LocalDateTime testDateTime;

    @Before
    public void testSetup(){
        testDateTime = LocalDateTime.of(2023, 10, 6, 13, 44, 30);
    }

    @Test
    public void doubleToPennies_returns_365_for_3_65(){
        //Arrange
        //Act
        int actual = VendingMachineUtility.doubleToPennies(3.65);
        int expected = 365;

        //Assert
        Assert.assertTrue("Error", actual == expected);
    }

    @Test
    public void penniesToDollarString_returns_$3_65_for_365(){
        //Arrange
        //Act
        String actual = VendingMachineUtility.penniesToDollarString(365);
        String expected = "$3.65";

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }


    @Test
    public void getFormattedDateAndTime_returns_sales_report_formatted_date_and_time(){
        //Arrange
        //handled in @Before

        //Act
        String actual = VendingMachineUtility.getFormattedDateAndTime(testDateTime, 2);
        String expected = "10/06/2023 1:44:30 PM";

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }

    @Test
    public void auditTransaction_appends_audit_action_to_log_file(){
        //Arrange
        String filePath = "logTest.txt";
        String action = "FEED MONEY";
        int moneyChange = 500;
        int newBalance = 1000;


        //Act
        //Update File for actual test
        try {
            VendingMachineUtility.auditTransaction(filePath, action, moneyChange, newBalance, testDateTime);
        }
        catch(IOException ex){
            //do nothing
        }

        String expected = "10/06/2023 1:44:30 PM FEED MONEY: $5.00 $10.00";
        String actual = "";

        try {
            File dataFile = new File(filePath);
            try (Scanner fileScanner = new Scanner(dataFile)) {
                while (fileScanner.hasNextLine()) {
                    String currentLine = fileScanner.nextLine();
                    if (!fileScanner.hasNextLine()){//if we're at the last line
                        actual = currentLine;
                    }
                }
            }
        } catch (IOException ex){
            //do nothing
        }

        //Assert
        Assert.assertTrue("Error", expected.equals(actual));

    }

    @Test
    public void convertPenniesIntoLargeCoins_returns_13_quarters_1_dime_4_pennies_for_339_pennies(){
        //Arrange
        int pennies = 339;

        //Act
        String actual = VendingMachineUtility.convertPenniesIntoLargeCoins(339);
        String expected = "Returning Change! You received 13 quarters(s), 1 dime(s), 0 nickle(s), and 4 penny(s).";

        //Assert
        Assert.assertTrue("Error", expected.equals(actual));
    }

    @Test
    public void validateMoneyInput_returns_true_for_5(){
        //Arrange
        int dollarInput = 5;
        int[] validBill = {1, 5, 10, 20};

        //Act
        boolean actual = VendingMachineUtility.validateMoneyInput(dollarInput, validBill);
        boolean expected = true;

        //Assert
        Assert.assertTrue("Error", expected == actual);
    }


    @After
    public void testCleanup(){
        testDateTime = null;
    }


}
