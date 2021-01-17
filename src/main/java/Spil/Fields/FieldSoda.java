package Spil.Fields;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FieldSoda extends Field {
    private int price;
    private int[] rentMultiplier;
    private int groupID;
    private int mortageValue;
    private boolean isPledged = false;

    public FieldSoda(String fileName){
        try {
            FileInputStream input = new FileInputStream("./Field/FieldSoda/"+ fileName);

            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));


            name = prop.getProperty("name");
            position = Integer.parseInt(prop.getProperty("pos"));
            fieldType = Integer.parseInt(prop.getProperty("fieldType"));
            fieldID = Integer.parseInt(prop.getProperty("fieldID"));

            price = Integer.parseInt(prop.getProperty("price"));
            String[] rents = prop.getProperty("rent").split(",");

            rentMultiplier = new int[rents.length];
            for(int i=0;i<rents.length;i++){
                rentMultiplier[i] = Integer.parseInt(rents[i]);
            }

            mortageValue = Integer.parseInt(prop.getProperty("mortageValue"));





        } catch (FileNotFoundException e){

        } catch (IOException e){

        }
    }

    public int getMortageValue() {
        return mortageValue;
    }

    public int getPrice() {
        return price;
    }

    public int[] getRentMultiplier() {
        return rentMultiplier;
    }
    public void setPledged(boolean pledged) {
        isPledged = pledged;
    }

    public boolean isPledged() {
        return isPledged;
    }
}
