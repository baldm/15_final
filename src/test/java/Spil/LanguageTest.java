package Spil;

import junit.framework.TestCase;

public class LanguageTest extends TestCase {
    Language lang = new Language("test.properties");
    public void testGetString() {

                String test1 = lang.getString("test");
                String test2 = lang.getString("test2");

                assertEquals(test1.equals("test") && test2.equals("hello tester!"),true);

    }
}