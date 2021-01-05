package Spil;

// Nedenstående kode er en modificeret udgave, med udgangspunkt i:
// CDIO3, af os, gruppe 15.

public class Player {
    private String name;
    private final Account account = new Account(0);
    private int position = 0;
    private boolean isInJail = false;
    private int id;
    private boolean[] ownsboth = new boolean[8];

    private boolean[] ownedFields = new boolean[16];

    public Player(String n, int money, int id){
        this.name = n;
        this.account.addToBalance(money);
        this.id = id;
    }
    // Definerer spillerens navn
    public void setName(String p_name){
        this.name = p_name;
    }

    // Tilføjer penge til nuværende formue
    public void addMoney(int p_money){
        account.addToBalance(p_money);
    }

    // Gives the player an ID
    public void setID(int id){
        this.id = id;
    }

    public int getID(){return id;}

    // Retunerer navn
    public String getName(){
        return this.name;
    }

    // Retunerer antal penge spilleren har
    public int getMoney(){
        return account.getBalance();
    }

    // Retunerer positionen af playeren
    public int getPosition(){
        return position;
    }

    // Setter positionen af playeren
    public void setPosition(int newPos){
        if (newPos >= 24) {
            position = newPos - 24;
            addMoney(2);
        }
        else {
            position = newPos;
            if (position == 18) {
                position = 6;
                isInJail = true;
            }
        }
    }

    public boolean ownsboth(int groupID) {
        return ownsboth[groupID];
    }

    public void setOwnedFields(int fieldID) {
        this.ownedFields[fieldID] = true;
        for(int i=0, k=0; i<ownedFields.length;i+=2, k++){
            if(ownedFields[i] && ownedFields[i+1]){
                ownsboth[k] = true;
            }
        }
    }

    public boolean[] getOwnedFields() {
        return ownedFields;
    }

    public boolean[] getOwnsboth() {
        return ownsboth;
    }

    public int[] getOwnedFieldsGui() {
        boolean[] boolArray = getOwnedFields();
        int[] ownedArray = new int[24];
        int ownedCount = 0;
        for (int i = 0; i < ownedArray.length; i++)  {
            if (i == 0 || i == 3|| i == 6 || i == 9|| i == 12 || i == 15|| i == 18 || i == 21)
            {ownedArray[i] = 0;}
            else {
                if (boolArray[ownedCount]) {
                    ownedArray[i] = i;
                    ownedCount++;
                } else {ownedArray[i] = 0; ownedCount++;}

            }

        }

        return ownedArray;
    }

    public boolean isInJail() {
        return isInJail;
    }

    public void setInJail(boolean inJail) {
        isInJail = inJail;
    }
}
