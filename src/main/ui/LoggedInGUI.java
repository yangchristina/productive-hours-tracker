package ui;

import model.*;

import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

// application page for a user that has logged in
public class LoggedInGUI {
    private static final Object[] ENTRY_TYPE_OPTIONS = {ProductivityEntry.Label.ENERGY, ProductivityEntry.Label.FOCUS,
            ProductivityEntry.Label.MOTIVATION};
    private static final Object[] LEVEL_OPTIONS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    private static final Object[] TIME_OPTIONS = createTimeOptions();

    private User user;
    private UserList userList;
    private boolean wasSaved;

    private JFrame frame;
    private JPanel listPanel;
    private GraphPanel graphPanel;
    private JMenuBar menuBar;

    private JList<ListModel<ProductivityEntry>> entryList;
    private DefaultListModel<ProductivityEntry> listModel;

    // EFFECTS: Constructs a gui for a logged in user, with a given user and user list
    public LoggedInGUI(User user, UserList users) {
        this.user = user;
        this.userList = users;
        initListPanel();
        initMenuBar();
        initGraph();
        initEntryList();
        initAddEntryButton();
        initEditEntryButton();
        initDeleteEntryButton();
        initFrame();
    }

    // MODIFIES: this
    // EFFECTS: initiates the graphPanel with the logged-in user's dailyAverageLog
    private void initGraph() {
        graphPanel =  new GraphPanel(user.getProductivityLog().getDailyAverageLog().getLog());
    }

    // MODIFIES: this
    // EFFECTS: initiates the listPanel with an empty JPanel that has a label
    private void initListPanel() {
        listPanel = new JPanel();
        JLabel label = new JLabel("Entries:");
        listPanel.add(label);
    }

    // MODIFIES: this
    // EFFECTS: constructs the menu bar with options for save and logout under FILE
    private void initMenuBar() {
        menuBar = new JMenuBar();

        JMenu m1 = new JMenu("FILE");
        menuBar.add(m1);

        JMenuItem m11 = new JMenuItem("Logout");
        m11.addActionListener(ev -> {
            endUserSession();
            new LoggedOutGUI(userList);
            frame.dispose();
        });

        JMenuItem m22 = new JMenuItem("Save");
        m22.addActionListener(ev -> {
            save();
            System.out.println("saved :)");
        });
        m1.add(m11);
        m1.add(m22);
    }

    // MODIFIES: this
    // EFFECTS: update the graph to new dailyAverageLog values
    private void updateGraph() {
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which adds an entry on click and updates the graph for the new entry
    private void initAddEntryButton() {
        JButton add = new JButton(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductivityEntry entry = createDefaultEntry();
                if (entryOptionForm(entry)) { // true means added, false means not added
                    user.getProductivityLog().add(entry);
                    listModel.addElement(entry);
                    updateGraph();
                }
            }
        });
        listPanel.add(add);
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which edits selected entry on click and updates the graph
    private void initEditEntryButton() {
        JButton edit = new JButton(new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductivityEntry selected = (ProductivityEntry) entryList.getSelectedValue();
                if (selected != null) {
                    ProductivityEntry old = new ProductivityEntry(selected.getLabel(), selected.getDate(),
                            selected.getTime(), selected.getLevel());
                    if (entryOptionForm(selected)) {
                        user.getProductivityLog().getDailyAverageLog().remove(old);
                        user.getProductivityLog().getDailyAverageLog().add(selected);
                        JOptionPane.showMessageDialog(listPanel,"Entry changed to: " + selected);
                        updateGraph();
                    }
                }
            }
        });
        listPanel.add(edit);
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which deletes selected entry on click, and updates graph
    private void initDeleteEntryButton() {
        JButton delete = new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductivityEntry selected = (ProductivityEntry) entryList.getSelectedValue();
                if (selected != null) { // null if after removing no average level for that time
                    user.getProductivityLog().remove(selected);
                    JOptionPane.showMessageDialog(listPanel, "Removed: " + selected);
                    listModel.removeElement(selected);
                    updateGraph();
                }
            }
        });
        listPanel.add(delete);
    }

    // MODIFIES: this
    // EFFECTS: initializes entry list with all entries from user's productivity log
    private void initEntryList() {
        listModel = new DefaultListModel<>();
        for (ProductivityEntry val : user.getProductivityLog().getEntries()) {
            listModel.addElement(val);
        }
        entryList = new JList(listModel);
        initList(entryList);
    }

    // MODIFIES: this
    // EFFECTS: initializes layout of listPanel and makes it scrollable
    private void initList(JList<ListModel<ProductivityEntry>> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(5);

        JScrollPane pane = new JScrollPane(list);
        pane.setPreferredSize(new Dimension(300, 80));
        listPanel.add(pane);
    }

    // MODIFIES: this
    // EFFECTS: initializes JFrame and adds panels to it
    private void initFrame() {
        frame = new JFrame("Application");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,900);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, listPanel);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, graphPanel);

        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                endUserSession();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: calls promptSave method and saves user to userList if wasSaved is true
    private void endUserSession() {
        promptSave();
        if (wasSaved) {
            saveUserList();
        }
    }

    // EFFECTS: asks user if they would like to save their session and saves if they answer yes
    private void promptSave() {
        if (JOptionPane.showConfirmDialog(frame, "Would you like to save?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            save();
        }
    }

    // EFFECTS: saves user to file
    private void save() {
        try {
            JsonWriter writer = new JsonWriter(user.getId().toString());

            writer.open();
            writer.write(user);
            writer.close();

            wasSaved = true;
        } catch (IOException e) {
            // idk what yet, shouldn't every be thrown cuz no illegal file names, all filenames are uuid
        }
    }

    // EFFECTS: shows form where user can input select changes to entry
    //          returns false if user cancels form, true otherwise
    private boolean entryOptionForm(ProductivityEntry entry) {
        ProductivityEntry.Label entryType = (ProductivityEntry.Label) JOptionPane.showInputDialog(null,
                "Which energy type?", "Option", JOptionPane.QUESTION_MESSAGE, null, ENTRY_TYPE_OPTIONS,
                entry.getLabel());
        if (entryType == null) {
            return false;
        }

        Integer level = (Integer) JOptionPane.showInputDialog(null, "Level?",
                "Option", JOptionPane.QUESTION_MESSAGE, null, LEVEL_OPTIONS, entry.getLevel());
        if (level == null) {
            return false;
        }

        LocalTime time = (LocalTime) JOptionPane.showInputDialog(null, "Time?",
                "Option", JOptionPane.QUESTION_MESSAGE, null, TIME_OPTIONS, entry.getTime());
        if (time == null) {
            return false;
        }

        entry.setLabel(entryType);
        entry.setLevel(level);
        entry.setTime(time);

        return true;
    }

    // EFFECTS: returns a default energy entry with a label of ENERGY, today's date, current time of day and level of 5
    private ProductivityEntry createDefaultEntry() {
        return new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(),
                LocalTime.of(LocalTime.now().getHour(), 0),5);
    }

    // EFFECTS: saves user list to file
    public void saveUserList() {
        try {
            JsonWriter writer = new JsonWriter("users");

            writer.open();
            writer.write(userList);
            writer.close();
        } catch (IOException e) {
            // exception should never be thrown
        }
    }

    // EFFECTS: returns a time options object with an entry for each hour of the day [00:00-23:00]
    private static Object[] createTimeOptions() {
        Object[] timeOptions = new Object[24];
        for (int i = 0; i < 24; i++) {
            timeOptions[i] = LocalTime.of(i, 0);
        }
        return timeOptions;
    }
}
