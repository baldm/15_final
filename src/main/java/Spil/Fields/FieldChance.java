package Spil.Fields;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FieldChance extends Field{
    /**
     * Loads a .property file in folder "./Field/Chance/"
     * and creates a Field of childclass FieldChance
     */
    public FieldChance(String fileName){
        try{
            FileInputStream input = new FileInputStream("./Field/Chance/"+ fileName);

            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));


            name = prop.getProperty("name");
            position = Integer.parseInt(prop.getProperty("pos"));
            fieldType = Integer.parseInt(prop.getProperty("fieldType"));
            fieldID = Integer.parseInt(prop.getProperty("fieldID"));

        } catch (FileNotFoundException e){

        } catch (IOException e){

        }
    }

}
