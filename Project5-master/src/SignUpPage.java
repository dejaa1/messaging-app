import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

/**
 * This the GUI for Signing up
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class SignUpPage extends JComponent implements Runnable {

    //UI elements for signUp page
    private JButton signUpBtn = new JButton("Sign Up");
    private JButton backBtn = new JButton("Back");

    private JTextField passwordField = new JTextField("Password");
    private JTextField usernameField = new JTextField("Username");
    private JTextField nameField = new JTextField("Name");
    String titleLabel = "<html>" +
            "<font size='6' color='black'>" +
            "<strong> Sign Up </strong>" +
            "</font>" +
            "</html>";
    private JLabel signUpTxt = new JLabel(titleLabel, SwingConstants.CENTER);

    public JFrame frame = new JFrame("Sign Up");

    public SignUpPage() {

    }


    /**
     * creates a panel with items on it
     *
     * @param panel
     */
    public void placeComponents(JPanel panel) {

        Container content = frame.getContentPane();

        content.setLayout(new FlowLayout());
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));

        ButtonListener buttonListener = new ButtonListener();

        backBtn.addActionListener(buttonListener);
        signUpBtn.addActionListener(buttonListener);

        signUpBtn.setPreferredSize(new Dimension(200, 50));
        backBtn.setPreferredSize(new Dimension(200, 50));

        usernameField.setPreferredSize(new Dimension(200, 50));
        passwordField.setPreferredSize(new Dimension(200, 50));
        nameField.setPreferredSize(new Dimension(200, 50));


        mainPanel.add(signUpTxt, CENTER_ALIGNMENT);
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(nameField);
        mainPanel.add(signUpBtn);
        mainPanel.add(backBtn);
        panel.add(mainPanel);
    }

    @Override
    public void run() {
        frame.setSize(400, 370);
        frame.setMaximumSize(new Dimension(400, 370));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    /**
     * button listener for any button on the page
     */
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signUpBtn) {
                if (usernameField.getText().isEmpty() ||
                        passwordField.getText().isEmpty() || nameField.getText().isEmpty()) {
                    Utility.showErrorMessage("Either password, username, or name is empty");
                } else {
                    try {
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        String name = nameField.getText();
                        StringBuilder sb = new StringBuilder();
                        Random random = new Random();
                        int idNumber = random.nextInt(1000000);
                        //creates a unique id containing the first letter of a user's username,
                        // a random idNumber, and the first letter of a user's name
                        sb.append(username.charAt(0)).append(idNumber).append(name.charAt(0));

                        User user = new User(username, password, name, sb.toString());

                        processSignup(user);

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
     * sends all of the sign up data to the server, which checks if this user already exists (handleSignup method)
     */
    public void processSignup(User user) throws IOException {
        System.out.println("--processing Sign Up for user...");
        ProcessSignUp processSignUp = new ProcessSignUp(user, frame);
        processSignUp.start();

    }
}
