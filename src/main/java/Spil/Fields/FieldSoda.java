package Spil.Fields;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FieldSoda extends Field {
    private int price;
    private int[] rentMultiplier;
    private int groupID;
    private int mortageValue;

    public FieldSoda(String fileName){
        try {
            FileInputStream input = new FileInputStream("./Field/FieldSoda/"+ fileName);

            Properties prop = new Properties();
            prop.load(input);


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

            mortageValue = price;





        } catch (FileNotFoundException e){

        } catch (IOException e){

        }
    }
}
