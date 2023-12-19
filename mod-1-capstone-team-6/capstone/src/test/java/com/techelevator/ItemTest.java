package com.techelevator;

import com.techelevator.model.Candy;
import com.techelevator.model.Chip;
import com.techelevator.model.Drink;
import com.techelevator.model.Gum;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ItemTest {

        @Test
    public void getJingle_for_chip_returns_crunch(){
            //Arrange
            Chip chip = new Chip("Lays", 250);

            //Act
            String actual = chip.getJingle();
            String expected = "Crunch Crunch, Yum!";

            //Assert
            Assert.assertTrue("Error", actual.equals(expected));

        }

    @Test
    public void getJingle_for_candy_returns_munch(){
        //Arrange
        Candy candy = new Candy("Snickers", 300);

        //Act
        String actual = candy.getJingle();
        String expected = "Munch Munch, Yum!";

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));

    }

    @Test
    public void getJingle_for_drink_returns_glug(){
        //Arrange
        Drink drink = new Drink("Pepsi", 275);

        //Act
        String actual = drink.getJingle();
        String expected = "Glug Glug, Yum!";

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));

    }

    @Test
    public void getJingle_for_gum_returns_chew(){
        //Arrange
        Gum gum = new Gum("Wrigleys", 175);

        //Act
        String actual = gum.getJingle();
        String expected = "Chew Chew, Yum!";

        //Assert
        Assert.assertTrue("Error", actual.equals(expected));

    }

}
