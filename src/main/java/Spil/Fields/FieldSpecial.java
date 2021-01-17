package Spil.Fields;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FieldSpecial extends Field {
    private boolean isStart;
    private boolean isParking;
    private  boolean isTaxes;
    /**
     * Loads a .property file in folder "./Field/Special/"
     * and creates a Field of childclass FieldSpecial
     */
    public FieldSpecial(String fileName){
        try{
            FileInputStream input = new FileInputStream("./Field/Special/"+ fileName);

            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));


            name = prop.getProperty("name");
            position = Integer.parseInt(prop.getProperty("pos"));
            fieldType = Integer.parseInt(prop.getProperty("fieldType"));
            fieldID = Integer.parseInt(prop.getProperty("fieldID"));

            isStart = Boolean.parseBoolean(prop.getProperty("isStart"));
            isParking = Boolean.parseBoolean(prop.getProperty("isParking"));
            isTaxes = Boolean.parseBoolean(prop.getProperty("isTaxes"));


        } catch (FileNotFoundException e){

        } catch (IOException e){

        }

    }


}
