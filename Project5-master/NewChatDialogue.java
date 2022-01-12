import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewChatDialogue {
    private JPanel panel1;
    private JTextPane newChatDialogueTextPane;
    private JTextField typeUsernameHereTextField;
    private JTextField textField2;
    private JButton searchForUserButton;
    private JButton finalizeGroupButton;
    private JButton addThisUserButton;

    public NewChatDialogue() {
        searchForUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        finalizeGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addThisUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
