import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class displays the GUI for Editing Accounts
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class EditAccountPage implements Runnable {

    private User user;
    private JFrame frame = new JFrame("Edit Account");
    private JTextField textField = new JTextField("");
    private JButton submitButton = new JButton("Submit");
    String textLabel = "<html>" +
            "<font size='4' color='black'>" +
            "<strong> New Password </strong>" +
            "</font>" +
            "</div></html>";
    private JLabel titleLabel = new JLabel(textLabel);

    private JLabel newPasswordLabel = new JLabel("    ");
    private JButton backBtn = new JButton("Back");
    JPanel passwordPanel = new JPanel();
    JPanel topPanel = new JPanel();


    public EditAccountPage(User user) {
        this.user = user;
    }

    public void run() {
        frame.setSize(400, 300);
        frame.setMaximumSize(new Dimension(400, 300));

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);

    }

    public void placeComponents(JPanel panel) {

        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout());
        panel.setLayout(new GridLayout(4, 1));

        passwordPanel.setLayout(new GridBagLayout());
        topPanel.setLayout(new BorderLayout());

        backBtn.setPreferredSize(new Dimension(300, 50));
        submitButton.setPreferredSize(new Dimension(300, 50));
        backBtn.setPreferredSize(new Dimension(300, 50));


        backBtn.addActionListener(actionListener);
        submitButton.addActionListener(actionListener);

        topPanel.add(newPasswordLabel);
        panel.add(topPanel);

        textField.setPreferredSize(new Dimension(200, 50));
        passwordPanel.add(titleLabel);
        passwordPanel.add(textField);

        panel.add(passwordPanel);
        panel.add(submitButton);
        panel.add(backBtn);

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backBtn) {
                frame.dispose();
                SwingUtilities.invokeLater(new ChatsPage(user));
            }
            if (e.getSource() == submitButton) {
                String password = textField.getText();
                if (!password.isEmpty()) {
                    ProcessEditAccount processEditAccount = new ProcessEditAccount(password, user);
                    processEditAccount.start();
                } else {
                    Utility.showErrorMessage("Type a new password!");
                }
            }

        }
    };

}
