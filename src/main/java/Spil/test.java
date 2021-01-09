package Spil;

public class test {
    public static void main(String[] args) {
        Language lang = new Language("Danish");
        ChanceCard test = new ChanceCardChangeMoney("FuldtStop.properties",lang);
        System.out.println(test.cardID);
        System.out.println(test.description);
    }
}
