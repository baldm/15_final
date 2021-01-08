package Spil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

// Nedenstående koder er en modificeret udgave, med udgangspunkt i:
// CDIO2, af os, gruppe 15.

class DiceTest {

    @RepeatedTest(100)
    void roll() {
        Dice dice_one = new Dice(6);
        Dice dice_two = new Dice(6);
        int a = 100000; // antal gange vi slår med de to terninger
        int[] slag = new int[a];
        int[] hyppighed = new int[11];

        for(int i=0; i<a;i++) {
            slag[i] = dice_one.Roll() + dice_two.Roll();
            switch(slag[i]){
                case 2: hyppighed[0]++;
                    break;
                case 3: hyppighed[1]++;
                    break;
                case 4: hyppighed[2]++;
                    break;
                case 5: hyppighed[3]++;
                    break;
                case 6: hyppighed[4]++;
                    break;
                case 7: hyppighed[5]++;
                    break;
                case 8: hyppighed[6]++;
                    break;
                case 9: hyppighed[7]++;
                    break;
                case 10: hyppighed[8]++;
                    break;
                case 11: hyppighed[9]++;
                    break;
                case 12: hyppighed[10]++;
                    break;
            }
        }
        //Først udregnes sandsynlighederne
        double[] px = new double[hyppighed.length];
        for(int i=0;i<hyppighed.length;i++){
            px[i] = (double) hyppighed[i]/a;
        }

        //Først udregnes mu
        double mu =0;

        for(int i=0;i<hyppighed.length;i++){
            mu += (double)(i+2) * px[i];
        }
        //Nu udregnes sigma^2
        double sigma2=0;
        double sigma;

        for(int i=0;i< hyppighed.length;i++){
            sigma2 += (double) Math.pow((i+2)-mu,2) * px[i];
        }
        sigma = Math.sqrt(sigma2);

        //Nu udregnes den teoretiske sandsynlighed
        double[] pt = new double[hyppighed.length];
        for(int i=0; i< 6;i++){
            pt[i] = (double)(i+1)/36;
        }
        for(int i=0;i< 5;i++){
            pt[i+6] = (double)(5-i)/36;
        }
        //Nu udregnes den teoretiske mu
        double muT = 0;
        for(int i=0;i<hyppighed.length;i++){
            muT += (double)(i + 2) * pt[i];
        }

        //Så udregnes den teoretiske sigma^2
        double sigmaT2=0;
        double sigmaT;

        for(int i=0;i< hyppighed.length;i++){
            sigmaT2 +=(double)Math.pow((i+2)-muT,2) * pt[i];
        }
        sigmaT = Math.sqrt(sigmaT2);

        //nu sammenlignes de to sigma og mu
        double afvigelseMu = Math.abs(muT-mu)/muT;
        double afvigelseSigma = Math.abs(sigmaT-sigma)/sigmaT;

        assertEquals(afvigelseSigma < 0.05 && afvigelseMu < 0.05, true);

    }

    @Test
    void getValue() {
        Dice dice_one = new Dice(6);
        assertEquals(dice_one.getValue(), 6);
    }
}