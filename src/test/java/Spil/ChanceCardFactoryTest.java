package Spil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChanceCardFactoryTest {

    @Test
    void getCards() {
        Language lang = new Language("Danish");
        ChanceCard[] test;
        ChanceCardFactory FactoryTest = new ChanceCardFactory(lang);
        test = FactoryTest.getCards();

        assertEquals(test[0].getCardName(), lang.getString("internationalTest"));
    }
}