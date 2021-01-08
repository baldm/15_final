package Spil;

// Nedenstående koder er en modificeret udgave, med udgangspunkt i:
// CDIO3, af os, gruppe 15.
public class Account {


    private int balance;

    private int active_value;

    public Account(int start_balance){

        this.balance = start_balance;
        active_value = 0;

    }

    //Retunere spillerens balance
    public int getBalance(){ return this.balance; }

    //Tilføjer et beløb til balancen og checker om balancen er mindre end 0
    public void addToBalance(int amount){ balance += amount;  }

    /**
     * Returns the total value of all properties owned by a player
     * @return Integer value of the active_value
     */
    public int getActiveValue(){ return active_value; }

    /**
     * Add an amount to the total value of all properties owned by a player
     * @param value value of the property
     */
    public void addToActiveValue(int value){ active_value += value; }

    /**
     * Check if player is bankrupt
     * @param amount Amount to bed added to balance
     * @return if player is bankrupt
     */
    public boolean checkBankrupt(int amount){
        // TODO: Skal den mulighvis være <= her?
        return (balance + active_value) - amount < 0;

    }

}
