package Launcher;

import Gui.Interface;
import Spil.Language;
import Spil.LanguageScanner;
import Spil.Player;

public class gameController {
    Player[] playerArray = new Player[];

    public static void main(String[] args) {
        gameInit();
    }

    private static void gameInit() {

        LanguageScanner langScanner = new LanguageScanner();
        String[] languageArray = langScanner.getLanguageNames();

        // Creates a dialog for selecting a language
        Interface gameInterface = new Interface();
        String langChoice = gameInterface.initLanguage(languageArray);
        Language lang = new Language(langChoice+".properties"); // Sets the language

        // Dialog for entering player names
        String[] playerStringArray = gameInterface.initPlayerNames(lang);

        // Creates player objects
        for (int i = 0; i < playerStringArray.length; i++) {

        }

        gameInterface.gameInit();




    }
}
