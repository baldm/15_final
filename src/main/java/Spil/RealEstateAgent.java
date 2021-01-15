package Spil;
import Spil.Fields.*;

public class RealEstateAgent {
     Field[] fieldArray;
     Player[] owners;
     int[] fieldType1 = new int[0];
     int[] fieldType2 = new int[0];

    public RealEstateAgent(Field[] fieldInput) {
        // Creates fields

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
               for (int k=0;k<fieldType1.length;k +=3){
                   if(field.fieldID < k+3  ){
                       if(owners[field.position] != null && owners[fieldType1[k]].getID() == owners[fieldType1[k+1]].getID() && owners[fieldType1[k]].getID() == owners[fieldType1[k+2]].getID()){
                           return true;
                       } else {
                           return false;
                       }
                   }
               }
           case 2:
               if(owners[field.position] != null && owners[fieldType2[0]].getID() == owners[fieldType2[1]].getID() && owners[fieldType2[0]].getID() == owners[fieldType2[2]].getID() && owners[fieldType2[0]].getID() == owners[fieldType2[3]].getID()){
                   return true;
               } else{
                   return false;
               }

           default:
               throw new RuntimeException("Error in field reader");

        }
    }
}
