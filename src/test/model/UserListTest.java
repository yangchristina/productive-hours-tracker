package model;
//!!! dunno how to test this without changing users.json

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//
//public class UserListTest {
//    private UserList users;
//    private User user;
//    private User user2;
//
//    @BeforeEach
//    void runBefore() {
//        users = new UserList();
//        user = new User("Tina");
//        user2 = new User("Chris");
//    }
//
//    @Test
//    void testConstructor() {
//        assertTrue(users.isEmpty());
//    }
//
//    @Test
//    void testAdd() {
//        users.add(user);
//        assertEquals(1, users.size());
//        assertEquals(user, users.getUserByName("Tina"));
//
//        users.add(user2);
//        assertEquals(2, users.size());
//        assertEquals(user2, users.getUserByName("Chris"));
//    }
//
//    @Test
//    void testGetUserByName() {
//        users.add(user);
//        assertEquals(user, users.getUserByName("Tina"));
//        assertNull(users.getUserByName("Chris"));
//    }
//
//    @Test
//    void testIsEmpty() {
//        assertTrue(users.isEmpty());
//        users.add(user);
//        assertFalse(users.isEmpty());
//
//        assertEquals(users.isEmpty(), users.getUsers().isEmpty());
//    }
//
//    @Test
//    void testSize() {
//        assertEquals(0, users.size());
//        users.add(user);
//        assertEquals(1, users.size());
//        users.add(user);
//        assertEquals(2, users.size());
//
//        assertEquals(users.size() ,users.getUsers().size());
//    }
//}
