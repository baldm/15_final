package Spil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Language {
    private Properties prop = new Properties();
    private FileInputStream stream;


    public Language(String fileName) {
        try {
            stream = new FileInputStream("./Languages/" + fileName);
            prop.load(stream);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public String getString(String input) {

        return prop.getProperty(input);

    }
}