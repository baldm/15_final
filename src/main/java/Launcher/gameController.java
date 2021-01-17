package Launcher;


import Gui.Interface;
import Spil.*;
import Spil.ChanceCardFactory;
import Spil.Fields.Field;
import Spil.ChanceCards.*;
import Spil.Fields.FieldProperty;

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
                // TEST:
                game.buyField(player, game.fieldFinder("Frederiksberggade"));
                game.buyField(player, game.fieldFinder("Rådhuspladsen"));

                // TEST END
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


        // Chance card logic
        if (fieldArray[currentPlayer.getPosition()].fieldType == 5)  // Checks if its a chance field
        {
            drawChanceCard(currentPlayer);
        }

        // Buying field logic
        // TODO: Tilføj soda og scandlines
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

    // TODO: DOCstring og comments
    private void buyableField(Player player) {

        Field currentField = fieldArray[player.getPosition()];
        Player owner = estateAgent.checkOwner(currentField);


        if (owner == null) {
            gameInterface.displayMessage(lang.getString("LandedOnBuyableProperty"));
<<<<<<< Updated upstream
            String choice = gameInterface.displayMultiButton(lang.getString("WantToBuy"), lang.getString("Yes"), lang.getString("No"));

            if (choice.equals("Yes") || choice.equals("Ja")) {
                player.addMoney(-((FieldProperty) currentField).getPrice());
                estateAgent.setOwner(player, currentField);
                gameInterface.setOwner(player, player.getPosition());
            }
=======
            buyField(player, currentField);
>>>>>>> Stashed changes

        } else if (owner == player) {
            // Landed on your own field, do nothing
        } else {
            gameInterface.displayMessage(lang.getString("LandedOnBoughtProperty"));
<<<<<<< Updated upstream
            if (currentField.fieldType == 1) {
                int amountHouses = ((FieldProperty) currentField).getHouseNumber();
                int rent = ((FieldProperty) currentField).getRent()[amountHouses];
                player.addMoney(- rent);
                owner.addMoney(rent);
                gameInterface.setPlayerBalance(player);
                gameInterface.setPlayerBalance(owner);
                gameInterface.displayMessage(lang.getString("PayedRent")+rent);
=======
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
            String[] turnChoices = {lang.getString("BuyHouses"), lang.getString("SellHouses"), lang.getString("SellField"), lang.getString("MortgageHouses"), lang.getString("Skip")};
            String turnChoice = gameInterface.displayDropdown(lang.getString("TurnChoice"), turnChoices);

            if (turnChoice.equals(turnChoices[0])) {
                buyHouses(player);
            } else if (turnChoice.equals(turnChoices[1])) {
                sellHouses(player);
            }else if (turnChoice.equals(turnChoices[2])) {
                sellField(player);
            } else if (turnChoice.equals(turnChoices[3])) {
                PledgeField(player);
            } else  {
                break;
>>>>>>> Stashed changes
            }
            // TODO: Tilføj soda og scandlines
        }

<<<<<<< Updated upstream

=======
        // handles loss
        if (hasPlayerLost(player)) {
            removePlayerFromGame(player);
        }

    }

    // TODO: TEST
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
>>>>>>> Stashed changes

        //5. Pay rent to owner that is equivalent of the rent determined by houses on field.


<<<<<<< Updated upstream
=======

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
>>>>>>> Stashed changes
    }

    private void endOfTurn(Player player) {

        // prompt player if they wish to do anything to their plots or whatever

        // prompt mortage

    }

    private void mortgage(Player player) {


        /*
        Man kan kun pansætte ubebyggende grunde

<<<<<<< Updated upstream
        1. Vil du hæve pantsætning eller pantsætte noget
=======
        for (int i = 0; i < ownedArray.length; i++) {
            if (ownedArray[i] != null) {
                ownedArrayString[i] = ownedArray[i].name;
            }
        }
>>>>>>> Stashed changes

        if hæve:
                1. Hvilken grund vil du hæve pæntsætningen på
                2. Det koster 10% mere end grunden
                3. har man penge nok?
                4. Overfør penge for at modtage property tilbage

        else:
            1. Sælg huse på grunden til banken
            2. Få penge iforhold til hvad grunden er vær
            3. Mærker grund som pæntsat


<<<<<<< Updated upstream
=======
        String[] playerNamesStringArray = new String[playerArray.length];

        for (int i = 0; i < playerArray.length; i++) {
            playerNamesStringArray[i] = playerArray[i].getName();
        }

        String playerChoice = gameInterface.displayDropdown(lang.getString("PickPlayer"), playerNamesStringArray);

        Player buyer;

        for (Player loopPlayer : playerArray) {
            if (loopPlayer.getName().equals(playerChoice)) {
                buyer = loopPlayer;
            }
        }



        // OLD
        player.addMoney(fieldPrice);
        estateAgent.setOwner(null, sellField);
        gameInterface.removeOwner(player, sellField.position);
>>>>>>> Stashed changes

         */
    }
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
=======
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
                    PledgeField(player);
                } else {
                    gameInterface.displayMessage(lang.getString("PlayerLeftGame"));
                    return true;
                }
            }
        }
        return false;
    }

    private void removePlayerFromGame(Player player) {

        // TODO: Handle players properties

        Player[] newPlayerArray = new Player[playerArray.length-1];

        playerArray[player.getID()] = null;
        gameInterface.removePlayer(player);

        for (int i = 0, k=0; i < playerArray.length; i++) {
            if (playerArray[i] != null) {
                newPlayerArray[k] = playerArray[i];
                k++;
            }
        }
        playerArray = newPlayerArray;


    }
    private void gameIsOver(Player player) {
        gameInterface.displayMessage(lang.getString("GameOver"));
        gameInterface.displayChance("Congrats");
        gameInterface.displayMessage(lang.getString("WinnerIs") + " " + player.getName());
    }
    /**
     * Full logic for a player to pledge an owned property
     * @param player - player object which landed on the field
     */
    private void PledgeField(Player player){
>>>>>>> Stashed changes

}
