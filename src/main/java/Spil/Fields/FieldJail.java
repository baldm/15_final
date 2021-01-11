package Spil.Fields;

import Spil.Language;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FieldJail extends Field{
        private boolean isVisit;
        private String Description;

    public FieldJail(String fileName, Language lang){

        try{
            FileInputStream input = new FileInputStream("./Field/Jail/"+ fileName);

            Properties prop = new Properties();
            prop.load(input);


            name = prop.getProperty("name");
            position = Integer.parseInt(prop.getProperty("pos"));
            fieldType = Integer.parseInt(prop.getProperty("fieldType"));
            fieldID = Integer.parseInt(prop.getProperty("fieldID"));

            isVisit = Boolean.parseBoolean(prop.getProperty("isVisit"));

            Description = lang.getString(prop.getProperty("JailvisitDescription"));

        } catch (FileNotFoundException e){

        } catch (IOException e){

        }


    }


}
