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

    private void initGraph() {
        graphPanel =  new GraphPanel(user.getProductivityLog().getDailyAverageLog().getLog());
    }

    private void initListPanel() {
        listPanel = new JPanel();
        JLabel label = new JLabel("Energy entries:");
        listPanel.add(label);
    }


    private void initMenuBar() {
        menuBar = new JMenuBar();

        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        menuBar.add(m1);
        menuBar.add(m2);

        JMenuItem m11 = new JMenuItem("Logout");
        m11.addActionListener(ev -> {
            endUserSession();
            new LoggedOutGUI();
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
    // EFFECTS: adds button to panel which adds an entry on click
    private void initAddEntryButton() {
        JButton add = new JButton(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("add clicked");
                ProductivityEntry entry = createDefaultEntry();
                if (entryOptionForm(entry)) { // true means added, false means not added
                    user.getProductivityLog().add(entry);
                    listModel.addElement(entry);
                    graphPanel.revalidate();
                    graphPanel.repaint();
                }
            }
        });
        listPanel.add(add);
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which edits selected entry on click
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
                        graphPanel.revalidate();
                        graphPanel.repaint();
                    }
                }
            }
        });
        listPanel.add(edit);
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which deletes selected entry on click
    private void initDeleteEntryButton() {
        JButton delete = new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductivityEntry selected = (ProductivityEntry) entryList.getSelectedValue();

                if (selected != null) { // null if after removing no average level for that time
                    user.getProductivityLog().remove(selected);
                    JOptionPane.showMessageDialog(listPanel, "Removed: " + selected);
                    listModel.removeElement(selected);
                    graphPanel.revalidate();
                    graphPanel.repaint(); //!!! not being updated
                }
            }
        });
        listPanel.add(delete);
    }

    // MODIFIES: this
    // EFFECTS: initializes entry list
    private void initEntryList() {
        listModel = new DefaultListModel<>();
        for (ProductivityEntry val : user.getProductivityLog().getEntries()) {
            listModel.addElement(val);
        }
        entryList = new JList(listModel);
        initList(entryList);
    }

    private void initList(JList<ListModel<ProductivityEntry>> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(5);

        JScrollPane pane = new JScrollPane(list);
        pane.setPreferredSize(new Dimension(300, 80));
        listPanel.add(pane);
    }

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

    private void endUserSession() {
        promptSave();
        if (wasSaved) {
            System.out.println("was saved, now saving user list");
            saveUserList();
        }
    }

    // asks user if they would like to save their session
    // EFFECTS: if user inputs true, save session, if false don't save, else ask again
    private void promptSave() {
        System.out.println("Would you like to save?");
        if (JOptionPane.showConfirmDialog(frame, "Would you like to save?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // yes option
            save();
            System.out.println("You said yes");
        } else {
            // no option
            System.out.println("not saved");
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

        entry.editLabel(entryType);
        entry.editLevel(level);
        entry.editTime(time);

        return true;
    }

    // MODIFIES: this
    // EFFECTS: removes selected entry from the productivity log
    public void removeEntry(ProductivityEntry entry) {
        System.out.println("Operation: remove");
        user.getProductivityLog().remove(entry);

        System.out.println("Removed: " + entry.toString());
    }

    // EFFECTS: creates a default energy entry with a label of ENERGY, today's date, time of 0:0 and level of 0.
    private ProductivityEntry createDefaultEntry() {
        return new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(),
                LocalTime.of(LocalTime.now().getHour(), 0),5);
    }

    private static Object[] createTimeOptions() {
        Object[] timeOptions = new Object[24];
        for (int i = 0; i < 24; i++) {
            timeOptions[i] = LocalTime.of(i, 0);
        }
        return timeOptions;
    }

    // EFFECTS: saves user list to file
    public void saveUserList() {
        try {
            JsonWriter writer = new JsonWriter("users");

            writer.open();
            writer.write(userList);
            writer.close();
        } catch (IOException e) {
            System.out.println("fight ");
        }
    }



//    // EFFECTS: shows the user's peak hours for either focus, energy, or motivation, depending on the user's input
//    private void showPeakHours(ProductivityEntry.Label label) {
//        ArrayList<LocalTime> peakHours = user.getPeaksAndTroughs(label).get("peak");
//        if (peakHours.isEmpty()) {
//            System.out.println("Not enough " + label + " entries");
//        } else {
//            System.out.println("Your peak " + label + " hours are at " + peakHours);
//        }
//    }
//
//    // EFFECTS: shows the user's peak hours for either focus, energy, or motivation, depending on the user's input
//    private void showTroughHours() {
//        String label = input.entryType();
//        ArrayList<LocalTime> troughHours = user.getPeaksAndTroughs(label).get("trough");
//        if (troughHours.isEmpty()) {
//            System.out.println("Not enough " + label + " entries");
//        } else {
//            System.out.println("Your trough " + label + " hours are at " + troughHours);
//        }
//    }
}
