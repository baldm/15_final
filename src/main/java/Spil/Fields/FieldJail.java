package Spil.Fields;

import Spil.Language;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FieldJail extends Field{
        private boolean isVisit;
        private String Description;

    public FieldJail(String fileName, Language lang){

        try{
            FileInputStream input = new FileInputStream("./Field/Jail/"+ fileName);

            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));


            name = prop.getProperty("name");
            position = Integer.parseInt(prop.getProperty("pos"));
            fieldType = Integer.parseInt(prop.getProperty("fieldType"));
            fieldID = Integer.parseInt(prop.getProperty("fieldID"));

            isVisit = Boolean.parseBoolean(prop.getProperty("isVisit"));

            Description = lang.getString(prop.getProperty("description"));

        } catch (FileNotFoundException e){

        } catch (IOException e){

        }


    }


}
