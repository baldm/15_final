package Spil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChanceCardTest {

    @Test
public void ChanceCardTest(){
        Language lang = new Language("Danish");
        ChanceCardTestDriver test = new ChanceCardTestDriver("test",lang);
        assertEquals(test.cardName,lang.getString("internationalTest"));
        assertEquals(test.cardID,0);
    }

}