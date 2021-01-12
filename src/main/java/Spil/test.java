package Spil;
import Spil.Fields.*;

public class test {
    public static void main(String[] args) {
        Language lang = new Language("Danish.properties");
        FieldFactory test = new FieldFactory(lang);

        Field[] testfields = test.getAllFields();
        for(int i = 0; i< testfields.length;i++){
            System.out.println(testfields[i].name);
        }


    }
}
