package Launcher;

import Gui.Interface;
import Spil.Dice;
import Spil.Language;
import Spil.LanguageScanner;
import Spil.Player;

public class gameController {
    private Player[] playerArray;
    private Language lang;
    private Dice diceOne = new Dice(6);
    private Dice diceTwo = new Dice(6);

    public static void main(String[] args) {
        gameController game = new gameController();
        game.gameInit();
    }

    private void gameInit() {

        LanguageScanner langScanner = new LanguageScanner();
        String[] languageArray = langScanner.getLanguageNames();

        // Creates a dialog for selecting a language
        Interface gameInterface = new Interface();
        String langChoice = gameInterface.initLanguage(languageArray);
        lang = new Language(langChoice+".properties"); // Sets the language

        // Dialog for entering player names
        String[] playerStringArray = gameInterface.initPlayerNames(lang);

        // Creates player objects
        playerArray = new Player[playerStringArray.length];
        for (int i = 0; i < playerStringArray.length; i++) {
            playerArray[i] = new Player(playerStringArray[i], 30000, i);
        }

        // Creates final game interface
        gameInterface.gameInit();
    }
    private void takeTurn() {
        // Simple turn
        diceOne.roll();
    }
}
