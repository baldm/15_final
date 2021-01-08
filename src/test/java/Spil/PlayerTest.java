package Spil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


// Nedenstående koder er en modificeret udgave, med udgangspunkt i:
// CDIO3, af os, gruppe 15

class PlayerTest {
    String name = "test";
    int money = 0;
    int addedMoney;
    Player player = new Player(name,money, 1);
    Dice dice = new Dice(0);
    int newpos;
    boolean[] ownedFields = new boolean[player.getOwnedFields().length];
    boolean[] ownsboth = new boolean[player.getOwnsboth().length];


    @Test
    void getName() {
        assertEquals(name, player.getName());
    }

    @Test
    void setName() {
        name = "test1";

        player.setName(name);
        assertEquals(name, player.getName());
    }

    @Test
    void getMoney() {
        assertEquals(money, player.getMoney());
    }

    @Test
    void addMoney() {
        addedMoney = dice.Roll();
        money += addedMoney;
        player.addMoney(addedMoney);
        assertEquals(money, player.getMoney());
    }

    @Test
    void getPosition() {
        assertEquals(player.getPosition(), 0);
    }

    @Test
    void setPosition() {
        newpos = player.getPosition() + 10;
        player.setPosition(newpos);
        assertEquals(newpos, player.getPosition());

        // Tests if going past start works
        newpos = player.getPosition() + 29;
        player.setPosition(newpos);
        assertEquals(newpos, player.getPosition());

        newpos = player.getPosition() + 5;
        player.setPosition(newpos);
        assertEquals(4, player.getPosition());

        // Tests jail
        player.setPosition(30);
        assertEquals(10, player.getPosition());
    }

    @Test
    void getOwnedFields() {
        ownedFields = player.getOwnedFields();
        for (int i = 0; i < player.getOwnedFields().length; i++) {
            assertFalse(ownedFields[i]);
        }
    }
    @Test
    void getOwnsboth() {
        ownsboth = player.getOwnsboth();
        for (int i = 0; i < ownsboth.length; i++) {
            assertFalse(ownedFields[i]);
        }
    }

    @Test
    void setOwnedFields() {
        for (int i = 0; i < player.getOwnedFields().length; i++) {
            ownedFields[i] = true;
        }
        for (int i = 0; i < player.getOwnedFields().length; i++) {
            assertTrue(ownedFields[i]);
        }

    }

    @Test
    void ownsboth() {
        for (int i = 0; i < player.getOwnedFields().length; i++) {
            ownedFields[i] = true;
        }
        for (int i = 0; i < ownsboth.length; i++) {
            assertTrue(ownedFields[i]);
        }
    }

    @Test
    void isInJail() {
        Player playerJail = new Player("Donald Trump",-10000, 45);
        assertFalse(playerJail.isInJail());
        playerJail.setPosition(30);
        assertTrue(playerJail.isInJail());
    }


}