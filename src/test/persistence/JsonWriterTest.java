package persistence;


import model.EnergyEntry;
import model.FocusEntry;
import model.MotivationEntry;
import model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            User user = new User("chris");
//            JsonWriter writer = new JsonWriter(user.getId().toString()); !!! normally do like this
            JsonWriter writer = new JsonWriter("testWriterEmptyUser");
            writer.open();
            writer.write(user);
            writer.close();

            JsonReader reader = new JsonReader("testWriterEmptyUser");
            user = reader.read();
            assertEquals("chris", user.getName());
            assertTrue(user.isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralUser() {
        try {
            User user = new User("chris");

            user.add(new EnergyEntry(LocalDate.now(), LocalTime.of(5, 0), 8));
            user.add(new EnergyEntry(LocalDate.now(), LocalTime.of(5, 0), 3));
            user.add(new FocusEntry(LocalDate.now(), LocalTime.of(7, 0), 5));
            user.add(new MotivationEntry(LocalDate.now(), LocalTime.of(1, 0), 9));

            JsonWriter writer = new JsonWriter("testWriterGeneralUser"); //normally fileName is user.getId().toString()

            writer.open();
            writer.write(user);
            writer.close();

//            JsonReader reader = new JsonReader("testWriterGeneralWorkroom");
//            user = reader.read();
//            assertEquals("chris", user.getName());
//
//            List<Thingy> thingies = wr.getThingies();
//            assertEquals(2, thingies.size());
//            checkThingy("saw", Category.METALWORK, thingies.get(0));
//            checkThingy("needle", Category.STITCHING, thingies.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}