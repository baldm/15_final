package Spil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChanceCardTest {

    @Test
public void ChanceCardTest(){
        Language lang = new Language("Danish");
        ChanceCard test = new ChanceCard("test",lang);
        assertEquals(test.getCardName(),lang.getString("internationalTest"));
    }

}