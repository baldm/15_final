package Spil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ChanceCardMovePlayerTo extends ChanceCard {

        private int moveTo;
    public ChanceCardMovePlayerTo(String fileName, Language language){
        try {
            FileInputStream input = new FileInputStream("./ChanceCards/MovePlayerTo/"+ fileName);

            Properties prop = new Properties();
            prop.load(input);

            cardName = language.getString("MoveField");
            cardID = Integer.parseInt(prop.getProperty("cardID"));
            description = language.getString(prop.getProperty("Description"));
            cardGroup = Integer.parseInt(prop.getProperty("cardGroup"));
            cardCount = Integer.parseInt(prop.getProperty("cardCount"));

            moveTo = Integer.parseInt(prop.getProperty("moveTo"));





        } catch (FileNotFoundException e){

        } catch (IOException e){

        }

    }

    public int getMoveTo() {
        return moveTo;
    }
}
