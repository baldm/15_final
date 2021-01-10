package Spil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FieldProperties extends Field {
    private int price;
    private int[] rent;
    private int groupID;
    private int mortageValue;
    private int houseNumber =   0;
    private int hotelNumber =   0;

    public FieldProperties(String fileName, Language language){
        try {
            FileInputStream input = new FileInputStream("./Field/FieldProperties/"+ fileName);

            Properties prop = new Properties();
            prop.load(input);


            name = prop.getProperty("name");
            position = Integer.parseInt(prop.getProperty("pos"));
            fieldType = Integer.parseInt(prop.getProperty("fieldType"));
            fieldID = Integer.parseInt(prop.getProperty("fieldID"));

            String[] rents = prop.getProperty("rent").split(",");

            rent = new int[rents.length];
            for(int i=0;i<rents.length;i++){
                rent[i] = Integer.parseInt(rents[i]);
            }






        } catch (FileNotFoundException e){

        } catch (IOException e){

        }


    }



}
