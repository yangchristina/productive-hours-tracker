package persistence;


import model.*;
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
    void testWriterUserList() {
        JsonReadUserList reader = new JsonReadUserList();

        try {
            UserList users = new UserList(reader.read());
            User user = new User("test");
            users.add(user);

            JsonWriter writer = new JsonWriter("users");
            writer.open();
            writer.write(users);
            writer.close();

            users = new UserList(reader.read());
            assertTrue(users.getNames().contains(user.getName()));
        } catch (IOException e) {
            fail("file not found");
        }
    }

    @Test
    void testWriterEmptyUser() {
        try {
            User user = new User("chris");
            JsonWriter writer = new JsonWriter("testWriterEmptyUser");
            writer.open();
            writer.write(user);
            writer.close();

            JsonReadUser reader = new JsonReadUser("testWriterEmptyUser");
            user = reader.read();
            assertEquals("chris", user.getName());
            assertTrue(user.getProductivityLog().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralUser() {
        try {
            User user = new User("chris");

            user.getProductivityLog().add(new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(), LocalTime.of(5, 0), 8));
            user.getProductivityLog().add(new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(), LocalTime.of(5, 0), 3));
            user.getProductivityLog().add(new ProductivityEntry(ProductivityEntry.Label.FOCUS, LocalDate.now(), LocalTime.of(7, 0), 5));
            user.getProductivityLog().add(new ProductivityEntry(ProductivityEntry.Label.MOTIVATION, LocalDate.now(), LocalTime.of(1, 0), 9));

            JsonWriter writer = new JsonWriter("testWriterGeneralUser"); //normally fileName is user.getId().toString()

            writer.open();
            writer.write(user);
            writer.close();

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}