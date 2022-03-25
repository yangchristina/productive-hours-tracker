package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static final UUID USER_ID = UUID.randomUUID();

    private User user;
    private User user2;
    private ProductivityEntry energyEntry;
    private ProductivityEntry focusEntry;
    private ProductivityEntry motivationEntry;

    @BeforeEach
    void runBefore() {
        energyEntry = new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(), LocalTime.of(10, 0), 9);
        focusEntry = new ProductivityEntry(ProductivityEntry.Label.FOCUS, LocalDate.now(), LocalTime.of(13, 0), 8);
        motivationEntry = new ProductivityEntry(ProductivityEntry.Label.MOTIVATION, LocalDate.now(), LocalTime.of(15, 0), 10);

        user = new User("Chris");
        user2 = new User("Tina", USER_ID, createProductivityEntriesList());
    }

    @Test
    void testConstructorNoParams() {
        assertEquals("Chris", user.getName());
        assertNotNull(user.getId());
    }

    @Test
    void testConstructorWithParams() {
        assertEquals("Tina", user2.getName());
        assertEquals(USER_ID, user2.getId());
        assertEquals(3, user2.getProductivityLog().getEntries().size());
        assertEquals(energyEntry, user2.getProductivityLog().getEntries().get(0));
        assertEquals(focusEntry, user2.getProductivityLog().getEntries().get(1));
        assertEquals(motivationEntry, user2.getProductivityLog().getEntries().get(2));
    }

    private ArrayList<ProductivityEntry> createProductivityEntriesList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();
        list.add(energyEntry);
        list.add(focusEntry);
        list.add(motivationEntry);
        return list;
    }
}
