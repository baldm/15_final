package Spil;

import org.junit.jupiter.api.Test;

import Spil.Fields.Field;
import Spil.Fields.FieldProperty;
import Spil.Fields.FieldScandlines;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateAgentTest {

    Language lang = new Language("danish.properties");

    FieldFactory factory = new FieldFactory(lang);
    Field[] fieldArray = factory.getAllFields();

    RealEstateAgent agent = new RealEstateAgent(fieldArray);

    Player p1 = new Player("Spiller1", 1000, 0);
    Player p2 = new Player("Spiller2", 1000, 1);

    @Test
    void setAndCheckOwner(){

        FieldProperty prop = new FieldProperty("allegade.properties");

        agent.setOwner(p1, prop);

        assertEquals(agent.checkOwner(prop).getID(), p1.getID());

    }

    @Test
    void isAllOwned(){

        FieldProperty rod = new FieldProperty("rødovrevej.properties");
        FieldProperty hvid = new FieldProperty("hvidovrevej.properties");

        agent.setOwner(p1, rod);
        agent.setOwner(p1, hvid);
        
        assertTrue(agent.isAllOwned(rod));

        FieldProperty allegade = new FieldProperty("allegade.properties");

        assertFalse(agent.isAllOwned(allegade));
    }

    @Test
    void ferriesOwned(){
        FieldScandlines scandlines = new FieldScandlines("GedserRostock.properties");
        FieldScandlines molslinjen = new FieldScandlines("Molslinjen.properties");

        agent.setOwner(p1, scandlines);

        assertTrue(agent.ferriesOwned(p1) == 1);

        agent.setOwner(p1, molslinjen);

        assertTrue(agent.ferriesOwned(p1) == 2);
    }

    @Test
    void getOwnedFields(){

        FieldProperty allegade = new FieldProperty("allegade.properties");
        FieldProperty amagertorv = new FieldProperty("amagertorv.properties");

        agent.setOwner(p1, allegade);
        agent.setOwner(p1, amagertorv);

        assertEquals(agent.getOwnedFields(p1)[0].name, allegade.name);
        assertEquals(agent.getOwnedFields(p1)[1].name, amagertorv.name);
    }

    @Test
    void getPledgedFields(){

        FieldProperty allegade = new FieldProperty("allegade.properties");
        FieldProperty amagertorv = new FieldProperty("amagertorv.properties");

        agent.setOwner(p1, allegade);
        agent.setOwner(p1, amagertorv);

        assertEquals(agent.getPledgedFields(p1)[0].name, allegade.name);

    }


    
}
