package Spil;

import java.util.Random;

// Nedenstående koder er genbrugt fra:
// CDIO3, af os, gruppe 15.

public class Dice {
    private int facevalue;

    Random r = new Random();

    public Dice(int value){
        facevalue = value;
    }


    public int Roll(){
        int[] sides = {1, 2, 3, 4, 5, 6};

        facevalue = sides[r.nextInt(6)];

        return facevalue;
    }

    public int getValue(){
        return facevalue;
    }
}