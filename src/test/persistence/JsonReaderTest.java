//package persistence;
//
//import model.User;
//import model.UserList;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JsonReaderTest extends JsonTest {
//
//    @Test
//    // EFFECTS: Read user list from users.json
//    void testReaderUserList() {
//        JsonReadUserList reader = new JsonReadUserList();
//        try {
//            UserList users = new UserList(reader.read()); //!!! read data here
//        } catch (IOException e) {
//            fail("file missing");
//            //when does this happen???, shouldn't ever happen cuz data/users.json (should) always exist
//        }
//    }
//
//    @Test
//    void testReaderNonExistentFile() {
//        JsonReadUser reader = new JsonReadUser("noSuchFile");
//        try {
//            User user = reader.read();
//            fail("IOException expected");
//        } catch (IOException e) {
//            // pass
//        }
//    }
//
//    @Test
//    void testReaderEmptyUser() {
//        JsonReadUser reader = new JsonReadUser("00000000-0000-0000-0000-000000000000");
//        try {
//            User user = reader.read();
//            assertEquals("name testReaderEmptyUser", user.getName());
//            assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), user.getId());
//            assertTrue(user.isEmpty());
//        } catch (IOException e) {
//            fail("Couldn't read from file");
//        }
//    }
//
//    @Test
//    void testReaderGeneralUser() {
//        JsonReadUser reader = new JsonReadUser("11111111-1111-1111-1111-111111111111");
//        try {
//            User user = reader.read();
//            assertEquals("name testReaderGeneralUser", user.getName());
//            assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), user.getId());
//            assertFalse(user.isEmpty());
//
//            assertEquals(2, user.getEnergyEntries().size());
//            assertEquals(1, user.getFocusEntries().size());
//            assertEquals(1, user.getMotivationEntries().size());
//
//            checkEntry(LocalDate.of(2022,3,2), LocalTime.of(5,0), 3,
//                    user.getEnergyEntries().get(0));
//            checkEntry(LocalDate.of(2022,1,1), LocalTime.of(12,0), 6,
//                    user.getFocusEntries().get(0));
//            checkEntry(LocalDate.of(2022,3,1), LocalTime.of(0,0), 9,
//                    user.getMotivationEntries().get(0));
//        } catch (IOException e) {
//            fail("Couldn't read from file");
//        }
//    }
//}