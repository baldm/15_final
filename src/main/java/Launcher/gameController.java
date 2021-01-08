package Launcher;

import Gui.Interface;
import Spil.Dice;
import Spil.Language;
import Spil.LanguageScanner;
import Spil.Player;

public class gameController {
    private static Player[] playerArray;
    private Language lang;
    private Interface gameInterface;
    private Dice diceOne = new Dice(6);
    private Dice diceTwo = new Dice(6);

    public static void main(String[] args) {
        gameController game = new gameController();
        game.gameInit();

        // Simple turn
        while (true) {
            for (int i = 0; i < playerArray.length; i++) {
                game.takeTurn(playerArray[i]);
            }
        }
    }

    private void gameInit() {

        LanguageScanner langScanner = new LanguageScanner();
        String[] languageArray = langScanner.getLanguageNames();

        // Creates a dialog for selecting a language
        gameInterface = new Interface();
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
    private void takeTurn(Player currentPlayer) {
        if (currentPlayer.isInJail()) {
            gameInterface.displayMessage(lang.getString("LandedInJail"));
            // Jail logic here
        }

        gameInterface.displayMessage(lang.getString("PlayersTurn")+" "+currentPlayer.getName()+" "+lang.getString("RollDice"));

        int sumRolls = diceOne.Roll() + diceTwo.Roll();
        gameInterface.removePlayer(currentPlayer);
        currentPlayer.setPosition(sumRolls+currentPlayer.getPosition());

        gameInterface.setBoardDice(diceOne.getValue(), diceTwo.getValue());
        gameInterface.movePlayer(currentPlayer);
        gameInterface.displayMessage(lang.getString("DiceResult") + " " + diceOne.getValue() + " & " + diceTwo.getValue());

        gameInterface.setFieldHouses(1,3,currentPlayer);
        gameInterface.setFieldHouses(3,1,currentPlayer);
        gameInterface.setFieldHotel(6, true, currentPlayer);



    }
}
