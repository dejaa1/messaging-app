import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the LAUNCH page of the app and displays a GUI
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class HomePage extends JComponent implements Runnable {

    private JButton signInBtn = new JButton("Sign In");
    private JButton signUpBtn = new JButton("Sign Up");

    String titleText = "<html>" +
            "<font size='6' color='black'>" +
            "<strong> Chat Application </strong>" +
            "</font>" +
            "</html>";
    JLabel titleLabel = new JLabel(titleText);
    JLabel emptyText = new JLabel("   ");

    public JFrame frame = new JFrame("Chat Application");

    HomePage() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new HomePage());
    }


    @Override
    public void run() {
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);

    }
    /**
     * This is the ButtonListener class
     *
     * @author sf, an, ad, jc, av
     * @version 12-05-20
     */
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signInBtn) {
                //goes to sign in page
                SwingUtilities.invokeLater(new LoginPage());
                frame.dispose();
                System.out.println("-----sign in page was launched-----");
            }
            if (e.getSource() == signUpBtn) {
                //goes to sign up page
                SwingUtilities.invokeLater(new SignUpPage());
                frame.dispose();
                System.out.println("-----sign up page was launched-----");
            }
        }

    }


    private void placeComponents(JPanel panel) {
        Container content = frame.getContentPane();

        content.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel(new GridLayout(0, 1));

        ButtonListener buttonListener = new ButtonListener();


        titleLabel.setSize(100, 50);

        signInBtn.addActionListener(buttonListener);
        signUpBtn.addActionListener(buttonListener);

        signInBtn.setPreferredSize(new Dimension(200, 50));
        signUpBtn.setPreferredSize(new Dimension(200, 50));

        mainPanel.add(emptyText);
        mainPanel.add(titleLabel);
        mainPanel.add(signInBtn);
        mainPanel.add(signUpBtn);

        content.add(mainPanel);

    }

    public HomePage getHomePage() {
        return this;
    }


}
