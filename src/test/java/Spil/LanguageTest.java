package Spil;

import junit.framework.TestCase;

public class LanguageTest extends TestCase {
    Language lang = new Language("Danish.properties");
    public void testGetString() {

                String test1 = lang.getString("Language");


                assertEquals(test1.equals("Danish") ,true);

    }

    public void testInternational(){
        String test = lang.getString("internationalTest");

        assertEquals(test.equals("æ ø å") ,true);
    }
}