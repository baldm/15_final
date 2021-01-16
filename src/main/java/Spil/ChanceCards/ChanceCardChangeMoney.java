package Spil.ChanceCards;

import Spil.Language;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ChanceCardChangeMoney extends ChanceCard{
    private int moneyChange;

    public ChanceCardChangeMoney(String fileName, Language language){
        try {
            FileInputStream input = new FileInputStream("./ChanceCards/ChangeMoney/"+ fileName);
            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));

            cardName = language.getString(prop.getProperty("name"));
            cardID = Integer.parseInt(prop.getProperty("cardID"));
            description = language.getString(prop.getProperty("Description"));
            cardGroup = Integer.parseInt(prop.getProperty("cardGroup"));
            cardCount = Integer.parseInt(prop.getProperty("cardCount"));

            moneyChange = Integer.parseInt(prop.getProperty("MoneyChange"));

        } catch (FileNotFoundException e){

        } catch (IOException e){

        }
    }

    public int getMoneyChange() {
        return moneyChange;
    }
}
