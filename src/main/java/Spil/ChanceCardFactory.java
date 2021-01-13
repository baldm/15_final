package Spil;
import Spil.ChanceCards.*;

import Spil.ChanceCards.ChanceCardChangeMoney;
import Spil.Fields.Field;
import Spil.Fields.FieldChance;

import java.io.File;

public class ChanceCardFactory {
private ChanceCard[] allCards = new ChanceCard[0];
    private String[] input;
    private ChanceCard[] placeholder;
    private int i = 0;

    public ChanceCardFactory(Language lang){
        /**
         * ChangeMoney
         */
        input = getNames("ChangeMoney/");
        addCards();
        for(int n = 0; n< input.length;n++){
            allCards[i++] = new ChanceCardChangeMoney(input[n],lang);
        }
        /**
         * MovePlayer
         */
        input = getNames("MovePlayer/");
        addCards();
        for(int n = 0; n< input.length;n++){
            allCards[i++] = new ChanceCardMovePlayer(input[n],lang);
        }
        /**
         * MovePlayerTo
         */
        input = getNames("MovePlayerTo/");
        addCards();
        for(int n = 0; n< input.length;n++){
            allCards[i++] = new ChanceCardMovePlayerTo(input[n],lang);
        }

    }
    public ChanceCard[] getAllCards(){
        return allCards;
    }
    private String[] getNames(String folderName){

        File allFields = new File("./ChanceCards/" + folderName);
        String[] input = allFields.list();

        return  input;



    }

    private ChanceCard[] addCards(){

        placeholder = new ChanceCard[allCards.length];
        placeholder = allCards.clone();
        allCards = new ChanceCard[allCards.length + input.length];

        for (int n = 0; n< placeholder.length;n++){
            allCards[n]=placeholder[n];
        }

        return allCards;
    }

}

