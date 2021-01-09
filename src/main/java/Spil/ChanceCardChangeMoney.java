package Spil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ChanceCardChangeMoney extends ChanceCard{
    private int moneyChange;

    public ChanceCardChangeMoney(String fileName, Language language){
        try {
            FileInputStream input = new FileInputStream("./ChanceCards/ChangeMoney/"+ fileName);

            Properties prop = new Properties();
            prop.load(input);

            cardName = language.getString(prop.getProperty("name"));
            System.out.println("cardname " + cardName);
            moneyChange = Integer.parseInt(prop.getProperty("MoneyChange"));
            System.out.println(moneyChange);
            cardID = Integer.parseInt(prop.getProperty("cardID"));
            System.out.println(cardID);
            description = language.getString(prop.getProperty("Description"));
            System.out.println(description);
            cardGroup = Integer.parseInt(prop.getProperty("cardGroup"));
            System.out.println(cardGroup);



        } catch (FileNotFoundException e){

        } catch (IOException e){

        }
    }

    public int getMoneyChange() {
        return moneyChange;
    }
}
