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

// the application page for a user before they log on. Allows them to login or register
public class LoggedOutGUI {

    private JTextField tf;
    private JPanel panel;
    private UserList users;
    private JFrame frame;

    // EFFECTS: constructs a loggedOutGUI
    public LoggedOutGUI() {
        initUserList();
        initPanel();
        initFrame();
    }

    // EFFECTS: constructs a loggedOutGUI with a given users
    public LoggedOutGUI(UserList users) {
        this.users = users;
        initPanel();
        initFrame();
    }

    // MODIFIES: this
    // EFFECTS: reads the userList from file and assigns it to users
    private void initUserList() {
        JsonReadUserList reader = new JsonReadUserList();
        try {
            users = new UserList(reader.read()); //!!! read data here
        } catch (IOException e) {
            // shouldn't ever happen cuz data/users.json (should) always exist, maybe rewrite user.json
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes panel
    private void initPanel() {
        panel = new JPanel(); // the panel is not visible in output
        initInput();
        initButtons();
    }

    // MODIFIES: this
    // EFFECTS: adds input to panel
    private void initInput() {
        JLabel label = new JLabel("Enter Text");
        tf = new JTextField(10); // accepts up to 10 characters
        panel.add(label);
        panel.add(tf);
    }

    // MODIFIES: this
    // EFFECTS: adds login and register buttons to panel
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

    // MODIFIES: this
    // EFFECTS: initializes frame and adds panel to it
    private void initFrame() {
        frame = new JFrame("Login page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,900);

        frame.add(panel);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: registers a user if user is not already in users, else show message to try again
    private void registerUser(String name) { //put in UserList
        User user = new User(name);
        try {
            users.register(user);
            startSession(user);
        } catch (UserAlreadyExistsException e) {
            JOptionPane.showMessageDialog(panel, "Name taken, please choose a different name");
        }
    }

    // EFFECTS: logs in user with given name or shows message if no user with name in users
    private void loginUser(String name) {
        try {
            startSession(users.loadUser(name));
        } catch (InvalidUserException e) { // create this exception
            JOptionPane.showMessageDialog(panel, "User not found");
        }
    }

    // MODIFIES: this
    // EFFECTS: switches to the loggedInGUI frame and disposes current frame
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
