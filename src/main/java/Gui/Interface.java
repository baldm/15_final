package Gui;

import Spil.Fields.*;
import Spil.Language;
import Spil.Player;
import gui_fields.*;
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
        return displayDropdown("Choose a language:", languages);
    }

    /**
     * Method for choosing player names, also creates the gui Player objects
     * @param lang Language object
     * @return Array of Strings containing player names
     */
    public String[] initPlayerNames(Language lang) {
        // Asks for the amount of players in the game
        String stringNumPlayers = displayDropdown(lang.getString("EnterPlayerNumber"), "2", "3", "4", "5", "6");
        int numPlayers = Integer.parseInt(stringNumPlayers);

        // Creates arrays to store player names and GUI player objects
        String[] playerArray = new String[numPlayers];
        guiPlayerList = new GUI_Player[numPlayers];

        // Loops over players
        for (int i = 0; i < numPlayers; i++) {
            playerArray[i] = displayEnterStringMsg(lang.getString("EnterPlayerName"));
            String playerIcon = displayDropdown(
                    lang.getString("EnterPlayerIcon"),
                    lang.getString("Car"),
                    lang.getString("Racecar"),
                    lang.getString("Tractor"),
                    "UFO");

            // Handles the type of car
            GUI_Car.Type type;
            if (playerIcon.equals(lang.getString("Racecar"))) {
                type = GUI_Car.Type.RACECAR;
            } else if (playerIcon.equals(lang.getString("Tractor"))) {
                type = GUI_Car.Type.TRACTOR;
            } else if (playerIcon.equals("UFO")) {
                type = GUI_Car.Type.UFO;
            } else {
                type = GUI_Car.Type.CAR;
            }

            // Creates the car object
            new GUI_Car();
            GUI_Car car = switch (i) {
                case 0 -> new GUI_Car(Color.BLUE, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 1 -> new GUI_Car(Color.RED, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 2 -> new GUI_Car(Color.YELLOW, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 3 -> new GUI_Car(Color.GREEN, Color.BLACK, type, GUI_Car.Pattern.FILL);
                case 4 -> new GUI_Car(Color.PINK, Color.BLACK, type, GUI_Car.Pattern.CHECKERED);
                case 5 -> new GUI_Car(Color.MAGENTA, Color.BLACK, type, GUI_Car.Pattern.CHECKERED);
                default -> new GUI_Car();
            };
            // Adds player to storage
            guiPlayerList[i] = new GUI_Player(playerArray[i], 30000, car);

        }
        gui.close();
        return playerArray;
    }


    public void gameInit(Field[] fieldArray, Language lang){

        // Handles field objects
        GUI_Field[] guiFieldArray = new GUI_Field[fieldArray.length];

        // Builds gui fields

        for (int i = 0; i < fieldArray.length; i++) {
            Field currentField = fieldArray[i];

            // Prints for debugging
            /*
            System.out.println("\nNow processing field:"+currentField.name);
            System.out.println("Field id:"+currentField.fieldID);
            System.out.println("Field type:"+currentField.fieldType);
            System.out.println("Field position:"+currentField.position);
            */

            // Creates field color by field id
            Color curColor = null;
            try {
                curColor = switch (((FieldProperty) currentField).getGroupID()) {
                    case 0 -> curColor = Color.BLUE;
                    case 1 -> curColor = Color.ORANGE;
                    case 2 -> curColor = Color.YELLOW;
                    case 3 -> curColor = Color.GRAY;
                    case 4 -> curColor = Color.RED;
                    case 5 -> curColor = Color.WHITE;
                    case 6 -> curColor = Color.PINK;
                    case 7 -> curColor = Color.MAGENTA;
                    default -> curColor = Color.WHITE;
                };
            } catch (ClassCastException e) {
                // Only here to avoid casting non properties to FieldProperty
            }

            // Sorts types of field into their respective class and values
            switch (currentField.fieldType) {
                case 1 -> {
                    guiFieldArray[i] = new GUI_Street(currentField.name, String.valueOf(((FieldProperty) currentField).getPrice()), descGen(currentField, lang), "0", curColor, Color.BLACK); //String.valueOf(((FieldProperty) currentField).getMortageValue())
                }
                case 2 -> {
                    guiFieldArray[i] = new GUI_Shipping("default", currentField.name, String.valueOf(((FieldScandlines) currentField).getPrice()), descGen(currentField, lang), "0", Color.CYAN, Color.BLACK);
                }
                case 3 -> {
                    if (currentField.name.equals("Squash")) {
                        guiFieldArray[i] = new GUI_Brewery("./Images/squash.PNG", currentField.name, String.valueOf(((FieldSoda) currentField).getPrice()), descGen(currentField, lang), "0", Color.CYAN, Color.BLACK);
                    } else {
                        guiFieldArray[i] = new GUI_Brewery("./Images/cola.PNG", currentField.name, String.valueOf(((FieldSoda) currentField).getPrice()), descGen(currentField, lang), "0", Color.CYAN, Color.BLACK);
                    }
                }
                case 4 -> {
                    switch (currentField.name) {
                        case "Parking" -> guiFieldArray[i] = new GUI_Refuge("./Images/parking.PNG", currentField.name, lang.getString("Parking"), lang.getString("ParkingDescription"), Color.GRAY, Color.BLACK);
                        case "START" -> guiFieldArray[i] = new GUI_Start("Start", "4000", lang.getString("StartDescription"), Color.RED, Color.BLACK);
                        case "Indkomstskat" -> guiFieldArray[i] = new GUI_Tax(currentField.name, lang.getString("10or4000"), lang.getString("IndkomstskatDescription"), Color.GRAY, Color.BLACK);
                        case "Statsskat" -> guiFieldArray[i] = new GUI_Tax(currentField.name, "2.000 kr", lang.getString("StatsskatDescription"), Color.GRAY, Color.BLACK);
                    }
                }
                case 5 -> {
                    guiFieldArray[i] = new GUI_Chance("?", lang.getString("ChanceCard"), lang.getString("ChanceCardDescription"), Color.BLACK, Color.GREEN);
                }
                case 6 -> {
                    if (currentField.name.equals("Jail")) {
                        guiFieldArray[i] = new GUI_Jail("default", currentField.name, lang.getString("Jail"), lang.getString("JailDescription"), Color.BLACK, Color.WHITE);
                    } else {
                        guiFieldArray[i] = new GUI_Jail("default", currentField.name, lang.getString("JailVisit"), lang.getString("JailVisitDescription"), Color.BLACK, Color.WHITE);
                    }
                }
            }
        }


        // Creates final gui
        gui = new GUI(guiFieldArray, Color.GREEN);

        // Adds players to the game
        for (GUI_Player gui_player : guiPlayerList) {
            gui.addPlayer(gui_player);
            gui_player.getCar().setPosition(gui.getFields()[0]);
        }
    }

    /**
     * Helper function to generate descriptions for ownable fields on the gui
     * @param field - The field object
     * @param lang - the language object
     * @return
     */
    private String descGen(Field field, Language lang) {
        // Sorts into field type
        switch (field.fieldType) {
            case 1 -> {
                int[] rent = ((FieldProperty) field).getRent();
                return lang.getString("RentOf") + rent[0] +
                        lang.getString("OneHouse") + rent[1] +
                        lang.getString("TwoHouse")+ rent[2] +
                        lang.getString("ThreeHouse")+ rent[3] +
                        lang.getString("FourHouse")+ rent[4] +
                         "\\n>> Hotel:___________" + rent[5] +
                        lang.getString("HouseCost")+ ((FieldProperty) field).getHousePrice()+
                        lang.getString("HotelCost")+ ((FieldProperty) field).getHousePrice()+
                        lang.getString("MortageValue")+ ((FieldProperty) field).getMortageValue();
            }
            case 2 -> {
                int[] rent = ((FieldScandlines) field).getRent();
                return lang.getString("RentFerry") + rent[0] +
                        lang.getString("TwoFerry") + rent[1] +
                        lang.getString("ThreeFerry") + rent[2] +
                        lang.getString("FourFerry") + rent[3] +
                        lang.getString("MortageValue")+ ((FieldScandlines) field).getMortageValue();
            }
            case 3 -> {
                return  lang.getString("SodaDescription")+
                        "\n\n\n"+
                        lang.getString("MortageValue") + ((FieldSoda) field).getMortageValue();
            }
        }

        return null;
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
        // Gets the gui player object
        // Gets the car of the player object
        // Gets the gui field object that the player is on
        // Moves the player
        guiPlayerList[player.getID()].getCar().setPosition( gui.getFields()[player.getPosition()]);
    }

    /**
     * deprecated  - With matadorgiu:3.2.1 removes the car one the board by itself.
     * @param player - Player object
     */
    public void removePlayer(Player player){
        // Gets the field that the player is on
        // Gets the gui player object
        // Hides the car from the board
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
    public String displayDropdown(String msg, String... args) {
        return gui.getUserSelection((msg), args);
    }

    /**
     * Displays a box to type in
     * @param msg - String message
     * @return String - Input value
     */
    public String displayEnterStringMsg(String msg) {
        return gui.getUserString("\n\n\n"+msg);
    }

    /**
     *  Displays a number of buttons dependant on arguments
     * @param msg - String message
     * @param args - Strings seperated by comma
     * @return
     */
    public String displayMultiButton(String msg, String... args) {
        return gui.getUserButtonPressed(("\n\n\n"+msg), args);
    }

    /**
     * Displays a chance card on the gui.
     * @param input String message
     */
    public void displayChance(String input) {
        gui.displayChanceCard(input);
        displayMessage("  "); // Ensures that the player has read the chance card
        gui.displayChanceCard();
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

    /**
     * Makes the owner of the field shown on the board
     * @param player - The player object that owns the field (null for remove owner)
     * @param fieldID - Position of the field
     */
    public void setOwner(Player player, int fieldID) {
        GUI_Field field = gui.getFields()[fieldID];     // Finds the field
        GUI_Ownable guiField = (GUI_Ownable) field;   // Changes the field to a street field

        // Check if the owner has to be reverted to default
        if (player == null) {
            guiField.setBorder(Color.BLACK);
            guiField.setOwnerName(" ");
        } else {
            guiField.setOwnerName(player.getName());
            guiField.setBorder(guiPlayerList[player.getID()].getPrimaryColor());
            setPlayerBalance(player); // Updates the gui balance of the player
        }
    }

    /**
     * Removes the owner on the board
     * @param player - Player which doesnt have the field anymore
     * @param fieldID - Position of the field
     */
    public void removeOwner(Player player, int fieldID) {
        GUI_Field field = gui.getFields()[fieldID];     // Finds the field
        GUI_Ownable guiField = (GUI_Ownable) field;   // Changes the field to a street field
        guiField.setOwnerName(null);
        guiField.setBorder(Color.BLACK);
        setPlayerBalance(player); // Updates the gui balance of the player
    }

    /**
     * Gives the field a crisscrossed pattern
     * @param player Player that owns it
     * @param fieldID The field position
     */
    public void pledgeField(Player player, int fieldID) {
        GUI_Field field = gui.getFields()[fieldID];     // Finds the field
        GUI_Ownable guiField = (GUI_Ownable) field;   // Changes the field to a street field
        guiField.setBorder(guiPlayerList[player.getID()].getPrimaryColor(), Color.BLACK);
        setPlayerBalance(player); // Updates the gui balance of the player
    }

    /**
     * Removes the crisscrossed pattern
     * @param player
     * @param fieldID
     */
    public void unpledgeField(Player player, int fieldID) {
        GUI_Field field = gui.getFields()[fieldID];     // Finds the field
        GUI_Ownable guiField = (GUI_Ownable) field;   // Changes the field to a street field
        guiField.setBorder(guiPlayerList[player.getID()].getPrimaryColor());
        setPlayerBalance(player); // Updates the gui balance of the player
    }


    /**
     * Displays an input field to put in integers
     * @param msg - String message before the input field
     * @return integer that was input
     */
    public int displayEnterInteger(String msg) {
        return gui.getUserInteger(msg);
    }

}
