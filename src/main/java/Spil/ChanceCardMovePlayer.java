package Spil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ChanceCardMovePlayer extends ChanceCard {
    private int movePlayer;

    public ChanceCardMovePlayer(String fileName, Language language) {
        try {
            FileInputStream input = new FileInputStream("./ChanceCards/MovePlayer/" + fileName);

            Properties prop = new Properties();
            prop.load(input);

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
