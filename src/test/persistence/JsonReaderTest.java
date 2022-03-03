package persistence;

import model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("noSuchFile");
        try {
            User user = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyUser() {
        JsonReader reader = new JsonReader("testReaderEmptyUser");
        try {
            User user = reader.read();
            assertEquals("name testReaderEmptyUser", user.getName());
            assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), user.getId());
            assertTrue(user.isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralUser() {
        JsonReader reader = new JsonReader("testReaderGeneralUser");
        try {
            User user = reader.read();
            assertEquals("name testReaderGeneralUser", user.getName());
            assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), user.getId());
            assertFalse(user.isEmpty());

            assertEquals(2, user.getEnergyEntries().size());
            assertEquals(1, user.getFocusEntries().size());
            assertEquals(0, user.getMotivationEntries().size());
            checkEntry(LocalDate.of(2022,3,2), LocalTime.of(5,0), 3,
                    user.getEnergyEntries().get(0));
            checkEntry(LocalDate.of(2022,1,1), LocalTime.of(12,0), 6,
                    user.getFocusEntries().get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}