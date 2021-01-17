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
         * adding ChanceFields
         */
        input = getNames("Chance/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldChance(input[n]);
        }
        /**
         * adding PropertiesFields
         */
        input = getNames("FieldProperties/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldProperty(input[n]);
        }
        /**
         * adding JailFields
         */
        input = getNames("Jail/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldJail(input[n],lang);
        }
        /**
         * ScandlinesFields
         */
        input = getNames("Scandlines/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldScandlines(input[n]);
        }
        /**
         * adding SpecialFields
         */
        input = getNames("Special/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldSpecial(input[n]);
        }

        /**
         * Adding Soda fields
         */
        input = getNames("FieldSoda/");
        addFields();
        for(int n = 0; n< input.length;n++){
            allFields[i++] = new FieldSoda(input[n]);
        }

        /**
         * Sorts all the fields by position on the board
         * @return Field[] array
         */
        Field[] unsortedFields;
        unsortedFields = allFields.clone();


        for(int k = 0; k< unsortedFields.length;k++){
            allFields[unsortedFields[k].position] = unsortedFields[k];
        }

    }
    public Field[] getAllFields(){
        return allFields;
    }


    /**
     * Scans a folder for all files
     * @param folderName the folder you want to scan
     * @return a String[] array with all the filenames
     */
    private String[] getNames(String folderName) {

        File allFields = new File("./Field/" + folderName);
        input = allFields.list();

        return input;
    }
    /**
     * Expands current fieldarray allfields
     * @return expanded Field-Array
     */
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
