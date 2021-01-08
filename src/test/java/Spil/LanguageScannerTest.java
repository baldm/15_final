package Spil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageScannerTest {
    LanguageScanner langScanner = new LanguageScanner();
    @Test
    void getFileNames() {
        String[] expectedFileNameArray = {"Danish.properties", "English.properties"};
        String[] actualFileNameArray = langScanner.getFileNames();
        assertArrayEquals(expectedFileNameArray, actualFileNameArray);
    }

    @Test
    void getLanguageNames() {
        String[] expectedLangNameArray = {"Danish", "English"};
        String[] actualLangNameArray = langScanner.getLanguageNames();
        assertArrayEquals(expectedLangNameArray, actualLangNameArray);
    }
}