package ui;

import model.User;
import model.UserList;
import model.exceptions.InvalidUserException;
import model.exceptions.UserAlreadyExistsException;
import persistence.JsonReadUserList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoggedOutGUI {
    private static final JLabel LABEL = new JLabel("Enter Text");
    private JTextField tf;
    private JButton login;
    private JButton register;
    private JPanel panel;
    private UserList users;
    private JFrame frame;

    public LoggedOutGUI() {
        JsonReadUserList reader = new JsonReadUserList();
        try {
            users = new UserList(reader.read()); //!!! read data here
        } catch (IOException e) {
            //maybe write ome?
            //when does this happen???, shouldn't ever happen cuz data/users.json (should) always exist
        }

        panel = new JPanel(); // the panel is not visible in output

        tf = new JTextField(10); // accepts up to 10 characters

        login = new JButton(new AbstractAction("Login") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(tf.getText());
                loginUser(tf.getText());
            }
        });

        register = new JButton(new AbstractAction("Register") {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser(tf.getText());
            }
        });

        addComponentsToPanel();

        frame = new JFrame("Testing");
        initializeFrame(frame, panel);
    }

    public void initializeFrame(JFrame frame, JPanel panel) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setSize(1200,900);
        frame.setVisible(true);
    }

    public void addComponentsToPanel() {
        panel.add(LABEL);
        panel.add(tf);
        panel.add(login);
        panel.add(register);
    }

    // REQUIRES: user is not yet in user list, name is not the empty string
    // MODIFIES: this
    // EFFECTS: adds user to end of user list
    public User registerUser(String name) { //put in UserList
        User user = new User(name);
        try {
            users.register(user);
        } catch (UserAlreadyExistsException e) {
            user = null;
            System.out.println("User already exists");
            System.out.println();
        }
        return user;
    }

    // EFFECTS: returns user with the given name, or null if there are no users with name in user list
    public void loginUser(String name) {
        try {
            startSession(users.loadUser(name));
        } catch (InvalidUserException e) { // create this exception
            System.out.println("User not found");
            JOptionPane.showMessageDialog(panel, "User not found");
        }
    }

    private void startSession(User user) {

        JFrame testFrame = new JFrame("test");
        JPanel testPanel = new JPanel();
        initializeFrame(testFrame, testPanel);

        frame.dispose();
    }
}
