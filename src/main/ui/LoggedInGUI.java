package ui;

import model.User;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LoggedInGUI {
    private User user;
    private boolean wasSaved;

    private JFrame frame;
    private JPanel panel;

    private JList energyList;

    public LoggedInGUI(User user) {

        this.user = user;
        initPanel();
        initEnergyList();
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
                // make an input show up???
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
    }

    private void initEnergyList() {
        String[] testList = {"logout", "add", "peak", "trough", "show", "edit", "delete", "save", "help"};

        energyList = new JList(testList);
        initList(energyList);
    }

    private void initList(JList list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(5);

        JScrollPane pane = new JScrollPane(list);
        pane.setPreferredSize(new Dimension(250, 80));
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
}
