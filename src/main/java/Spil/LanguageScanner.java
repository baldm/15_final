package Spil;

import java.io.File;

public class LanguageScanner {
    String[] fileNames;
    String[] LanguageNames;

    public LanguageScanner() {
        File allFields = new File("./Languages");

        fileNames = allFields.list();
        LanguageNames = new String[fileNames.length];
        for(int i = 0; i < fileNames.length; i++){
            LanguageNames[i] = fileNames[i].replace(".properties","");
        }
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public String[] getLanguageNames() {
        return LanguageNames;
    }
}
