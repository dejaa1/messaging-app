import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This is the conversation page and displays all the messages
 * in a chat and is called when a chat is clicked on in the ChatsPage
 *
 * @author sf, an, ad, jc, av
 * @version 12/05/20
 */
public class ConversationPage implements Runnable {

    private JTextField messageField;
    private JButton sendButton = new JButton("Send");
    private JButton backBtn = new JButton("Back");
    private JButton refreshMessagesBtn = new JButton("Refresh");
    public JList messagesJList = new JList();
    private JScrollPane scrollPane = new JScrollPane();

    private JPanel panel = new JPanel();
    private User currentUser;
    private Chat currentChat;
    private ConversationPage conversationPage;
    Object[] options = {"Edit", "Delete", "Cancel"};

    private ArrayList<Message> chatMessages;

    public JFrame frame = new JFrame("Conversation");

    public ConversationPage(Chat currentChat, User currentUser) {
        this.currentChat = currentChat;
        this.currentUser = currentUser;
        this.chatMessages = currentChat.getMessages();
        this.conversationPage = this;
    }


    @Override
    public void run() {
        //setting up the frame
        frame.setSize(400, 420);
        frame.setMaximumSize(new Dimension(400, 420));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //setting up the panel
        placeComponents(panel);
        frame.add(panel);
        frame.setVisible(true);
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());

        ProcessGetNewMessages processGetNewMessages = new ProcessGetNewMessages(currentChat, scrollPane, this);
        processGetNewMessages.start();
    }

    ListSelectionListener listSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                try {
                    int selectedIndex = messagesJList.getSelectedIndex();

                    if (selectedIndex >= 0) {

                        String message = chatMessages.get(selectedIndex).getMessage().substring(
                                chatMessages.get(selectedIndex).getMessage().indexOf(':') + 2,
                                chatMessages.get(selectedIndex).getMessage().length());
                        String username = chatMessages.get(selectedIndex).getMessage().substring(0,
                                chatMessages.get(selectedIndex).getMessage().indexOf(':'));

                        if (chatMessages.get(selectedIndex).getUid().equals(currentUser.getUid())) {
                            int n = JOptionPane.showOptionDialog(null,
                                    "Select an action with a message:\n" + message,
                                    "Edit Message",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options, null);
                            if (n == 0) {
                                String messageWithoutUsername = JOptionPane.showInputDialog(
                                        null, "Enter a new message");
                                String messageWithUsername = "" + username + ": " + messageWithoutUsername;
                                if (messageWithoutUsername == null) {
                                    return;
                                }

                                ProcessEditMessage processEditMessage = new ProcessEditMessage(currentChat.getId(),
                                        messageWithUsername, selectedIndex);
                                processEditMessage.start();
                                try {
                                    processEditMessage.join();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }

                            }
                            if (n == 1) {
                                ProcessDeleteMessage processDeleteMessage =
                                        new ProcessDeleteMessage(currentChat.getId(), selectedIndex);
                                processDeleteMessage.start();
                                try {
                                    processDeleteMessage.join();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }

                        } else {
                            Utility.showInformationMessage("You can't edit or delete:\n" +
                                    message);
                        }
                        ProcessGetNewMessages processGetNewMessages =
                                new ProcessGetNewMessages(currentChat, scrollPane, conversationPage);
                        try {
                            processGetNewMessages.join();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        processGetNewMessages.start();
                        messagesJList.clearSelection();
                    }
                } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    System.out.print("");
                }
            }
        }
    };
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {
                if (!messageField.getText().isEmpty()) {
                    System.out.println("message has been added");

                    Message currentMessage = new Message("" + currentUser.getName() + ": " +
                            messageField.getText(), currentUser.getUid());
                    //adding message to arraylist of type message
                    chatMessages.add(currentMessage);

                    ArrayList<String> messagesArrayList = new ArrayList<>();
                    for (int i = 0; i < chatMessages.size(); i++) {
                        messagesArrayList.add(chatMessages.get(i).getMessage());
                    }


                    //sends the new message user added to the server
                    ProcessSendNewMessages processSendNewMessages =
                            new ProcessSendNewMessages(currentMessage, currentChat);
                    processSendNewMessages.start();


                    try {
                        processSendNewMessages.join();
                        ProcessGetNewMessages processGetNewMessages =
                                new ProcessGetNewMessages(currentChat, scrollPane, conversationPage);
                        processGetNewMessages.start();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }


                    messageField.setText("");
                }

            }
            if (e.getSource() == backBtn) {
                frame.dispose();
                SwingUtilities.invokeLater(new ChatsPage(currentUser));
            }
            if (e.getSource() == refreshMessagesBtn) {
                System.out.println("page has been refreshed");

                ProcessGetNewMessages processGetNewMessages =
                        new ProcessGetNewMessages(currentChat, scrollPane, conversationPage);
                processGetNewMessages.start();

            }
        }
    };

    public void placeComponents(JPanel jPanel) {

        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout());
        jPanel.setLayout(new BorderLayout());

        //setting up panel for back button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        backBtn.setPreferredSize(new Dimension(100, 40));
        refreshMessagesBtn.setPreferredSize(new Dimension(100, 40));

        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(refreshMessagesBtn, BorderLayout.EAST);
        jPanel.add(topPanel, BorderLayout.NORTH);

        //setting up panel for scroll pane
        JPanel scrollPanePanel = new JPanel();
        scrollPanePanel.setLayout(new BorderLayout());

        //setting up the scroll pane
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(330, 250));


        messagesJList = new JList(chatMessages.toArray());
        messagesJList.addListSelectionListener(listSelectionListener);

        scrollPane.setViewportView(messagesJList);


        scrollPanePanel.add(scrollPane);

        jPanel.add(scrollPanePanel);

        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(250, 45));

        sendButton.setPreferredSize(new Dimension(250, 45));

        //adding the message field
        //setting panel that contains scrollPane
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        jPanel.add(bottomPanel, BorderLayout.SOUTH);

        //setting up the send and back button
        sendButton.addActionListener(actionListener);
        backBtn.addActionListener(actionListener);
        refreshMessagesBtn.addActionListener(actionListener);

        //adding send and back buttons
        bottomPanel.add(messageField, BorderLayout.NORTH);
        bottomPanel.add(sendButton, BorderLayout.CENTER);
    }
    //accessors

    public void populateJList(ArrayList<String> messagesList, JScrollPane jScrollPane) {

        messagesJList.setListData(messagesList.toArray(new String[messagesList.size()]));
        JScrollBar vertical = jScrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<Message> getChatMessages() {
        return chatMessages;
    }

    public Chat getCurrentChat() {
        return currentChat;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    public void setChatMessages(ArrayList<Message> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
