package model;

import model.exceptions.InvalidUserException;
import model.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReadUserList;
import ui.exceptions.InvalidInputException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserListTest {
    private static final UUID USER2_ID = UUID.randomUUID();

    private UserList users;
    private User user;
    private User user2;
    private User userInFile;

    @BeforeEach
    void runBefore() {
        JsonReadUserList reader = new JsonReadUserList();
        try {
            users = new UserList(reader.read()); //!!! read data here
        } catch (IOException e) {
            fail("file missing");
        }

        user = new User("test user 1");
        user2 = new User("test user 2", USER2_ID, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        userInFile = new User("name testReaderEmptyUser", UUID.fromString("00000000-0000-0000-0000-000000000000"),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void testConstructor() {
        assertNotNull(users);
    }

    @Test
    void testAdd() {
        int initialSize = users.size();

        users.add(user);
        assertEquals(initialSize + 1, users.size());

        users.add(user2);
        assertEquals(initialSize + 2, users.size());
        try {
            users.getUserId("test user 1");
            assertEquals(USER2_ID, users.getUserId("test user 2"));
        } catch (InvalidUserException e) {
            fail("User wasn't added to list");
        }
    }

    @Test
    void testRegisterNewUser() {
        clearUsers(users);

        try {
            users.register(user);
            assertTrue(users.getNames().contains(user.getName()));
            assertEquals(1, users.size());
        } catch (UserAlreadyExistsException e) {
            fail("user already there");
        }
    }

    @Test
    void testRegisterOldUser() {
        users.add(user);
        try {
            users.register(user);
            fail("Exception should have been thrown");
        } catch (UserAlreadyExistsException e) {
            // expected
        }
    }

    @Test
    void testGetUserIdInvalid() {
        try {
            users.getUserId("invalid user");
            fail("InvalidUserException should have been thrown");
        } catch (InvalidUserException e) {
            // expected
        }
    }

    @Test
    void testGetUserIdValid() {
        users.add(user2);
        try {
            assertEquals(USER2_ID, users.getUserId("test user 2"));
        } catch (InvalidUserException e) {
            fail("User wasn't added to list");
        }
    }

    @Test
    void testGetNames() {
        users.add(user);
        users.add(user2);

        assertTrue(users.getNames().contains(user.getName()));
        assertTrue(users.getNames().contains(user2.getName()));
    }

    @Test
    void testRemoveValidName() {
        users.add(user);
        users.add(user2);

        Set<String> names = new HashSet<>(users.getNames());
        for (String name : names) {
            assertTrue(users.remove(name));
        }

        assertTrue(users.isEmpty());
    }

    @Test
    void testRemoveValidUser() {
        users.add(user);
        users.add(user2);
        assertTrue(users.remove(user));
        assertTrue(users.remove(user2));
    }

    @Test
    void testRemoveInvalidName() {
        users.add(user);
        users.remove(user.getName());
        assertFalse(users.remove(user.getName()));
    }

    @Test
    void testRemoveInvalidUser() {
        users.add(user);
        users.remove(user);
        assertFalse(users.remove(user));
    }

    @Test
    void testIsEmpty() {
        // clear users first
        Set<String> names = new HashSet<>(users.getNames());
        for (String name : names) {
            users.remove(name);
        }

        assertTrue(users.isEmpty());
        users.add(user);
        assertFalse(users.isEmpty());
    }

    @Test
    void testSize() {
        int initialSize = users.size();
        users.add(user);
        assertEquals(initialSize + 1, users.size());
        users.add(user2);
        assertEquals(initialSize + 2, users.size());
    }

    @Test
    void testLoadUserHasFile() {
        users.add(userInFile);
        try {
            User loadedUser = users.loadUser(userInFile.getName());
            usersIdentical(userInFile, loadedUser);
        } catch (InvalidUserException e) {
            fail("user not found in file");
        }
    }

    @Test
    void testLoadUserNoFile() {
        users.add(user);
        try {
            User loadedUser = users.loadUser(user.getName());
            assertNull(loadedUser);
        } catch (InvalidUserException e) {
            fail("user not in users.json");
        }
    }

    // EFFECTS: tests if users are identical
    private void usersIdentical(User user1, User user2) {
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getLog(), user2.getLog());
        assertEquals(user1.getEnergyEntries(), user2.getEnergyEntries());
        assertEquals(user1.getFocusEntries(), user2.getFocusEntries());
        assertEquals(user1.getMotivationEntries(), user2.getMotivationEntries());
    }

    // MODIFIES: users
    // EFFECTS: empties given user list
    private void clearUsers(UserList users) {
        Set<String> names = new HashSet<>(users.getNames());
        for (String name : names) {
            assertTrue(users.remove(name));
        }
    }
}
