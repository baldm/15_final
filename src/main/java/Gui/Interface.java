package Gui;

import Spil.Language;
import Spil.Player;
import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;

import java.awt.*;

// Nedenstående kode er en modificeret udgave, med udgangspunkt i:
// CDIO2 og CDIO3 af os, gruppe 15.

public class Interface {
    private GUI gui;
    private GUI_Player[] guiPlayerList;


    /**
     * Asks the player to choose a language
     * @param languages Array of possible languages
     * @return Chosen language
     */
    public String initLanguage(String[] languages) {
        GUI_Field[] fields = new GUI_Field[]{};

        gui = new GUI(fields, Color.GRAY);
        return displayMultiButtonMsg("Choose a language:", languages);
    }

    /**
     * Method for choosing player names, also creates the gui Player objects
     * @param lang Language object
     * @return Array of Strings containing player names
     */
    public String[] initPlayerNames(Language lang) {
        // Asks for the amount of players in the game
        String stringNumPlayers = displayMultiButtonMsg(lang.getString("EnterPlayerNumber"), "2", "3", "4", "5", "6");
        int numPlayers = Integer.parseInt(stringNumPlayers);

        // Creates arrays to store player names and GUI player objects
        String[] playerArray = new String[numPlayers];
        guiPlayerList = new GUI_Player[numPlayers];

        // Loops over players
        for (int i = 0; i < numPlayers; i++) {
            playerArray[i] = displayEnterStringMsg(lang.getString("EnterPlayerName"));
            String playerIcon = displayMultiButtonMsg(
                    lang.getString("EnterPlayerIcon"),
                    lang.getString("Car"),
                    lang.getString("Racecar"),
                    lang.getString("Tractor"),
                    "UFO");

            // Handles the type of car
            GUI_Car.Type type = switch (playerIcon) {
                case "Tractor" -> GUI_Car.Type.TRACTOR;
                case "Racecar" -> GUI_Car.Type.RACECAR;
                case "UFO" -> GUI_Car.Type.UFO;
                default -> GUI_Car.Type.CAR;
            };

            // Creates the car object
            new GUI_Car();
            GUI_Car car = switch (i) {
                case 0 -> new GUI_Car(Color.BLUE, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 1 -> new GUI_Car(Color.RED, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 2 -> new GUI_Car(Color.YELLOW, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 3 -> new GUI_Car(Color.GREEN, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 4 -> new GUI_Car(Color.PINK, Color.BLACK, type, GUI_Car.Pattern.CHECKERED);
                default -> new GUI_Car();
            };
            // Adds player to storage
            guiPlayerList[i] = new GUI_Player(playerArray[i], 30000, car);

        }
        gui.close();
        return playerArray;
    }


    public void gameInit(){
        gui = new GUI();
        for (GUI_Player gui_player : guiPlayerList) {
            gui.addPlayer(gui_player);
        }
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
     * Method that removes a player on the grid
     * @param player - Player object
     */
    public void removePlayer(Player player){
        gui.getFields()[player.getPosition()].setCar(guiPlayerList[player.getID()], false);
    }

    /**
     * Display a message on the gui.
     * @param message String message
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

    /**
     * Displays a box to type in
     * @param msg - String message
     * @return String - Input value
     */
    public String displayEnterStringMsg(String msg) {
        return gui.getUserString(msg);
    }

    /**
     * Displays a chance card on the gui.
     * @param input String message
     */
    public void displayChance(String input) {

        // It seems to work best calling both of these commands at the same time.
        gui.setChanceCard(input);
        gui.displayChanceCard(input);
    }

    /**
     * Sets the houses on a field - Remember to update players balance before buying a house
     * @param fieldID - The id of the field, max 39
     * @param houseAmount - amount of houses on field, max 4
     * @param player - player object the owns the house
     */
    public void setFieldHouses(int fieldID, int houseAmount, Player player) {
        GUI_Field field = gui.getFields()[fieldID];     // Finds the field
        GUI_Street street = (GUI_Street) field;   // Changes the field to a street field
        street.setHouses(houseAmount);
        setPlayerBalance(player); // Updates the gui balance of the player
    }

    /**
     *  Sets the hotel on a field - Remember to update players balance before buying a hotel
     * @param fieldID - The id of the field, max 39
     * @param hotelBool - true for a hotel, false for none
     * @param player - player object the owns the hotel
     */
    public void setFieldHotel(int fieldID, boolean hotelBool, Player player) {
        GUI_Field field = gui.getFields()[fieldID];     // Finds the field
        GUI_Street street = (GUI_Street) field;   // Changes the field to a street field
        street.setHotel(hotelBool);
        setPlayerBalance(player); // Updates the gui balance of the player
    }

}
