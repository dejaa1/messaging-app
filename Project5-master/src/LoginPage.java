

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This is the LoginPage and displays the GUI for logging in
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class LoginPage extends JComponent implements Runnable {
    private JTextField usernameField; //textField for username
    private JTextField passwordField; //textField for password
    private JButton loginBtn = new JButton("Sign In");
    private JButton backBtn;
    public JFrame frame = new JFrame("Sign In");
    String titleLabel = "<html>" +
            "<font size='6' color='black'>" +
            "<strong> Sign In </strong>" +
            "</font>" +
            "</html>";
    private JLabel signInText = new JLabel(titleLabel, SwingConstants.CENTER);

    public LoginPage() {

    }


    @Override
    public void run() {
        frame.setSize(400, 350);
        frame.setMaximumSize(new Dimension(400, 350));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);

    }

    /**
     * creates a panel with items on it
     *
     * @param panel
     */
    public void placeComponents(JPanel panel) {
        Container content = frame.getContentPane();

        content.setLayout(new FlowLayout());

        usernameField = new JTextField("Username");
        usernameField.setPreferredSize(new Dimension(200, 50));
        passwordField = new JTextField("Password");
        passwordField.setPreferredSize(new Dimension(200, 50));
        signInText.setSize(100, 50);

        loginBtn = new JButton("Login");
        backBtn = new JButton("Back");

        ButtonListener buttonListener = new ButtonListener();

        loginBtn.setPreferredSize(new Dimension(200, 50));
        backBtn.setPreferredSize(new Dimension(200, 50));

        loginBtn.addActionListener(buttonListener);
        backBtn.addActionListener(buttonListener);

        //setting gridLayout allows each item to be below each other
        panel.setLayout(new GridLayout(0, 1));
        panel.add(signInText);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(backBtn);
    }

    /**
     * button listener for any button on the page
     */
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginBtn) {
                if (usernameField.getText().isEmpty() ||
                        passwordField.getText().isEmpty()) {
                    Utility.showErrorMessage("Please enter your username and password!");
                } else {
                    try {
                        User user = new User(usernameField.getText(), passwordField.getText());
                        processLogin(user);
                        System.out.println("----chats page was launched----");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

            }
            if (e.getSource() == backBtn) {
                frame.dispose();
                SwingUtilities.invokeLater(new HomePage());

            }

        }

    }

    /**
     * calls the Threads.ProcessLogin class, which starts a thread to handle logging in on the server
     */
    private void processLogin(User user) throws IOException {
        System.out.println("--processing Login for user...");
        ProcessLogin processLogin = new ProcessLogin(user, frame);
        processLogin.start();
    }

}
