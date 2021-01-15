package Spil.Fields;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FieldScandlines extends Field{
    private int price;
    private int[] rent;
    private int groupID;
    private int mortageValue;

public FieldScandlines(String fileName){
    try {
        FileInputStream input = new FileInputStream("./Field/Scandlines/"+ fileName);

        Properties prop = new Properties();
        prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));


        name = prop.getProperty("name");
        position = Integer.parseInt(prop.getProperty("pos"));
        fieldType = Integer.parseInt(prop.getProperty("fieldType"));
        fieldID = Integer.parseInt(prop.getProperty("fieldID"));

        price = Integer.parseInt(prop.getProperty("price"));
        String[] rents = prop.getProperty("rent").split(",");

        rent = new int[rents.length];
        for(int i=0;i<rents.length;i++){
            rent[i] = Integer.parseInt(rents[i]);
        }
        groupID = Integer.parseInt(prop.getProperty("groupID"));
        mortageValue = Integer.parseInt(prop.getProperty("mortageValue"));






    } catch (FileNotFoundException e){

    } catch (IOException e){

    }
}

    public int getPrice() {
        return price;
    }

    public int[] getRent() {
        return rent;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getMortageValue() {
        return mortageValue;
    }

}
