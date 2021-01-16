package Spil;
import Spil.ChanceCards.ChanceCardChangeMoney;
import Spil.Fields.*;

public class RealEstateAgent {
     Field[] fieldArray;
     Player[] owners;
     int[] fieldType1 = new int[0];
     int[][] fieldType1Sorted;
     int[] fieldType2 = new int[0];

    public RealEstateAgent(Field[] fieldInput) {
        // Creates fields
        int totalGroupNumber = 0;

        fieldArray = fieldInput;
        owners = new Player[fieldArray.length];



        for (int i=0;i<fieldArray.length;i++){
            int[] placeholder;


            switch (fieldArray[i].fieldType){
                case 1:
                    int n;
                    placeholder = fieldType1.clone();
                    fieldType1 = new int[fieldType1.length+1];
                    for(n = 0;n<placeholder.length;n++){
                        fieldType1[n] = placeholder[n];
                    }
                    fieldType1[n] = i;
                    break;
                case 2:
                    int l;
                    placeholder = fieldType2.clone();
                    fieldType2 = new int[fieldType1.length+1];
                    for( l = 0;l<placeholder.length;l++){
                        fieldType2[l] = placeholder[l];
                    }
                    fieldType2[l] = i;
                    break;
                default:
                    break;

            }


        }

        for (int i=0; i < fieldType1.length;i++) {
            if(totalGroupNumber < ((FieldProperty) fieldArray[fieldType1[i]]).getGroupID()+1){
                totalGroupNumber = ((FieldProperty) fieldArray[fieldType1[i]]).getGroupID()+1;
            }

        }

        fieldType1Sorted = new int[totalGroupNumber][];



        for (int i=0;i<fieldType1.length;i++) {
            int[] placeholder;
            int input =((FieldProperty) fieldArray[fieldType1[i]]).getGroupID();


            placeholder = fieldType1Sorted[input].clone();
            fieldType1Sorted[input] = new int[placeholder.length+1];

            int n;
            for (n=0; n< placeholder.length;n++){
               fieldType1Sorted[input][n] = placeholder[n];
            }
            fieldType1Sorted[input][n] = fieldType1[i];


        }







    }

    public void setOwner(Player player,Field field) {
        owners[field.position] = player;
    }

    public Player checkOwner(Field field){
        return owners[field.position];
    }


    public boolean isAllOwned(Field field) {
       switch (field.fieldType){
           case 1:




           case 2:
               if(owners[field.position] != null && owners[fieldType2[0]].getID() == owners[fieldType2[1]].getID() && owners[fieldType2[0]].getID() == owners[fieldType2[2]].getID() && owners[fieldType2[0]].getID() == owners[fieldType2[3]].getID()){
                   return true;
               } else{
                   return false;
               }

           default:
               throw new RuntimeException("Error. Not a buyable field");

        }
    }
}
