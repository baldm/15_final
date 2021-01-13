package Launcher;

import Gui.Interface;
import Spil.*;
import Spil.Fields.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class gameController {
    private static Player[] playerArray;
    private Language lang;
    private Interface gameInterface;
    private Field[] fieldArray;
    private Dice diceOne = new Dice(6);
    private Dice diceTwo = new Dice(6);

    public static void main(String[] args) {
        gameController game = new gameController();
        game.gameInit();




        // Simple turn
        while (true) {
            for (Player player : playerArray) {
                game.takeTurn(player);
            }
        }
    }

    // TODO: Write docstring
    private void gameInit() {

        LanguageScanner langScanner = new LanguageScanner();
        String[] languageArray = langScanner.getLanguageNames();

        // Creates a dialog for selecting a language
        gameInterface = new Interface();
        String langChoice = gameInterface.initLanguage(languageArray);
        lang = new Language(langChoice+".properties"); // Sets the language

        // Creates fields
        FieldFactory factory = new FieldFactory(lang);
        fieldArray = factory.getAllFields();

        // Dialog for entering player names
        String[] playerStringArray = gameInterface.initPlayerNames(lang);

        // Creates player objects
        playerArray = new Player[playerStringArray.length];
        for (int i = 0; i < playerStringArray.length; i++) {
            playerArray[i] = new Player(playerStringArray[i], 30000, i);
        }

        // Creates final game interface
        gameInterface.gameInit(fieldArray, lang);
    }

    // TODO: Write docstring
    private void takeTurn(Player currentPlayer) {
        gameInterface.displayMessage(lang.getString("PlayersTurn")+" "+currentPlayer.getName());
        if (currentPlayer.isInJail()) {
            // Jail logic here
            jailTurn(currentPlayer);
        } else {
            // Standard turn
            int sumRolls = diceRoll();
            // Moves player
            movePlayer(currentPlayer, sumRolls + currentPlayer.getPosition());
        }






        // Chance card logic
        Integer[] values = {2,7,17,22, 33, 36};                          // Replace with logic for field group
        List<Integer> intList = new ArrayList<>(Arrays.asList(values));  // Replace with logic for field group
        if (intList.contains(currentPlayer.getPosition())) {
            drawChanceCard(currentPlayer);
        }

        // Landed on buyable field logic
        /*
        if (true)  // Change to if fieldArray[currentPlayer.getPosition()].isBuyable() ??
        {
            buyableField(currentPlayer);

        }*/


    }

    private void drawChanceCard(Player player) {
        gameInterface.displayMessage(lang.getString("LandedOnChancecard"));

    }

    private void jailTurn(Player player) {
        gameInterface.displayMessage(lang.getString("LandedInJail"));

        if (player.getMoney() >= 1000) {
            String choice = gameInterface.displayMultiButton("Vil du betale for at komme ud?", "Betal 1000 kr", "Nej"); // TODO: Change to support language
            if (choice.equals("Betal 1000 kr")) {
                player.addMoney(-1000);
                gameInterface.setPlayerBalance(player);
                player.setInJail(false);
                player.hasBeenInJail = 0;
                int sumRolls = diceRoll();
                movePlayer(player, sumRolls + player.getPosition());


            } else {
                gameInterface.displayMessage("Du skal rulle 2 ens for at komme ud");  // TODO: Change to support language
                int sumRolls = diceRoll();
                if (player.hasBeenInJail > 2) {
                    gameInterface.displayMessage("Du har været i fængsel mere end 3 omgange\nHvis ikke du slår 2 ens nu skal du betale 1000 kr og rykke frem til summen af dit slag");  // TODO: Change to support language
                }
                if (diceOne.getValue() == diceTwo.getValue()) {
                    gameInterface.displayMessage("Du slå 2 ens og rykker frem til summen af terningerne");  // TODO: Change to support language
                    player.hasBeenInJail = 0;
                    player.setInJail(false);
                    movePlayer(player, sumRolls + player.getPosition());

                } else if (player.hasBeenInJail > 2) {
                    gameInterface.displayMessage("Du rykker frem til summen af slaget og betaler 1000 kr"); // TODO: Change to support language
                    player.hasBeenInJail = 0;
                    player.setInJail(false);
                    movePlayer(player, sumRolls + player.getPosition());
                } else {
                    gameInterface.displayMessage("Du slog ikke 2 ens og bliver i fængsel"); // TODO: Change to support language
                    player.hasBeenInJail++;
                }
            }
        }
    }

    private void buyableField(Player player) {
        gameInterface.displayMessage(lang.getString("LandedOnBuyableProperty"));

    }

    private int diceRoll() {
        gameInterface.displayMessage(lang.getString("RollDice"));

        // Rolls dices
        int sumRolls = diceOne.Roll() + diceTwo.Roll();
        // Displays dices and the result of dices on gui
        gameInterface.setBoardDice(diceOne.getValue(), diceTwo.getValue());
        gameInterface.displayMessage(lang.getString("DiceResult") + " " + diceOne.getValue() + " & " + diceTwo.getValue());
        return sumRolls;
    }

    private void movePlayer(Player player, int toPosition) {
        gameInterface.removePlayer(player);
        player.setPosition(toPosition);
        gameInterface.movePlayer(player);
    }


}
