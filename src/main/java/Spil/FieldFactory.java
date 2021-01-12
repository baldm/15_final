package Spil;

import Spil.ChanceCards.ChanceCardChangeMoney;
import Spil.Fields.*;

import java.io.File;
import java.util.Arrays;

public class FieldFactory {
    private Field[] allFields = new Field[0];
    private String[] input;
    private Field[] placeholder;
    private int i = 0;

    public FieldFactory(Language lang){

        /**
         * ChanceFields
         */
        input = getNames("Chance/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldChance(input[n]);
        }
        /**
         * Properties
         */
        input = getNames("FieldProperties/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldProperty(input[n]);
        }
        /**
         * Jail
         */
        input = getNames("Jail/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldJail(input[n],lang);
        }
        /**
         * Scandlines
         */
        input = getNames("Scandlines/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldScandlines(input[n]);
        }
        /**
         * Special
         */
        input = getNames("Special/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldSpecial(input[n]);
        }
        /**
         * Soda
         */
        input = getNames("FieldSoda/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldSoda(input[n]);
        }
        placeholder = new Field[allFields.length];
        placeholder = allFields;

        for(int n = 0; n< placeholder.length;n++){
            allFields[placeholder[n].position] = placeholder[n];
        }




    }
    public Field[] getAllFields(){
        return allFields;
    }



    private String[] getNames(String folderName) {

        File allFields = new File("./Field/" + folderName);
        String[] input = allFields.list();

        return input;
    }

    private Field[] addFields(){

            placeholder = new Field[allFields.length];
            placeholder = allFields;
            allFields = new Field[allFields.length + input.length];

            for (int n = 0; n< placeholder.length;n++){
                allFields[n]=placeholder[n];
            }

        return allFields;
    }
}
