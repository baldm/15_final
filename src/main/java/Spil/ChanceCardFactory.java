package Spil;
import Spil.ChanceCards.*;

import Spil.ChanceCards.ChanceCardChangeMoney;

import java.io.File;

public class ChanceCardFactory {
private ChanceCard[] allCards;

    public ChanceCardFactory(Language lang){
        String[] input;
        /**
         * ChangeMoney ChanceCards
         */
        input = getNames("ChangeMoney/");

        ChanceCard[] ChangeMoney = new ChanceCard[input.length];
        for(int i = 0; i< input.length;i++){
            ChangeMoney[i] = new ChanceCardChangeMoney(input[i],lang);
        }


        /**
         * ChanceCards Move Player
         */

        input = getNames("MovePlayer/");

        ChanceCard[] ChanceMove = new ChanceCard[input.length];
        for(int i = 0; i< input.length;i++){
            ChanceMove[i] = new ChanceCardMovePlayer(input[i],lang);
        }

        /**
         * ChanceCards MovePlayerTo
         */

        input = getNames("MovePlayerTo/");


        ChanceCard[] ChanceMoveTo = new ChanceCard[input.length];

        for(int i = 0; i< input.length;i++){
            ChanceMove[i] = new ChanceCardMovePlayerTo(input[i],lang);
        }

        allCards = new ChanceCard[ChangeMoney.length+ChanceMoveTo.length+ChanceMove.length];


        int k=0;
        for(int i =0;i< allCards.length;i++){

            if(i < ChangeMoney.length){
                allCards[i] = ChangeMoney[k++];
                if(i == ChangeMoney.length-1){
                    k=0;
                }
            } else if(i < ChangeMoney.length+ChanceMove.length){
            allCards[i] = ChanceMove[k++];
                if(i == ChangeMoney.length+ChanceMove.length-1){
                    k=0;
                }
            } else if(i < ChangeMoney.length+ChanceMove.length+ChanceMoveTo.length){
                allCards[i] = ChanceMoveTo[k];
                if(i== ChangeMoney.length+ChanceMove.length+ChanceMoveTo.length-1){
                    k=0;
                }
            }
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

}

