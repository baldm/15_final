package Spil;
import Spil.ChanceCards.ChanceCard;
import Spil.ChanceCards.ChanceCardChangeMoney;
import Spil.ChanceCards.ChanceCardMovePlayer;
import Spil.ChanceCards.ChanceCardMovePlayerTo;
import Spil.Fields.*;

import java.util.concurrent.ThreadLocalRandom;

public class test {
    private static ChanceCard[] allChanceCards;
    private static ChanceCard[] drawAbleChanceCards;

    public static void main(String[] args) {
        Language lang = new Language("Danish.properties");
        ChanceCardFactory chanceCardFactory = new ChanceCardFactory(lang);
        allChanceCards = chanceCardFactory.getAllCards();
        drawAbleChanceCards = chanceCardFactory.getAllCards();

        Player test = new Player("test",1000,0);

        System.out.println(test.getMoney());
        System.out.println(test.getPosition());
        drawChanceCard(test);
        System.out.println(test.getMoney());
        System.out.println(test.getPosition());



    }

    private static void drawChanceCard(Player player) {
        ChanceCard drawedCard;
        if (drawAbleChanceCards.length > 1) {
            int drawedCardNumber = ThreadLocalRandom.current().nextInt(0, drawAbleChanceCards.length);
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
                player.addMoney(((ChanceCardChangeMoney) drawedCard).getMoneyChange());
            case 3:
                player.setPosition(((ChanceCardMovePlayerTo) drawedCard).getMoveTo());
            case 4:
                player.setPosition(player.getPosition() + ((ChanceCardMovePlayer) drawedCard).getMovePlayer());
        }
    }
}
