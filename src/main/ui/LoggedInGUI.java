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
    private User user;
    private boolean wasSaved;

    private JFrame frame;
    private JPanel panel;

    private JList entryList;
    private DefaultListModel<ProductivityEntry> listModel;

    public LoggedInGUI(User user) {
        this.user = user;
        initPanel();
        initEnergyList();
        initEntryButtons();
        initFrame();
    }

    private void initPanel() {
        panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //makes scrollable stop working
        JLabel label = new JLabel("Energy entries:");
        panel.add(label);
    }

    private void initEntryButtons() {
        JButton add = new JButton(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("add clicked");
                entryOptionForm();
            }
        });
        panel.add(add);

        JButton edit = new JButton(new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("edit clicked");
                // call edit
                //input where:
                // change level to:
                // change time to:
            }
        });
        panel.add(edit);

        JButton delete = new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("delete clicked");
                System.out.println(entryList.getSelectedIndex() + 1);
            }
        });
        panel.add(delete);
    }

    private void initEnergyList() {
//        String[] testList = user.listEntries(user.getEnergyEntries());
        listModel = new DefaultListModel<>();
        for (ProductivityEntry val : user.getEntries()) {
            listModel.addElement(val);
        }
        entryList = new JList(listModel);
        initList(entryList);
    }

    private void initList(JList list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(5);

        JScrollPane pane = new JScrollPane(list);
        pane.setPreferredSize(new Dimension(300, 80));
        panel.add(pane);
    }

    private void initFrame() {
        frame = new JFrame("Application");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,900);

        frame.add(panel);

        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                promptSave();
            }
        });
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

    private void entryOptionForm() {
        Object[] entryTypeOptions = {ProductivityEntry.Label.ENERGY, ProductivityEntry.Label.FOCUS,
                ProductivityEntry.Label.MOTIVATION};
        ProductivityEntry.Label entryType = (ProductivityEntry.Label) JOptionPane.showInputDialog(null,
                "Which energy type?", "Option", JOptionPane.QUESTION_MESSAGE, null, entryTypeOptions,
                ProductivityEntry.Label.ENERGY);
        if (entryType == null) {
            return;
        }

        Object[] levelOptions = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Integer level = (Integer) JOptionPane.showInputDialog(null, "Level?",
                "Option", JOptionPane.QUESTION_MESSAGE, null, levelOptions, 1);
        if (level == null) {
            return;
        }

        Object[] timeOptions = new Object[24];
        for (int i = 0; i < 24; i++) {
            timeOptions[i] = LocalTime.of(i, 0);
        }
        LocalTime time = (LocalTime) JOptionPane.showInputDialog(null, "Time?",
                "Option", JOptionPane.QUESTION_MESSAGE, null, timeOptions, timeOptions[0]);
        if (time == null) {
            return;
        }

        addEntries(entryType, level, time);
    }

    // MODIFIES: this
    // EFFECTS: removes selected entry from the productivity log
    public void removeEntry(ProductivityEntry entry) {
        System.out.println("Operation: remove");
        boolean isRemoved = user.remove(entry);

        if (isRemoved) {
            System.out.println("Removed " + entry.toString());
        }
    }

    public boolean wasSaved() {
        return wasSaved;
    }

    // MODIFIES: this
    // EFFECTS: lets the user enter values to create a new productivity entry, and add it  to the user's log
    private void addEntries(ProductivityEntry.Label label, int level, LocalTime time) {
        LocalDate date = LocalDate.now();

        ProductivityEntry newEntry;
        switch (label) {
            case ENERGY:
                newEntry = new EnergyEntry(date, time, level);
                break;
            case FOCUS:
                newEntry = new FocusEntry(date, time, level);
                break;
            default:
                newEntry = new MotivationEntry(date, time, level);
        }
        user.add(newEntry);
        listModel.addElement(newEntry);
    }
}
