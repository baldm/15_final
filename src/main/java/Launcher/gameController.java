package Launcher;


import Gui.Interface;
import Spil.*;
import Spil.ChanceCardFactory;
import Spil.Fields.Field;
import Spil.ChanceCards.*;

import java.util.concurrent.ThreadLocalRandom;

public class gameController {
    private static Player[] playerArray;
    private Language lang;
    private Interface gameInterface;
    private Field[] fieldArray;
    private Dice diceOne = new Dice(6);
    private Dice diceTwo = new Dice(6);
    private static boolean extraturn = false;
    private ChanceCard[] allChanceCards;
    private ChanceCard[] drawAbleChanceCards;

    public static void main(String[] args) throws Exception {
        gameController game = new gameController();
        game.gameInit();




        // Simple turn
        while (true) {
            for (Player player : playerArray) {
                player.hasExtraTurn = 0;
                game.takeTurn(player);
                if (player.hasExtraTurn > 0) {
                    game.takeTurn(player);
                }
            }
        }
    }

    // TODO: Write docstring
    private void gameInit() {

        LanguageScanner langScanner = new LanguageScanner();
        String[] languageArray = langScanner.getLanguageNames();

        // Creates a dialog for selecting a language
        gameInterface = new Interface();
        String langChoice = gameInterface.initLanguage(languageArray);
        lang = new Language(langChoice+".properties"); // Sets the language

        // Creates fields
        FieldFactory factory = new FieldFactory(lang);
        fieldArray = factory.getAllFields();

        // Dialog for entering player names
        String[] playerStringArray = gameInterface.initPlayerNames(lang);

        // Creates player objects
        playerArray = new Player[playerStringArray.length];
        for (int i = 0; i < playerStringArray.length; i++) {
            playerArray[i] = new Player(playerStringArray[i], 30000, i);
        }

        // Creates final game interface
        gameInterface.gameInit(fieldArray, lang);

        //Creates ChanceCards
        ChanceCardFactory chanceCardFactory = new ChanceCardFactory(lang);
        allChanceCards = chanceCardFactory.getAllCards();
        drawAbleChanceCards = chanceCardFactory.getAllCards();
    }

    /**
     * Most of the game logic. Handles an entire turn for the player.
     * @param currentPlayer Player object which turn it is
     */
    private void takeTurn(Player currentPlayer) {
        gameInterface.displayMessage(lang.getString("PlayersTurn")+" "+currentPlayer.getName());
        if (currentPlayer.isInJail()) {
            // Jail logic here
            jailTurn(currentPlayer);
        } else {
            // Standard turn
            int sumRolls = diceRoll();
            // Moves player
            movePlayer(currentPlayer, sumRolls + currentPlayer.getPosition());
            // Check for extra turn
            if(extraturn && currentPlayer.hasExtraTurn < 2){
                gameInterface.displayMessage(lang.getString("ExtraTurn"));
                currentPlayer.hasExtraTurn += 1;
            }else if(extraturn && currentPlayer.hasExtraTurn == 2){
                // Third time 
                gameInterface.displayMessage(lang.getString("ThirdExtraTurn"));
                movePlayer(currentPlayer, 30);
                currentPlayer.hasExtraTurn = 0;
            }
        }


        // Chance card logic
        if (fieldArray[currentPlayer.getPosition()].fieldType == 5)  // Checks if its a chance field
        {
            drawChanceCard(currentPlayer);
        }

        // Buying field logic
        if (fieldArray[currentPlayer.getPosition()].fieldType == 1)  // Currently only works with properties
        {
            buyableField(currentPlayer);

        }
    }

    private void drawChanceCard(Player player) {
        gameInterface.displayMessage(lang.getString("LandedOnChancecard"));
        ChanceCard drawedCard;
        int drawedCardNumber;

        if (drawAbleChanceCards.length > 1) {
            drawedCardNumber = ThreadLocalRandom.current().nextInt(0, drawAbleChanceCards.length+1);
            drawedCard = drawAbleChanceCards[drawedCardNumber];
            ChanceCard[] chanceCardsPlaceholder = drawAbleChanceCards.clone();
            drawAbleChanceCards = new ChanceCard[drawAbleChanceCards.length - 1];

            for (int i = 0, k = 0; i < chanceCardsPlaceholder.length; i++) {
                if (i != drawedCardNumber) {
                    drawAbleChanceCards[k++] = chanceCardsPlaceholder[i];
                }
            }
        } else {
            drawedCard = drawAbleChanceCards[0];
            drawAbleChanceCards = new ChanceCard[allChanceCards.length];
            drawAbleChanceCards = allChanceCards.clone();
        }

        switch (drawedCard.cardGroup){
            case 2:
                gameInterface.displayChance(drawedCard.description);
                player.addMoney(((ChanceCardChangeMoney) drawedCard).getMoneyChange());
                gameInterface.setPlayerBalance(player);
                break;
            case 3:
                gameInterface.displayChance(drawedCard.description);
                player.setPosition(((ChanceCardMovePlayerTo) drawedCard).getMoveTo());
                gameInterface.movePlayer(player);
                break;
            case 4:
                gameInterface.displayChance(drawedCard.description);
                player.setPosition(player.getPosition() + ((ChanceCardMovePlayer) drawedCard).getMovePlayer());
                gameInterface.movePlayer(player);
                break;
            case 5:
                for(int n=0;n< playerArray.length;n++){
                    playerArray[n].addMoney(((ChanceCardChangeMoney) drawedCard).getMoneyChange());
                    player.addMoney(((ChanceCardChangeMoney) drawedCard).getMoneyChange());
                }
                break;
            default:
                throw new RuntimeException("Error in ChanceCard reader");
        }
    }

    private void jailTurn(Player player) {
        gameInterface.displayMessage(lang.getString("LandedInJail"));

        if (player.getMoney() >= 1000) {
            String choice = gameInterface.displayMultiButton(lang.getString("JailQuestion"), lang.getString("JailPay"),lang.getString("JailNo")); // TODO: Bug here if not translated correctly
            if (choice.contains("1000 kr")) {
                player.addMoney(-1000);
                gameInterface.setPlayerBalance(player);
                player.setInJail(false);
                player.hasBeenInJail = 0;
                int sumRolls = diceRoll();
                movePlayer(player, sumRolls + player.getPosition());


            } else {
                gameInterface.displayMessage(lang.getString("Jail2Equal"));
                int sumRolls = diceRoll();
                if (player.hasBeenInJail >= 2) {
                    gameInterface.displayMessage(lang.getString("Jail3Rounds"));
                }
                if (diceOne.getValue() == diceTwo.getValue()) {
                    gameInterface.displayMessage(lang.getString("Jail3RoundsDone"));
                    player.hasBeenInJail = 0;
                    player.setInJail(false);
                    movePlayer(player, sumRolls + player.getPosition());

                } else if (player.hasBeenInJail >= 2) {
                    gameInterface.displayMessage(lang.getString("JailPayedToExit"));
                    player.hasBeenInJail = 0;
                    player.setInJail(false);
                    player.addMoney(-1000);
                    gameInterface.setPlayerBalance(player);
                    movePlayer(player, sumRolls + player.getPosition());
                } else {
                    gameInterface.displayMessage(lang.getString("JailStay"));
                    player.hasBeenInJail++;
                }
            }
        }
    }

    private void buyableField(Player player) {

        

        gameInterface.displayMessage(lang.getString("LandedOnBuyableProperty"));

        // LOGIC
        /*
        1. check if field is available else see (4.)

        2. Prompt buy screen

        3. If bought add to players properties else (auction?)

        4. Check owner of field, if self skip else (5.)

        5. Pay rent to owner that is equivalent of the rent determined by houses on field.

         */

    }

    private void endOfTurn(Player player) {
        // pay rent

        // prompt player if they wish to do anything to their plots or whatever

        // prompt mortage

    }

    private void mortgage(Player player) {


        /*
        Man kan kun pansætte ubebyggende grunde

        1. Vil du hæve pantsætning eller pantsætte noget

        if hæve:
                1. Hvilken grund vil du hæve pæntsætningen på
                2. Det koster 10% mere end grunden
                3. har man penge nok?
                4. Overfør penge for at modtage property tilbage

        else:
            1. Sælg huse på grunden til banken
            2. Få penge iforhold til hvad grunden er vær
            3. Mærker grund som pæntsat



         */
    }

    private int diceRoll() {
        gameInterface.displayMessage(lang.getString("RollDice"));

        // Rolls dices
        int sumRolls = diceOne.Roll() + diceTwo.Roll();

        // Check if dice roll the same
        if(diceOne.getValue() == diceTwo.getValue()){
            extraturn = true;
        }else{
            extraturn = false;
        }

        // Displays dices and the result of dices on gui
        gameInterface.setBoardDice(diceOne.getValue(), diceTwo.getValue());
        gameInterface.displayMessage(lang.getString("DiceResult") + " " + diceOne.getValue() + " & " + diceTwo.getValue());
        return sumRolls;
    }

    private void movePlayer(Player player, int toPosition) {
        //gameInterface.removePlayer(player);
        player.setPosition(toPosition);
        gameInterface.movePlayer(player);
    }


}
