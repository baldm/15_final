package Spil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChanceCardChangeMoneyTest {
    Language lang = new Language("Danish");
    @Test
    void ChanceCardChangeMoneyTest(){
        ChanceCardChangeMoney test = new ChanceCardChangeMoney("FuldtStop.properties",lang);

        assertEquals(3,test.cardID);
        assertEquals(1,test.cardGroup);
        assertEquals("Kørt over for rødt",test.cardName);
        assertEquals("De har kørt over for rødt. Betal 1000 kroner i bøde",test.description);
        assertEquals(-1000,test.getMoneyChange());


    }

}