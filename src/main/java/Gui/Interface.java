package Gui;

import Spil.Player;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Start;
import gui_main.GUI;

import java.awt.*;

// Nedenstående kode er en modificeret udgave, med udgangspunkt i:
// CDIO2 og CDIO3 af os, gruppe 15.

public class Interface {
    private GUI gui;
    private GUI_Player[] guiPlayerList;

    public Interface() {

    }
    public String initLanguage(String[] languages) {
        GUI_Field[] fields = new GUI_Field[]{};

        gui = new GUI(fields, Color.GRAY);
        String choice = displayMultiButtonMsg("Choose a language:", languages);
        return choice;
    }

    /**
     * Internal method for finding a player in the array of gui player objects
     * @param playerID - Integer
     * @return Specific gui Player Object
     */
    private GUI_Player findGuiPlayer(int playerID){
        return guiPlayerList[playerID];
    }

    /**
     * Method that moves a player on the grid
     * @param player - Player object
     */
    public void movePlayer(Player player){
        gui.getFields()[player.getPosition()].setCar(guiPlayerList[player.getID()], true);
    }

    /**
     * Display a message on the gui.
     * @param message
     */
    public void displayMessage(String message) {
        gui.showMessage("\n\n\n"+message);
    }

    /**
     * Sets the dice on the gui
     * @param roll1 Int
     * @param roll2 Int
     */
    public void setBoardDice(int roll1, int roll2 ) {
        gui.setDice(roll1, roll2);
    }

    /**
     * tilføjer en delta balance til player
     * @param player playerObject
     */

    public void setPlayerBalance(Player player) {
        guiPlayerList[player.getID()].setBalance(player.getMoney());
    }

    /**
     * Displays a message on the gui with  variable amount of choices
     * @param msg String - A message before the choice
     * @param args Strings seperated by comma
     */
    public String displayMultiButtonMsg(String msg, String... args) {
        return gui.getUserSelection(msg, args);
    }


    public String displayEnterStringMsg(String msg) {
        return gui.getUserString(msg);
    }

    /**
     * Displays a chance card on the gui.
     * @param input
     */
    public void displayChance(String input) {

        // It seems to work best calling both of these commands at the same time.
        gui.setChanceCard(input);
        gui.displayChanceCard(input);
    }





}
