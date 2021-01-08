package Spil;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Nedenstående koder er en modificeret udgave, med udgangspunkt i:
// CDIO2, af os, gruppe 15.

class AccountTest {

    @Test
    void getBalance() {
        //Definerer Account med en start_balance på 1233
        Account account = new Account(1233);

        assertEquals(1233, account.getBalance());

        //Checker om pengebeholdningen bliver 0,
        //hvis addToBalance gør at pengebeholdningen bliver mindre end 0
        account.addToBalance(-1500);
        assertEquals(0, account.getBalance());

        //Checker om pengebeholdningen ikke forbliver 0
        //efter man tilføjer et positivt beløb
        account.addToBalance(2000);
        assertEquals(2000, account.getBalance());
    }

    @Test
    void addToBalance() {
        //Definerer Account med en start_balance på 1000
        Account account = new Account(1000);

        //Tilføjer beløb til pengebeholdningen
        account.addToBalance(233);
        //Tilføjer beløb til pengebeholdningen
        account.addToBalance(233);

        assertEquals(1466, account.getBalance());
    }
}