package Gui;

import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Start;
import gui_main.GUI;

public class Interface {
    private GUI gui;
    private GUI_Player[] guiPlayerList;

    public Interface() {
        GUI_Field[] fields = new GUI_Field[]{
                new GUI_Start(),
        };
        gui = new GUI(fields);
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
        gui.getFields()[player.getPosition()].setCar(player.id, true);
    }


}
