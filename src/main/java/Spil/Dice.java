package Spil;

import java.util.Random;

// Nedenstående koder er genbrugt fra:
// CDIO3, af os, gruppe 15.

public class Dice {
    private int facevalue;

    Random r = new Random();

    /**
     * Set facevalue to the input (value)
     * @param value number of eyes on the dice
     */
    public Dice(int value){
        facevalue = value;
    }

    /**
     * Generates a random number between 1 and 6
     * @return integer between 1 and 6
     */
    public int Roll(){
        int[] sides = {1, 2, 3, 4, 5, 6};

        facevalue = sides[r.nextInt(6)];

        return facevalue;
    }

    /**
     * Takes dice last value
     * @return value of dice
     */
    public int getValue(){
        return facevalue;
    }
}