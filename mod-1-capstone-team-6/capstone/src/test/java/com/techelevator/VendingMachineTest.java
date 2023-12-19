package com.techelevator;

import com.techelevator.exceptions.InsufficientFundsException;
import com.techelevator.exceptions.InvalidProductCodeException;
import com.techelevator.exceptions.OutOfStockException;
import com.techelevator.model.Chip;
import com.techelevator.model.Gum;
import com.techelevator.model.Item;
import com.techelevator.model.VendingMachine;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.Scanner;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class VendingMachineTest {

    VendingMachine vm;

    @Before
    public void testSetup(){
        vm = new VendingMachine();
        vm.loadInventory();
        vm.FeedMoney(1000);
    }

    @Test
    public void loadInventory_VM_with_identical_load_sheets_are_equal(){
        //Arrange
        VendingMachine testvm = new VendingMachine();
        testvm.loadInventory();

        //Act
        VendingMachine actual = vm;
        VendingMachine expected = testvm;

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }

    @Test
    public void generateSalesReport_outputs_expected_results_based_on_purchases(){
        //Arrange
        try {
            vm.purchaseItemAndPlayJingle("B2");
            vm.purchaseItemAndPlayJingle("A1");
            vm.purchaseItemAndPlayJingle("A1");
        }
        catch (Exception e){
            //do nothing
        }
        boolean testTrue = true;

        //Act
        String actualPath = vm.generateSalesReport();
        String expectedPath = "SalesReportTest.txt";

        File actual = new File(actualPath);
        File expected = new File(expectedPath);
        try (Scanner actualScanner = new Scanner(actual); Scanner
        expectedScanner = new Scanner(expected)){
            while (actualScanner.hasNext()){
                if (!actualScanner.nextLine().equals(expectedScanner.nextLine())){
                    testTrue = false;
                }
            }
        }
        catch (Exception ex){
            //do nothing
        }

        //Assert
        Assert.assertTrue("Error", testTrue);
    }

    @Test
    public void purchaseItemAndPlayJingle_returns_glug_for_purchased_drink(){
        //Arrange
        //Handled in @Before

        //Act
        String actual = "";
        try{
            actual = vm.purchaseItemAndPlayJingle("C3");
        }
        catch (Exception ex){
            //do nothing
        }

        String expected = "\nMountain Melter $1.50 $8.50 \nGlug Glug, Yum!";


        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }

    @Test
    public void giveChange_returns_1000_pennies(){
        //Arrange
        //Handled in @Before

        //Act
        int actual = vm.giveChange();
        int expected = 1000;

        //Assert
        Assert.assertTrue("Error", actual == expected);
    }

    @Test
    public void outputItem_returns_chiclet_gum_for_slot_d3(){
        //Arrange
        //Handled in @Before

        Item actual = null;
        //Act
        try {
            actual = vm.outputItem("D3");
        }
        catch (Exception ex) {
            //do nothing
        }
        Item expected = new Gum("Chiclets", 75);

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }

    @Test
    public void putItem_results_in_equal_vending_machine(){
        //Arrange
        VendingMachine testvm = new VendingMachine();
        testvm.loadInventory();
        testvm.putItem("Z1", new Chip("Doritos", 150));
        vm.putItem("Z1", new Chip("Doritos", 150));

        //Act
        VendingMachine actual = vm;
        VendingMachine expected = testvm;

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }

    @Test
    public void processItem_returns_chip_object_for_chip_type_string(){
        //Arrange
        Chip chip = new Chip("Fritos", 225);

        //Act
        Item actual = vm.processItem("Chip", "Fritos", "2.25");
        Item expected = chip;


        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }

    @Test
    public void purchaseItemAndPlayJingle_throws_OutOfStockException_if_item_unavailable(){
        //Arrange
        boolean testPath = false;
        //Act

        try {
            for (int i = 1; i <= 6; i++) {
                vm.purchaseItemAndPlayJingle("C1");
            }
        }
        catch (OutOfStockException ex){
             testPath = true;
        }
        catch (Exception ex){
            //do nothing
        }
        //Assert
        Assert.assertTrue("Error", testPath);

    }

    @Test
    public void purchaseItemAndPlayJingle_throws_InsufficientFundsException_if_not_enough_funds(){
        //Arrange
        boolean testPath = false;
        vm.giveChange();

        //Act
        try {
            vm.purchaseItemAndPlayJingle("A1");
            }

        catch (InsufficientFundsException ex){
            testPath = true;
        }
        catch (Exception ex){
            //do nothing
        }
        //Assert
        Assert.assertTrue("Error", testPath);

    }

    @Test
    public void purchaseItemAndPlayJingle_throws_InvalidProductCodeException_if_slot_location_does_not_exist(){
        //Arrange
        boolean testPath = false;

        //Act

        try {
            vm.purchaseItemAndPlayJingle("Z1");
        }

        catch (InvalidProductCodeException ex){
            testPath = true;
        }
        catch (Exception ex){
            //do nothing
        }
        //Assert
        Assert.assertTrue("Error", testPath);

    }

    @Test
    public void displayInventory_returns_identical_strings_for_equal_vending_machines(){
        //Arrange
        VendingMachine testvm = new VendingMachine();
        testvm.loadInventory();
        testvm.FeedMoney(1000);

        vm.putItem("Z1", new Chip("Doritos", 150));
        testvm.putItem("Z1", new Chip("Doritos", 150));

        try {
            vm.purchaseItemAndPlayJingle("A1");
            testvm.purchaseItemAndPlayJingle("A1");
        }
        catch (Exception ex){
            //do nothing
        }

        //Act
        String actual = vm.displayInventory();
        String expected = testvm.displayInventory();

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }


    @After
    public void testCleanup(){
        vm = null;
    }



}
