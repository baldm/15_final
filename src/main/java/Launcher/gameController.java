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

    public static void main(String[] args) {
        gameController game = new gameController();
        game.gameInit();


        // Simple turn
        while (playerArray.length > 1) {
            for (Player player : playerArray) {
                player.hasExtraTurn = 0;

                game.takeTurn(player);
                while (extraturn) {
                    game.takeTurn(player);
                }
                game.endOfTurn(player);
            }
        }
        game.gameIsOver(playerArray[0]);
    }

    /**
     * Initilizes the Game
     */
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
            } else if(extraturn && currentPlayer.hasExtraTurn == 2){
                // Third time
                gameInterface.displayMessage(lang.getString("ThirdExtraTurn"));
                movePlayer(currentPlayer, 30);
                currentPlayer.hasExtraTurn = 0;
            }
        }

        int currentFieldType = fieldArray[currentPlayer.getPosition()].fieldType;

        // ORDER OF THESE ARE IMPORTANT,
        // if order is changed some chance card logic will not work

        // Chance card logic
        if (currentFieldType == 5)  // Checks if its a chance field
        {
            drawChanceCard(currentPlayer);
        }

        // Update again if chancecard changed players position
        currentFieldType = fieldArray[currentPlayer.getPosition()].fieldType;

        // Buying field logic
        if (currentFieldType == 1 || currentFieldType == 2 || currentFieldType == 3)
        {
            buyableField(currentPlayer);
        }

        // Tax field logic
        if (currentFieldType == 4) {
            taxField(currentPlayer);
        }

    }

    private void drawChanceCard(Player player) {
        gameInterface.displayMessage(lang.getString("LandedOnChancecard"));
        ChanceCard drawedCard;
        int drawedCardNumber;

        if (drawAbleChanceCards.length > 1) {
            drawedCardNumber = ThreadLocalRandom.current().nextInt(0, drawAbleChanceCards.length);
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
            buyField(player, currentField);

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

    private void buyField(Player player, Field currentField) {

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
            gameInterface.setOwner(player, currentField.position);
        }
    }

    /**
     * Full logic that handles when a
     * @param player
     */
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
                    int tenpercent = (int) (((player.getMoney()/100)*0.1));
                    tenpercent = tenpercent *100;
                    player.addMoney(-tenpercent);
                } else {

                    // Taxes the player 4000
                    player.addMoney(-4000);
                }
                gameInterface.setPlayerBalance(player);
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

        while (true) {
            String[] turnChoices = {lang.getString("BuyHouses"), lang.getString("SellHouses"), lang.getString("SellField"), lang.getString("MortgageHouses"), lang.getString("UnPledge"), lang.getString("Skip")};
            String turnChoice = gameInterface.displayDropdown(lang.getString("TurnChoice"), turnChoices);

            if (turnChoice.equals(turnChoices[0])) {
                buyHouses(player);
            } else if (turnChoice.equals(turnChoices[1])) {
                sellHouses(player);
            }else if (turnChoice.equals(turnChoices[2])) {
                sellField(player);
            } else if (turnChoice.equals(turnChoices[3])) {
                pledgeField(player);
            } else if (turnChoice.equals(turnChoices[4])) {
                unPledgeField(player);
            } else  {
                break;
            }
        }

        // handles loss
        if (hasPlayerLost(player)) {
            removePlayerFromGame(player);
        }

    }


    private void buyHouses(Player player) {
        Field[] ownedArray = estateAgent.getOwnedFields(player);

        if (ownedArray.length == 0) {
            gameInterface.displayMessage(lang.getString("NoOwnedFields"));
            return;
        }

        int ownedInGroups = 0;

        for (Field field : ownedArray) {
            if (estateAgent.isAllOwned(field) && field.fieldType == 1) {
                ownedInGroups++;
            }
        }

        String[] ownedArrayString = new String[ownedInGroups];

        for (int i = 0, k = 0; i < ownedArray.length; i++) {
            if (ownedArray[i] != null) {
                if (ownedArray[i].fieldType == 1) {
                    if (estateAgent.isAllOwned(ownedArray[i])) {
                        ownedArrayString[k++] = ownedArray[i].name;
                    }
                }
            }
        }
        if (ownedArrayString[0] == null) {
            gameInterface.displayMessage(lang.getString("DoesntOwnFieldGroup"));
            return;
        }


        Field buyField = fieldFinder(gameInterface.displayDropdown(lang.getString("ChooseOwned"), ownedArrayString));
        int houseAmount = ((FieldProperty) buyField).getHouseNumber();

        gameInterface.displayMessage(lang.getString("BuyHousesDescription"));
        String[] houseChoices = {"1","2","3","4","Hotel"};
        String chosenHouseAmountString = gameInterface.displayDropdown(lang.getString("BuyAmountHouses"), houseChoices);

        int currentHouseValue = houseAmount * ((FieldProperty) buyField).getHousePrice();
        int newHouseValue;
        int deltaHouseValue;

        if (chosenHouseAmountString.equals("Hotel")) {
            newHouseValue = ((FieldProperty) buyField).getHousePrice() * 5;
            deltaHouseValue = newHouseValue - currentHouseValue;


            if (deltaHouseValue < player.getMoney()) {
                gameInterface.displayMessage(lang.getString("InsufficientFunds"));

            } else {
                player.addMoney(-deltaHouseValue);
                ((FieldProperty) buyField).setHouseNumber(5); // TODO: Check med jens om det her er rigtigt

                gameInterface.setFieldHouses(buyField.position, 0,player);
                gameInterface.setFieldHotel(buyField.position, true, player);
                gameInterface.setPlayerBalance(player);
            }

        } else {
            int chosenHouseAmount = Integer.parseInt(chosenHouseAmountString);
            newHouseValue = ((FieldProperty) buyField).getHousePrice() * chosenHouseAmount;
            deltaHouseValue = newHouseValue - currentHouseValue;


            if (deltaHouseValue > player.getMoney()) {
                gameInterface.displayMessage(lang.getString("InsufficientFunds"));
            } else {
                player.addMoney(-deltaHouseValue);
                ((FieldProperty) buyField).setHouseNumber(chosenHouseAmount);

                gameInterface.setFieldHouses(buyField.position, chosenHouseAmount,player);
                gameInterface.setPlayerBalance(player);
            }
        }
    }


    private void sellHouses(Player player) {
        Field[] ownedArray = estateAgent.getOwnedFields(player);

        if (ownedArray.length == 0) {
            gameInterface.displayMessage(lang.getString("NoOwnedFields"));
            return;
        }
        String[] ownedArrayString = new String[ownedArray.length];

        for (int i = 0; i < ownedArray.length; i++) {
            if (ownedArray[i] != null) {
                if (ownedArray[i].fieldType == 1) {
                    if (((FieldProperty) ownedArray[i]).getHouseNumber() > 0) {
                        ownedArrayString[i] = ownedArray[i].name;
                    }
                }
            }
        }

        if (ownedArrayString[0] == null) {
            gameInterface.displayMessage(lang.getString("NoHouses"));
            return;
        }

        // Select a field and find it
        Field sellField = fieldFinder(gameInterface.displayDropdown(lang.getString("ChooseOwned"), ownedArrayString));
        int houseAmount = ((FieldProperty) sellField).getHouseNumber();

        // Select amount of houses to sell:
        String[] houseAmountChoices = new String[houseAmount];
        for (int i = 1; i <= houseAmount; i++) {
            houseAmountChoices[i] = String.valueOf(i);
        }

        int chosenHouseAmount = Integer.parseInt(gameInterface.displayDropdown(lang.getString("SellAmountHouses"), houseAmountChoices));

        // Sells all houses on field
        int housePrice = ((FieldProperty) sellField).getHousePrice();

        int totalPrice = housePrice*chosenHouseAmount;
        player.addMoney(totalPrice);

        // Updates the board
        gameInterface.setPlayerBalance(player);

        // Removes the houses
        ((FieldProperty) sellField).setHouseNumber(chosenHouseAmount);
        gameInterface.setFieldHouses(sellField.position, (((FieldProperty) sellField).getHouseNumber()-chosenHouseAmount), player);
        gameInterface.setFieldHotel(sellField.position, false,player);


    }

    
    private void sellField(Player player) {

        Field[] ownedArray = estateAgent.getOwnedFields(player);

        if (ownedArray.length == 0) {
            gameInterface.displayMessage(lang.getString("NoOwnedFields"));
            return;
        }

        String[] ownedArrayString = new String[ownedArray.length];

        for (int i = 0; i < ownedArray.length; i++) {
            if (ownedArray[i] != null) {
                ownedArrayString[i] = ownedArray[i].name;
            }
        }

        // Select a field and find it
        Field sellField = fieldFinder(gameInterface.displayDropdown(lang.getString("ChooseOwned"), ownedArrayString));
        int houseAmount = 0;
        if (sellField.fieldType == 1) {
            houseAmount = ((FieldProperty) sellField).getHouseNumber();
        }

        if (houseAmount > 0) {
            gameInterface.displayMessage(lang.getString("TooManyHouses"));
            return;
        }


        String[] playerNamesStringArray = new String[playerArray.length];

        for (int i = 0; i < playerArray.length; i++) {
            playerNamesStringArray[i] = playerArray[i].getName();
        }

        String playerChoice = gameInterface.displayDropdown(lang.getString("PickPlayer"), playerNamesStringArray);

        Player buyer = null;

        for (Player loopPlayer : playerArray) {
            if (loopPlayer.getName().equals(playerChoice)) {
                buyer = loopPlayer;
            }
        }

        int priceChoice = gameInterface.displayEnterInteger(lang.getString("PickPrice"));

        player.addMoney(priceChoice);
        buyer.addMoney(-priceChoice);
        gameInterface.setPlayerBalance(player);
        estateAgent.setOwner(buyer, sellField);
        gameInterface.setOwner(buyer, sellField.position);

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

    private Field fieldFinder(String fieldName) {
        for (Field field : fieldArray) {
            if (field.name.equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    private boolean hasPlayerLost(Player player) {
        if (player.getMoney() < 0) {
            while (player.getMoney() < 0) {
                gameInterface.displayMessage(lang.getString("UnderZeroMoney"));
                String[] turnChoices = {lang.getString("SellHouses"), lang.getString("SellField"), lang.getString("MortgageHouses"), lang.getString("Forfeit")};
                String turnChoice = gameInterface.displayDropdown(lang.getString("TurnChoice"), turnChoices);

                if (turnChoice.equals(turnChoices[0])) {
                    sellHouses(player);
                } else if (turnChoice.equals(turnChoices[1])) {
                    sellField(player);
                } else if (turnChoice.equals(turnChoices[2])) {
                    pledgeField(player);
                } else {
                    gameInterface.displayMessage(lang.getString("PlayerLeftGame"));
                    return true;
                }
            }
        }
        return false;
    }

    private void removePlayerFromGame(Player player) {

        Field[] ownedFields = estateAgent.getOwnedFields(player);

        if (ownedFields != null) {
            for (Field field : ownedFields) {
                if (field.fieldType == 1) {
                    ((FieldProperty) field).setHouseNumber(0);
                    gameInterface.setFieldHouses(field.position, 0, player);
                    gameInterface.setFieldHotel(field.position, false, player);

                }
                gameInterface.removeOwner(player, field.position);
                estateAgent.setOwner(null, field);
            }
        }

        playerArray[player.getID()] = null;


        Player[] newPlayerArray = new Player[playerArray.length-1];


        for (int i = 0, k=0; i < playerArray.length; i++) {
            if (playerArray[i] != null) {
                newPlayerArray[k] = playerArray[i];
                k++;
            }
        }

        gameInterface.removePlayer(player);
        playerArray = newPlayerArray;
    }
    private void gameIsOver(Player player) {
        gameInterface.displayMessage(lang.getString("GameOver"));
        gameInterface.displayChance(lang.getString("Congrats"));
        gameInterface.displayMessage(lang.getString("WinnerIs") + " " + player.getName());
    }
    /**
     * Full logic for a player to pledge an owned property
     * @param player - player object which landed on the field
     */
    private void pledgeField(Player player){

        Field[] ownedFields = estateAgent.getOwnedFields(player);
        String[] ownedArrayString;
        Field[] allOwnedFields = ownedFields.clone();
        int[][] ownedFieldsSorted = estateAgent.getFieldType1Sorted();
        Field[] placeholder;
        if (ownedFields.length == 0) {
            gameInterface.displayMessage(lang.getString("NoOwnedFields"));
            return;
        }
        int h=0;
        ownedFields = new Field[h];
            if(allOwnedFields == null){
                lang.getString("NoOwnedFields");
                return;
            }
            for(int i = 0; i<allOwnedFields.length;i++)
                switch (allOwnedFields[i].fieldType) {
                    case 1:
                        int[] allFieldsInGroup = ownedFieldsSorted[((FieldProperty) allOwnedFields[i]).getGroupID()];
                        boolean anyHouses = false;
                        for(int m =0; m<allFieldsInGroup.length;m++){
                            if(((FieldProperty) fieldArray[allFieldsInGroup[m]]).getHouseNumber() != 0){
                                anyHouses = true;
                            }
                        }
                        if (!((FieldProperty) allOwnedFields[i]).isPledged() && !anyHouses) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[ownedFields.length+1];
                            for (int k = 0; k< placeholder.length;k++){
                                ownedFields[k]=placeholder[k];
                            }
                            ownedFields[++h] = allOwnedFields[i];
                        }
                        break;
                    case 2:
                        if (!((FieldScandlines) allOwnedFields[i]).isPledged()) {

                            placeholder = ownedFields.clone();
                            ownedFields = new Field[ownedFields.length+1];
                            for (int k = 0; k< placeholder.length;k++){
                                ownedFields[k]=placeholder[k];
                            }
                            ownedFields[++h] = allOwnedFields[i];

                        }
                        break;
                    case 3:
                        if (!((FieldSoda) allOwnedFields[i]).isPledged()) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[ownedFields.length+1];
                            for (int k = 0; k< placeholder.length;k++){
                                ownedFields[k]=placeholder[k];
                            }
                            ownedFields[++h] = allOwnedFields[i];
                        }
                        break;
                    default:

                    }

                ownedArrayString = new String[ownedFields.length];

        for(int i = 0; i < ownedFields.length;i++){
            if(ownedFields[i].name != null){
                switch (ownedFields[i].fieldType){
                    case 1:
                        ownedArrayString[i] = ownedFields[i].name + " " +
                                lang.getString("mortage") +  " " +
                                ((FieldProperty) ownedFields[i]).getMortageValue();
                        break;
                    case 2:
                        ownedArrayString[i] = ownedFields[i].name + " " +
                                lang.getString("mortage") +  " " +
                                ((FieldScandlines) ownedFields[i]).getMortageValue();    break;
                    case 3:
                        ownedArrayString[i] = ownedFields[i].name + " " +
                                lang.getString("mortage") +  " " +
                                ((FieldSoda) ownedFields[i]).getMortageValue();
                    break;

                }
            }
        }


        // Select a field and find it
        Field sellField = fieldFinder(gameInterface.displayDropdown(
                lang.getString("ChooseOwned"), ownedArrayString));

            switch (sellField.fieldType){
                case 1:
                    player.addMoney(((FieldProperty) sellField).getMortageValue());
                    ((FieldProperty) sellField).setPledged(true);
                case 2:
                    player.addMoney(((FieldScandlines) sellField).getMortageValue());
                    ((FieldScandlines) sellField).setPledged(true);
                case 3:
                    player.addMoney(((FieldSoda) sellField).getMortageValue());
                    ((FieldSoda) sellField).setPledged(true);
            }


    }

    /**
     * Full logic for a player to unpledge an owned property
     * @param player - player object which landed on the field
     */
    private void unPledgeField(Player player){
        Field[] ownedFields = estateAgent.getPledgedFields(player);
        String[] ArrayString;
        Field[] allOwnedFields = ownedFields.clone();
        Field[] placeholder;
        if(allOwnedFields.length == 0){
            lang.getString("NoFieldsPledged");
            return;
        }
        int h=0;
        ownedFields = new Field[h];
        for(int k = 0; k< allOwnedFields.length;k++){
            switch (allOwnedFields[k].fieldType){
                case 1:
                    if(((FieldProperty) allOwnedFields[k]).isPledged()){
                        placeholder = ownedFields.clone();
                        ownedFields = new Field[++h];
                        for(int m =0; m<placeholder.length;m++){
                            ownedFields[m] = placeholder[m];
                        }
                        ownedFields[h] = allOwnedFields[k];
                    }
                    break;
                case 2:
                    if(((FieldScandlines) allOwnedFields[k]).isPledged()){
                        placeholder = ownedFields.clone();
                        ownedFields = new Field[++h];
                        for(int m =0; m<placeholder.length;m++){
                            ownedFields[m] = placeholder[m];
                        }
                        ownedFields[h] = allOwnedFields[k];
                    }
                    break;
                case 3:
                    if(((FieldSoda) allOwnedFields[k]).isPledged()){
                        placeholder = ownedFields.clone();
                        ownedFields = new Field[++h];
                        for(int m =0; m<placeholder.length;m++){
                            ownedFields[m] = placeholder[m];
                        }
                        ownedFields[h] = allOwnedFields[k];
                    }
                    break;
            }
        }


        ArrayString = new String[ownedFields.length];
        // Creating StringArray with pledged properties and price for unpledging
        for(int i = 0; i < ownedFields.length;i++){
            if(ownedFields[i].name != null){
                switch (ownedFields[i].fieldType){
                    case 1:
                        ArrayString[i] = ownedFields[i].name + " " +
                                lang.getString("price") +  " " +
                                (int)( (((FieldProperty) ownedFields[i]).getMortageValue()/100)*1.10)*100;
                        break;
                    case 2:
                        ArrayString[i] = ownedFields[i].name + " " +
                                lang.getString("price") +  " " +
                                (int)( (((FieldScandlines) ownedFields[i]).getMortageValue()/100)*1.10)*100;
                        break;
                    case 3:
                        ArrayString[i] = ownedFields[i].name + " " +
                                lang.getString("price") +  " " +
                                (int)( (((FieldSoda) ownedFields[i]).getMortageValue()/100)*1.10)*100;
                        break;

                }
            }
        }

        // Select a field and find it
        Field sellField = fieldFinder(gameInterface.displayDropdown(
                lang.getString("ChooseOwned"), ArrayString));

        switch (sellField.fieldType){
            case 1:
                if( (int)( (((FieldProperty) sellField).getMortageValue()/100)*1.10)*100 <=
                        player.getMoney()){
                    player.addMoney(-(int)( (((FieldProperty) sellField).getMortageValue()/100)*1.10)*100);
                    ((FieldProperty) sellField).setPledged(false);
                } else{
                    gameInterface.displayMessage(lang.getString("InsufficientFunds"));
                }
                break;
            case 2:
                if( (int)( (((FieldScandlines) sellField).getMortageValue()/100)*1.10)*100 <=
                        player.getMoney()) {
                    player.addMoney( -(int)( (((FieldScandlines) sellField).getMortageValue()/100)*1.10)*100);
                    ((FieldScandlines) sellField).setPledged(false);
                } else{
                    gameInterface.displayMessage(lang.getString("InsufficientFunds"));
                }
                break;
            case 3:
                if( (int)( (((FieldSoda) sellField).getMortageValue()/100)*1.10)*100 <=
                        player.getMoney()) {
                    player.addMoney(-(int)( (((FieldSoda) sellField).getMortageValue()/100)*1.10)*100);
                    ((FieldSoda) sellField).setPledged(false);
                }else{
                    gameInterface.displayMessage(lang.getString("InsufficientFunds"));
                }
                break;
        }

    }

}
