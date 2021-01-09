package Spil;


    import java.io.File;

    public class ChanceCardFactoryTestDriver {
        private String[] cardNames;
        private ChanceCard[] cards;

        public ChanceCardFactoryTestDriver(Language lang) {


            File allCards = new File("./" + "ChanceCards");


            cardNames = allCards.list();

            cards = new ChanceCardTestDriver[cardNames.length];
            for (int i = 0; i < cardNames.length; i++) {

                cards[i] = new ChanceCardTestDriver(cardNames[i], lang);
            }

        }

        public ChanceCard[] getCards() {
            return cards;
        }


    }


