package Spil;

import Spil.ChanceCards.ChanceCardChangeMoney;
import Spil.ChanceCards.ChanceCardMovePlayer;
import Spil.ChanceCards.ChanceCardMovePlayerTo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChanceCardTest {
    Language lang = new Language("Danish.properties");
    @Test
    public void ChanceCardTest(){

        ChanceCardTestDriver test = new ChanceCardTestDriver("test.properties",lang);
        assertEquals(lang.getString("internationalTest"),test.cardName);
        assertEquals(0,test.cardID);

    }

    @Test
    void ChanceCardChangeMoneyTest(){
        ChanceCardChangeMoney test = new ChanceCardChangeMoney("FuldtStop.properties",lang);

        assertEquals(3,test.cardID);
        assertEquals(2,test.cardGroup);
        assertEquals("Kørt over for rødt",test.cardName);
        assertEquals("De har kørt over for rødt. Betal 1000 kroner i bøde",test.description);
        assertEquals(-1000,test.getMoneyChange());
        assertEquals(1,test.cardCount);


    }
    @Test
    void ChanceCardMovePlayerToTest(){
        ChanceCardMovePlayerTo test = new ChanceCardMovePlayerTo("MoveToStart.properties",lang);

        assertEquals(24,test.cardID);
        assertEquals(3,test.cardGroup);
        assertEquals("Ryk felt",test.cardName);
        assertEquals("Ryk frem til START",test.description);
        assertEquals(2,test.cardCount);
        assertEquals(0,test.getMoveTo());



    }
    @Test
    void ChanceCardMovePlayerTest(){
        ChanceCardMovePlayer test = new ChanceCardMovePlayer("MoveForward.properties",lang);

        assertEquals(26,test.cardID);
        assertEquals(4,test.cardGroup);
        assertEquals("Ryk felt",test.cardName);
        assertEquals("Ryk tre felter frem",test.description);
        assertEquals(1,test.cardCount);
        assertEquals(3,test.getMovePlayer());



    }

}