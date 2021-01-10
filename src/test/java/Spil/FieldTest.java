package Spil;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FieldTest {
    Language lang = new Language("Danish.properties");


    @Test
    void FieldPropertiesTest(){
        FieldProperty test = new FieldProperty("rødovrevej.properties",lang);

        assertEquals("Rødovrevej",test.name);
        assertEquals(1,test.position);
        assertEquals(1,test.fieldType);
        assertEquals(0,test.fieldID);
        assertEquals(600,test.getPrice());
        assertEquals(600,test.getMortageValue());
        assertArrayEquals(new int[] {50,250,750,2250,4000,6000},test.getRent());
        assertEquals(0,test.getGroupID());
    }




}