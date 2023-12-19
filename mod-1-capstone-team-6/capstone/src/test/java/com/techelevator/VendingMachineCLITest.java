package com.techelevator;

import com.techelevator.UI.VendingMachineCLI;
import org.junit.*;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class VendingMachineCLITest {

    //most methods in here deal with Console IO so we didn't test them in JUNIT and instead made sure to do extensive application testing to verify it behaved as expected

    VendingMachineCLI app;

    @Before
    public void testSetup(){
        app = new VendingMachineCLI();
    }

    @Test
    public void writeMenu_behaves_as_expected(){
        //Arrange
        String HIDDEN_MENU_FLAG = "***";
        String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
        String MAIN_MENU_OPTION_PURCHASE = "Purchase";
        String MAIN_MENU_OPTION_EXIT = "Exit";
        String MAIN_MENU_OPTION_SALES_REPORT = "Generate Sales Report";
        String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT,
                HIDDEN_MENU_FLAG + MAIN_MENU_OPTION_SALES_REPORT};

        //Act
        String actual = VendingMachineCLI.writeMenu(MAIN_MENU_OPTIONS);
        String expected = "\n********************************" + "\n1) Display Vending Machine Items" + "\n2) Purchase" + "\n3) Exit" + "\n********************************";

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));
    }

    @After
    public void testCleanup(){
        app = null;
    }


}
