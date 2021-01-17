package Spil;

import Spil.Fields.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class FieldTest {



    @Test
    void FieldPropertiesTest(){
        FieldProperty test = new FieldProperty("rødovrevej.properties");

        assertEquals("Rødovrevej",test.name);
        assertEquals(1,test.position);
        assertEquals(1,test.fieldType);
        assertEquals(0,test.fieldID);
        assertEquals(1200,test.getPrice());
        assertEquals(600,test.getMortageValue());
        assertArrayEquals(new int[] {50,250,750,2250,4000,6000},test.getRent());
        assertEquals(0,test.getGroupID());
    }

    @Test
    void AllFieldTest(){
        Language lang = new Language("danish.properties");
        FieldFactory factory = new FieldFactory(lang);

        Field[] allFields = factory.getAllFields();

        int propcount = 0;
        int chancecount = 0;
        int sodacount = 0;
        int jailcount = 0;
        int scancount = 0;
        int specialcount = 0;

        for(int i = 0; i < allFields.length; i++){
            Field field = allFields[i];
            switch(field.fieldType){
                case 1:
                    FieldProperty fProperty = (FieldProperty) field;
                    try {
                        assertEquals(true, fProperty.name != null);
                        assertEquals(true, fProperty.position != 0);
                        assertEquals(true, fProperty.fieldType == 1);
                        assertEquals(true, fProperty.fieldID >= 0);
                        assertEquals(true, fProperty.getMortageValue() != 0);
                        assertEquals(true, fProperty.getPrice() != 0);
                        assertEquals(true, fProperty.getHousePrice() != 0);
                        assertEquals(true, fProperty.getRent() != null);
                        assertEquals(true, fProperty.getGroupID() >= 0);
                        propcount += 1;
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    break;
                case 5:
                    FieldChance fChance = (FieldChance) field;
                    try {
                        assertEquals(true, fChance.name != null);
                        assertEquals(true, fChance.position != 0);
                        assertEquals(true, fChance.fieldType == 5);
                        assertEquals(true, fChance.fieldID >= 0);
                        chancecount += 1;
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    break;

                case 3:
                    FieldSoda fSoda = (FieldSoda) field;
                    try {
                        assertEquals(true, fSoda.name != null);
                        assertEquals(true, fSoda.position != 0);
                        assertEquals(true, fSoda.fieldType == 3);
                        assertEquals(true, fSoda.fieldID >= 0);
                        assertEquals(true, fSoda.getPrice() != 0);
                        assertEquals(true, fSoda.getRentMultiplier() != null);
                        assertEquals(true, fSoda.getMortageValue() != 0);
                        sodacount += 1;
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    break;

                case 6:
                    FieldJail fieldJail = (FieldJail) field;
                    try {
                        assertEquals(true, fieldJail.name != null);
                        assertEquals(true, fieldJail.position != 0);
                        assertEquals(true, fieldJail.fieldType == 6);
                        assertEquals(true, fieldJail.fieldID >= 0);
                        jailcount += 1;
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    break;

                case 2:
                    FieldScandlines fieldScandlines = (FieldScandlines) field;
                    try {
                        assertEquals(true, fieldScandlines.name != null);
                        assertEquals(true, fieldScandlines.position != 0);
                        assertEquals(true, fieldScandlines.fieldType == 2);
                        assertEquals(true, fieldScandlines.fieldID >= 0);
                        assertEquals(true, fieldScandlines.getPrice() != 0);
                        assertEquals(true, fieldScandlines.getRent() != null);
                        assertEquals(true, fieldScandlines.getGroupID() >= 0);
                        assertEquals(true, fieldScandlines.getMortageValue() != 0);
                        scancount += 1;
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    break;

                case 4:
                    FieldSpecial fieldSpecial = (FieldSpecial) field;
                    try {
                        assertEquals(true, fieldSpecial.name != null);
                        assertEquals(true, fieldSpecial.position >= 0);
                        assertEquals(true, fieldSpecial.fieldType == 4);
                        assertEquals(true, fieldSpecial.fieldID >= 0);
                        specialcount += 1;
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    break;
            }

        }

        assertEquals(22, propcount);
        assertEquals(6, chancecount);
        assertEquals(2, sodacount);
        assertEquals(2, jailcount);
        assertEquals(4, scancount);
        assertEquals(4, specialcount);
    }




}