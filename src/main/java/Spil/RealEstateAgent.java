package Spil;
import Spil.ChanceCards.ChanceCardChangeMoney;
import Spil.Fields.*;

public class RealEstateAgent {
     Field[] fieldArray;
     Player[] owners;
     int[] fieldType1 = new int[0];
     int[][] fieldType1Sorted;
     int[] fieldType2 = new int[0];
     int[] fieldType3 = new int[0];
    /**
     * Creates a RealEstateAgent for the game.
     * Tracks all fields owner and if a field is pledged
     */

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
                    fieldType2 = new int[fieldType2.length+1];
                    for( l = 0;l<placeholder.length;l++){
                        fieldType2[l] = placeholder[l];
                    }
                    fieldType2[l] = i;
                    break;
                case 3:
                    placeholder = fieldType3.clone();
                    fieldType3 = new int[fieldType3.length+1];
                    for( l = 0;l<placeholder.length;l++){
                        fieldType3[l] = placeholder[l];
                    }
                    fieldType3[l] = i;
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

        fieldType1Sorted = new int[totalGroupNumber][0];



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

    /**
     * Checks if a player owns all fields the same group of a field of type
     * "FieldProperties" or "FieldSoda"
     * @return boolean
     */

    public boolean isAllOwned(Field field) {
        boolean isTrue = false;
        //Checking which field-type the field is
       switch (field.fieldType){
           //Fieldtype: FieldProperty
           case 1:

               int input = ((FieldProperty) field).getGroupID();
               for (int l = 1; l<fieldType1Sorted[input].length;l++){
                   if (owners[fieldType1Sorted[input][l]] != null &&
                           owners[fieldType1Sorted[input][l-1]] != null &&
                           owners[fieldType1Sorted[input][l]].getID() ==
                           owners[fieldType1Sorted[input][l-1]].getID() ){
                       isTrue = true;
                   }else{
                       return false;
                   }

               }
               return isTrue;
           //Fieldtype: FieldSoda
           case 3:

               for (int l=1;l<fieldType3.length;l++){
                   if((owners[fieldType3[l-1]] != null &&
                           owners[fieldType3[l]] != null &&
                           owners[fieldType3[l-1]].getID() ==
                                   owners[fieldType3[l]].getID())){
                       isTrue = true;
                   }else{
                       return false;
                   }

               }
               return isTrue;
           default:
               throw new RuntimeException("Error. Not a Property or Soda field");

        }
    }
    /**
     * Checks how many of fieldtype "FieldScandlines" a palyer owns
     * @return int
     */
    public int ferriesOwned(Player player){
        int ferriesOwned = 0;
        for(int l =0; l< fieldType2.length;l++){
            if(owners[fieldType2[l]] != null &&
                    owners[fieldType2[l]].getID() == player.getID()){
                ferriesOwned++;
            }
        }
        return ferriesOwned;
    }
    /**
     * Finds all non-pledged fields a palyer owns
     * @return Field[]-array
     */
    public Field[] getOwnedFields (Player player) {
        Field[] ownedFields = new Field[0];
        //Creating an array with all owned fields
       ownedFields = addField(fieldType1,ownedFields,player);
       ownedFields = addField(fieldType2,ownedFields,player);
        ownedFields = addField(fieldType3,ownedFields,player);

        //Checks if the array is equal "null"
        if(ownedFields != null) {
            Field[] allOwnedFields = ownedFields.clone();
            Field [] placeholder;
            int h = 0;
            ownedFields = new Field[h];
            for (int i = 0; i < allOwnedFields.length; i++) {
                //Sorts and casts all fields to their respective field-type
                //Removing all fields which is pledged
                switch (allOwnedFields[i].fieldType) {
                    case 1:
                        if (!((FieldProperty) allOwnedFields[i]).isPledged()) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[++h];

                            for(int k = 0;k<placeholder.length;k++){
                                ownedFields[k] = placeholder[k];
                            }
                            ownedFields[h - 1] = allOwnedFields[i];
                        }
                        break;
                    case 2:
                        if (!((FieldScandlines) allOwnedFields[i]).isPledged()) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[++h];

                            for(int k = 0;k<placeholder.length;k++){
                                ownedFields[k] = placeholder[k];
                            }
                            ownedFields[h - 1] = allOwnedFields[i];

                        }
                        break;
                    case 3:
                        if (!((FieldSoda) allOwnedFields[i]).isPledged()) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[++h];

                            for(int k = 0;k<placeholder.length;k++){
                                ownedFields[k] = placeholder[k];
                            }
                            ownedFields[h - 1] = allOwnedFields[i];

                        }
                        break;
                }
            }

        }
        return ownedFields;
    }

    /**
     * Finds all pledged fields a palyer owns
     * @return Field[]-array
     */
    public Field[] getPledgedFields (Player player) {
        Field[] ownedFields = new Field[0];
        Field[] placeholder;
        ownedFields = addField(fieldType1, ownedFields, player);
        ownedFields = addField(fieldType2, ownedFields, player);
        //Making sure player owns any fields
        if(ownedFields != null){
            Field[] allOwnedFields = ownedFields.clone();
            int h = 0;
            ownedFields = new Field[h];
            for (int i = 0; i < allOwnedFields.length; i++) {
                //Sorts and casts all fields to their respective field-type
                //Removing all fields which is not pledged
                switch (allOwnedFields[i].fieldType) {
                    case 1:
                        if (((FieldProperty) allOwnedFields[i]).isPledged()) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[++h];
                            for(int k =0; k< placeholder.length;k++){
                                ownedFields[k] = placeholder[k];
                            }
                            ownedFields[h - 1] = allOwnedFields[i];
                        }
                        break;
                    case 2:
                        if (((FieldScandlines) allOwnedFields[i]).isPledged()) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[++h];
                            for(int k =0; k< placeholder.length;k++){
                                ownedFields[k] = placeholder[k];
                            }
                            ownedFields[h-1] = allOwnedFields[i];
                        }
                        break;
                    case 3:
                        if (((FieldSoda) allOwnedFields[i]).isPledged()) {
                            placeholder = ownedFields.clone();
                            ownedFields = new Field[++h];
                            for(int k =0; k< placeholder.length;k++){
                                ownedFields[k] = placeholder[k];
                            }
                            ownedFields[h - 1] = allOwnedFields[i];

                        }
                        break;
                }

            }
        }

        return ownedFields;
    }

    /**
     * Expands Field-Array, then adds all the newly created Fields
     * @return an array of Fields
     */

    private Field[] addField(int[] input,Field[] fieldInput,Player player){
        Field[] ownedFields = fieldInput;
        Field[] placeholder;

        for (int l = 0; l< input.length;l++){
            if(owners[input[l]] != null && owners[input[l]].getID() == player.getID()){
                placeholder = ownedFields.clone();
                ownedFields = new Field[ownedFields.length +1];
                int k;
                for (k =0; k< placeholder.length;k++){
                    ownedFields[k] = placeholder[k];
                }
                ownedFields[k] = fieldArray[input[l]];
            }

        }

        return ownedFields;

    }

    public int[][] getFieldType1Sorted() {
        return fieldType1Sorted;
    }
}
