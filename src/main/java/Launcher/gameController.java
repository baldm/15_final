package Launcher;


import Gui.Interface;
import Spil.*;
import Spil.ChanceCardFactory;
import Spil.Fields.Field;
import Spil.ChanceCards.*;
import Spil.Fields.FieldProperty;
import Spil.Fields.FieldScandlines;
import Spil.Fields.FieldSoda;

import java.util.concurrent.ThreadLocalRandom;

public class gameController {
    private static Player[] playerArray;
    private Language lang;
    private Interface gameInterface;
    private RealEstateAgent estateAgent;
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

        // Creates real estate agent
        estateAgent = new RealEstateAgent(fieldArray);


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



        int currentFieldType = fieldArray[currentPlayer.getPosition()].fieldType;
        // Buying field logic

        if (currentFieldType == 1 || currentFieldType == 2 || currentFieldType == 3)
        {
            buyableField(currentPlayer);
        }

        // Tax field logic
        if (currentFieldType == 4) {
            taxField(currentPlayer);
        }

        // Chance card logic
        if (currentFieldType == 5)  // Checks if its a chance field
        {
            drawChanceCard(currentPlayer);
        }

        endOfTurn(currentPlayer);


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

    /**
     * Handles the logic for a player in jail.
     * @param player - Player object
     */
    private void jailTurn(Player player) {
        gameInterface.displayMessage(lang.getString("LandedInJail"));

        // Checks if the player has enough funds to pay
        if (player.getMoney() >= 1000) {
            // Asks if they want to pay
            String choice = gameInterface.displayMultiButton(lang.getString("JailQuestion"), lang.getString("JailPay"),lang.getString("JailNo")); // TODO: Bug here if not translated correctly

            // Handles pay to exit
            if (choice.contains("1000 kr")) {
                player.addMoney(-1000);
                gameInterface.setPlayerBalance(player);
                player.setInJail(false);
                player.hasBeenInJail = 0;
                int sumRolls = diceRoll();
                movePlayer(player, sumRolls + player.getPosition());
                return; // Stops method
            }
        }

        // Handles if player does not pay
        gameInterface.displayMessage(lang.getString("Jail2Equal"));
        int sumRolls = diceRoll();

        // Checks if the player has been in jail for 3 turns
        if (player.hasBeenInJail >= 2) {
            gameInterface.displayMessage(lang.getString("Jail3Rounds"));
        }
        // Checks if they rolled the same value
        if (diceOne.getValue() == diceTwo.getValue()) {
            gameInterface.displayMessage(lang.getString("Jail3RoundsDone"));
            player.hasBeenInJail = 0;
            player.setInJail(false);
            movePlayer(player, sumRolls + player.getPosition());

            // Handles if player has been in jail to long
        } else if (player.hasBeenInJail >= 2) {
            gameInterface.displayMessage(lang.getString("JailPayedToExit"));
            player.hasBeenInJail = 0;
            player.setInJail(false);
            player.addMoney(-1000);
            gameInterface.setPlayerBalance(player);
            movePlayer(player, sumRolls + player.getPosition());
        } else {

            // Handles staying in jail
            gameInterface.displayMessage(lang.getString("JailStay"));
            player.hasBeenInJail++;
        }
    }

    /**
     * Full logic for if the player lands on a buyable field
     * @param player - player object which landed on the field
     */
    private void buyableField(Player player) {

        Field currentField = fieldArray[player.getPosition()];
        Player owner = estateAgent.checkOwner(currentField);

        // Checks if the field is not owned
        if (owner == null) {
            gameInterface.displayMessage(lang.getString("LandedOnBuyableProperty"));
            String choice = gameInterface.displayMultiButton(lang.getString("WantToBuy"), lang.getString("Yes"), lang.getString("No"));

            if (choice.equals("Yes") || choice.equals("Ja")) {

                // Casts the correct child class
                switch (currentField.fieldType) {
                    case 1 -> player.addMoney(-((FieldProperty) currentField).getPrice());
                    case 2 -> player.addMoney(-((FieldScandlines) currentField).getPrice());
                    case 3 -> player.addMoney(-((FieldSoda) currentField).getPrice());
                }

                // Sets owner of the field
                estateAgent.setOwner(player, currentField);
                gameInterface.setOwner(player, player.getPosition());
            }

        } else if (owner == player) {
            // Landed on your own field, do nothing
        } else {

            // Handles if the player doesnt own the field.
            gameInterface.displayMessage(lang.getString("LandedOnBoughtProperty"));
            int rent = 0;
            switch (currentField.fieldType) {
                case 1:
                    // Creates the rent from amount of houses
                    int amountHouses = ((FieldProperty) currentField).getHouseNumber();
                    rent = ((FieldProperty) currentField).getRent()[amountHouses];
                    break;
                case 2:
                    int amountFerries = estateAgent.ferriesOwned(owner);
                    rent = ((FieldScandlines) currentField).getRent()[amountFerries];
                    break;
                case 3:
                    // Checks if all sodas are owned and finds the rent
                    if (estateAgent.isAllOwned(currentField)) {
                        gameInterface.displayMessage(lang.getString("SodaAllBought"));
                        rent = (diceOne.getValue() + diceTwo.getValue())* 200;
                    } else {
                        gameInterface.displayMessage(lang.getString("SodaOneBought"));
                        rent = (diceOne.getValue() + diceTwo.getValue())* 100;
                    }
            }

            // Pays the rent and updates the game.
            player.addMoney(- rent);
            owner.addMoney(rent);
            gameInterface.setPlayerBalance(player);
            gameInterface.setPlayerBalance(owner);
            gameInterface.displayMessage(lang.getString("PayedRent")+rent);
        }
    }

    private void taxField(Player player) {
        Field currentField = fieldArray[player.getPosition()];

        // Switch between the type of tax field
        switch (currentField.name) {

            case "Indkomstskat":
                gameInterface.displayMessage(lang.getString("IndkomstskatDescription"));

                // Dialog for the player choice
                String choice = gameInterface.displayMultiButton(lang.getString("IndkomstskatChoice"), "10%", "4.000 kr");
                if (choice.equals("10%")) {

                    // Taxes the player 10 %
                    int tenprocent = (int) (((player.getMoney()/100)*0.1));
                    tenprocent = tenprocent *100;
                    player.addMoney(-tenprocent);
                    gameInterface.setPlayerBalance(player);
                } else {

                    // Taxes the player 4000
                    player.addMoney(-4000);
                    gameInterface.setPlayerBalance(player);
                }
                break;

            case "Statsskat":
                // Taxes the player 2000
                gameInterface.displayMessage(lang.getString("StatsskatDescription"));
                player.addMoney(-2000);
                gameInterface.setPlayerBalance(player);
                break;
            default:
                break;
        }

    }

    private void endOfTurn(Player player) {
        // Check if player is in the negatives of the monies
        // prompt mortage

        // prompt player if they wish to do anything to their plots or whatever
            // sell
            // buy houses
        String[] turnChoices = {"Buy Houses", "Sell houses", "Mortgage houses", "Skip"}; // TODO: Translate
        String turnChoice = gameInterface.displayDropdown(lang.getString("TurnChoice"));

        if (turnChoice.equals(turnChoices[0])) {
            buyHouses(player);
        }


    }

    // TODO: Forsæt
    private void buyHouses(Player player) {
        Field[] ownedArray = estateAgent.getOwnedFields(player);
        String[] ownedArrayString = new String[ownedArray.length];

        for (int i = 0; i < ownedArray.length; i++) {
            if (estateAgent.isAllOwned(ownedArray[i])) {
                ownedArrayString[i] = ownedArray[i].name;
            }
        }

        gameInterface.displayDropdown(lang.getString("ChooseOwned"), ownedArrayString);

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
