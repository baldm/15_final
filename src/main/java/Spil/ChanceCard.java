package Spil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ChanceCard {
    private String cardName;


    public  ChanceCard(String fileName, Language language) {
        try {
            FileInputStream input = new FileInputStream("./ChanceCards/"+ fileName);

            Properties prop = new Properties();
            prop.load(input);

            cardName = prop.getProperty("name");
            cardName = language.getString(cardName);



        } catch (FileNotFoundException e){

        } catch (IOException e){

        }

    }

    public String getCardName() {
        return cardName;
    }
}
