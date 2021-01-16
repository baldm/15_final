package Spil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Language {
    private Properties prop = new Properties();

    public Language(String fileName) {
        try {
            FileInputStream stream = new FileInputStream("./Languages/" + fileName);
            prop.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public String getString(String input) {

        return prop.getProperty(input);

    }
}