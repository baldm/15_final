package Spil.ChanceCards;

import Spil.Language;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ChanceCardMovePlayer extends ChanceCard {
    private int movePlayer;
    /**
     * Loads a .property file in folder "./ChanceCards/MovePlayer/"
     * and creates a ChanceCard of childclass ChanceCardMovePlayer
     */

    public ChanceCardMovePlayer(String fileName, Language language) {
        try {
            FileInputStream input = new FileInputStream("./ChanceCards/MovePlayer/" + fileName);

            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));

            cardName = language.getString("MoveField");
            cardID = Integer.parseInt(prop.getProperty("cardID"));
            description = language.getString(prop.getProperty("Description"));
            cardGroup = Integer.parseInt(prop.getProperty("cardGroup"));
            cardCount = Integer.parseInt(prop.getProperty("cardCount"));

            movePlayer = Integer.parseInt(prop.getProperty("movePlayer"));


        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }

    public int getMovePlayer() {
        return movePlayer;
    }
}
