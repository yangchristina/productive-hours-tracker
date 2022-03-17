package ui;

import model.User;
import model.UserList;
import model.exceptions.InvalidUserException;
import model.exceptions.UserAlreadyExistsException;
import persistence.JsonReadUserList;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoggedOutGUI {

    private JTextField tf;
    private JPanel panel;
    private UserList users;
    private JFrame frame;

    public LoggedOutGUI() {
        initUserList();
        initPanel();
        initFrame();
    }

    public LoggedOutGUI(UserList users) {
        this.users = users;
        initPanel();
        initFrame();
    }

    private void initUserList() {
        JsonReadUserList reader = new JsonReadUserList();
        try {
            users = new UserList(reader.read()); //!!! read data here
        } catch (IOException e) {
            // shouldn't ever happen cuz data/users.json (should) always exist, maybe rewrite user.json
        }
    }

    private void initPanel() {
        panel = new JPanel(); // the panel is not visible in output
        initInput();
        initButtons();
    }

    private void initInput() {
        JLabel label = new JLabel("Enter Text");
        tf = new JTextField(10); // accepts up to 10 characters
        panel.add(label);
        panel.add(tf);
    }

    private void initButtons() {
        JButton login = new JButton(new AbstractAction("Login") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(tf.getText());
                loginUser(tf.getText());
            }
        });
        panel.add(login);

        JButton register = new JButton(new AbstractAction("Register") {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser(tf.getText());
            }
        });
        panel.add(register);
    }

    private void initFrame() {
        frame = new JFrame("Login page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,900);

        frame.add(panel);
        frame.setVisible(true);
    }

    // REQUIRES: user is not yet in user list, name is not the empty string
    // MODIFIES: this
    // EFFECTS: adds user to end of user list
    private void registerUser(String name) { //put in UserList
        User user = new User(name);
        try {
            users.register(user);
            startSession(user);
        } catch (UserAlreadyExistsException e) {
            System.out.println("User already exists");
            JOptionPane.showMessageDialog(panel, "Name taken, please choose a different name");
        }
    }

    // EFFECTS: returns user with the given name, or null if there are no users with name in user list
    private void loginUser(String name) {
        try {
            startSession(users.loadUser(name));
        } catch (InvalidUserException e) { // create this exception
            System.out.println("User not found");
            JOptionPane.showMessageDialog(panel, "User not found");
        }
    }

    private void startSession(User user) {
        new LoggedInGUI(user, users);
        frame.dispose();
    }

    // EFFECTS: saves user list to file
    public void save() {
        try {
            JsonWriter writer = new JsonWriter("users");

            writer.open();
            writer.write(users);
            writer.close();
        } catch (IOException e) {
            System.out.println("fight ");
        }
    }
}
