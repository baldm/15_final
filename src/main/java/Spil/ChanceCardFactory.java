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
        getNames("ChangeMoney/");
        addCards();
        for(int n = 0; n< input.length;n++){
            allCards[i++] = new ChanceCardChangeMoney(input[n],lang);
        }

        /**
         * MovePlayer
         */
        getNames("MovePlayer/");
        addCards();
        for(int n = 0; n< input.length;n++){
            allCards[i++] = new ChanceCardMovePlayer(input[n],lang);
        }
        /**
         * MovePlayerTo
         */
        getNames("MovePlayerTo/");
        addCards();
        for(int n = 0; n< input.length;n++){
            allCards[i++] = new ChanceCardMovePlayerTo(input[n],lang);
        }

        /**
         * Checking if multiple cards of each is needed
         * Then implementing them in allCards
         */
        int length = allCards.length;
        for(int n = 0; n<length; n++){
            if(allCards[n].cardCount > 1){
                for(int k=0;k<allCards[n].cardCount-1;k++){
                    input = new String[1];
                    input[0] = allCards[n].cardName;
                    addCards();
                    allCards[i++] = allCards[n];

                }
            }
        }

    }
    public ChanceCard[] getAllCards(){
        return allCards;
    }
    private void getNames(String folderName){

        File allFields = new File("./ChanceCards/" + folderName);
        input = allFields.list();


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

