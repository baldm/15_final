package Spil;

import Spil.ChanceCards.ChanceCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class ChanceCardFactoryTest {

   @Test
    void getCards() {
        Language lang = new Language("Danish");
        ChanceCard[] test;
        ChanceCardFactoryTestDriver FactoryTest = new ChanceCardFactoryTestDriver(lang);
        test = FactoryTest.getCards();

        assertEquals(test[0].cardName, lang.getString("internationalTest"));
    }
}