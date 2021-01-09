package Spil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ChanceCardMovePlayer extends ChanceCard {
        private boolean moveToSpecificField;
        private int numberOfMoves;
        private int moveTo;
    public ChanceCardMovePlayer(String fileName, Language language){
        try {
            FileInputStream input = new FileInputStream("./ChanceCards/ChangeMoney/"+ fileName);

            Properties prop = new Properties();
            prop.load(input);

            cardName = language.getString(prop.getProperty("name"));
            cardID = Integer.parseInt(prop.getProperty("cardID"));
            description = language.getString(prop.getProperty("Description"));
            cardGroup = Integer.parseInt(prop.getProperty("cardGroup"));

        } catch (FileNotFoundException e){

        } catch (IOException e){

        }

    }
}
