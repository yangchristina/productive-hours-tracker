package model;

import model.exceptions.InvalidUserException;
import model.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReadUserList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserListTest {
    private static final UUID USER2_ID = UUID.randomUUID();

    private UserList users;
    private UserList emptyList;
    private User user;
    private User user2;
    private User userInFile;

    @BeforeEach
    void runBefore() {
        JsonReadUserList reader = new JsonReadUserList();
        try {
            users = new UserList(reader.read());
        } catch (IOException e) {
            fail("file missing");
        }

        emptyList = new UserList(new HashMap<>());

        user = new User("test user 1");
        user2 = new User("test user 2", USER2_ID, new ArrayList<>());

        userInFile = new User("testReaderEmptyUser", UUID.fromString("00000000-0000-0000-0000-000000000000"),
                new ArrayList<>());
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
        try {
            users.register(user);
            assertTrue(users.getNames().contains(user.getName()));
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
    void testIsEmpty() {
        assertTrue(emptyList.isEmpty());
        emptyList.add(user);
        assertFalse(emptyList.isEmpty());
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
        assertEquals(user1.getProductivityLog().getEntries(), user2.getProductivityLog().getEntries());
    }
}
