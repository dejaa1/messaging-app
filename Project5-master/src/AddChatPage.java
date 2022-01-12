
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This displays the GUI for creating individual and group chats
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class AddChatPage extends JComponent implements Runnable {

    private JButton submitButton = new JButton("Submit");

    String leftText = "<html>" +
            "<font size='5' color='black'>" +
            "<strong> All Users </strong>" +
            "</font>" +
            "</html>";
    String rightText = "<html>" +
            "<font size='5' color='black'>" +
            "<strong>Selected Users</strong>" +
            "</font>" +
            "</html>";

    private JLabel titleLabel = new JLabel(leftText);

    private JLabel titleLabel2 = new JLabel(rightText);

    private ArrayList<String> allUsersList = new ArrayList<>();
    private ArrayList<String> chosenUsersList = new ArrayList<>();

    private JList<String> allUsersJList = new JList<>();
    private JList<String> chosenUsersJList = new JList<>();

    private AddChatPage chats;
    private JPanel panel = new JPanel();
    private JScrollPane allUsersScrollPane = new JScrollPane();
    private JScrollPane chosenUsersScrollPane = new JScrollPane();
    private JButton backbtn = new JButton("Back");
    private JButton clearBtn = new JButton("Clear");

    private ArrayList<String> usernames;
    private User user;
    private ArrayList<User> userArrayList;


    public JFrame frame = new JFrame("New Chat");
    private ChatsPage chatsPage;


    public AddChatPage(User user, ChatsPage chatsPage) {
        this.chatsPage = chatsPage;
        chats = this;
        this.user = user;
    }


    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backbtn) {
                SwingUtilities.invokeLater(new ChatsPage(user));
                frame.dispose();
                chosenUsersList.clear();
                chosenUsersJList = new JList(chosenUsersList.toArray());
                chosenUsersScrollPane.setViewportView(chosenUsersJList);
            }

            if (e.getSource() == clearBtn) {
                chosenUsersList.clear();
                chosenUsersJList = new JList(chosenUsersList.toArray());
                chosenUsersScrollPane.setViewportView(chosenUsersJList);

            }
            if (e.getSource() == submitButton) {
                //converts string of users to ArrayList of users
                if (chosenUsersList.isEmpty()) {
                    Utility.showErrorMessage("You have not added any users. Please add a user to create a chat!");
                } else {
                    int counter = 0;
                    ArrayList<User> userss = getUserObj(chosenUsersList);

                    for (Chat chat : chatsPage.getChatArrayList()) {
                        if (chat.getUsers().equals(userss)) {
                            Utility.showErrorMessage("You already have a chat with this user");
                            return;
                        }
                        if (chat.getUsers().size() == userss.size()) {

                            for (User usertest : userss) {
                                if (chat.getUsers().contains(usertest)) {

                                    counter++;
                                }
                            }
                            if (counter == userss.size()) {
                                Utility.showErrorMessage("You already have a chat with this user");
                                return;
                            }
                        }
                    }


                    boolean groupchat = false;
                    if (chosenUsersList.size() > 1) {
                        groupchat = true;
                    }
                    ArrayList<Message> messages = new ArrayList<>();
                    ArrayList<Chat> chats2 = new ArrayList<>();
                    Chat chatToAdd = new Chat(messages, getUserObj(chosenUsersList), groupchat);
                    ProcessAddNewChat processAddNewChat = new ProcessAddNewChat(user, chatToAdd);
                    processAddNewChat.start();

                    chosenUsersList.clear();
                    chosenUsersJList = new JList(chosenUsersList.toArray());
                    chosenUsersScrollPane.setViewportView(chosenUsersJList);
                    chats2.add(chatToAdd);
                    chatsPage.setChatArrayList(chats2);
                }


            }

        }
    };


    private ArrayList<User> getUserObj(ArrayList<String> chosenUsersList2) {
        ArrayList<User> usersToAdd = new ArrayList<>();
        for (String username : chosenUsersList2) {
            for (User user2 : userArrayList) {
               /*if the username matches a user object, then the user object is added an
               the arraylist
               */
                if (user2.getUsername().equals(username)) {
                    usersToAdd.add(user2);
                    break;
                }
            }


        }
        //adds the current user to the list of users
        usersToAdd.add(user);
        return usersToAdd;
    }


    public void placeComponents(JPanel panel2) {
        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout());
        panel2.setLayout(new BorderLayout());

        //setting up panel for back button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        titleLabel.setSize(15, 10);


        topPanel.add(titleLabel, BorderLayout.CENTER);

        topPanel.add(titleLabel2, BorderLayout.EAST);
        panel2.add(topPanel, BorderLayout.NORTH);


        //setting up panel for scroll pane
        JPanel scrollPanePanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        gridLayout.setHgap(10);
        scrollPanePanel.setLayout(gridLayout);

        //setting up the scroll pane
        allUsersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        allUsersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        allUsersScrollPane.setPreferredSize(new Dimension(160, 200));

        chosenUsersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chosenUsersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chosenUsersScrollPane.setPreferredSize(new Dimension(160, 200));

        allUsersJList = new JList(allUsersList.toArray());

        allUsersScrollPane.setViewportView(allUsersJList);
        chosenUsersScrollPane.setViewportView(chosenUsersJList);


        //sets the scroll bar to the very bottom when new messages are added
        allUsersScrollPane.getVerticalScrollBar().setValue(allUsersScrollPane.getHorizontalScrollBar().getMinimum());
        chosenUsersScrollPane.getVerticalScrollBar().setValue(allUsersScrollPane.getHorizontalScrollBar().getMinimum());

        scrollPanePanel.add(allUsersScrollPane);
        scrollPanePanel.add(chosenUsersScrollPane);

        panel2.add(scrollPanePanel, BorderLayout.EAST);
        panel2.add(scrollPanePanel, BorderLayout.WEST);


        //adding the message field
        //setting panel that contains scrollPane
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3, 1));
        panel2.add(bottomPanel, BorderLayout.SOUTH);

        clearBtn.setPreferredSize(new Dimension(305, 50));
        submitButton.setPreferredSize(new Dimension(305, 50));
        backbtn.setPreferredSize(new Dimension(305, 50));

        //adding send and back buttons
        bottomPanel.add(clearBtn);
        bottomPanel.add(submitButton);
        bottomPanel.add(backbtn);

        backbtn.addActionListener(actionListener);
        submitButton.addActionListener(actionListener);
        clearBtn.addActionListener(actionListener);


    }

    @Override
    public void run() {
        frame.setSize(400, 410);
        frame.setMaximumSize(new Dimension(400, 410));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        placeComponents(panel);
        frame.add(panel);
        frame.setVisible(true);
        ProcessGetUserList pgl = new ProcessGetUserList(this);
        pgl.start();

        allUsersJList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    try {
                        //add username to chosen usernames list
                        int selectedIndex = allUsersJList.getSelectedIndex();
                        if (chosenUsersList.contains(allUsersList.get(selectedIndex))) {
                            Utility.showInformationMessage("This user has already been added!");
                        } else {
                            chosenUsersList.add(allUsersList.get(selectedIndex));
                            //update view for chosen usernames
                            chosenUsersJList = new JList(chosenUsersList.toArray());
                            chosenUsersScrollPane.setViewportView(chosenUsersJList);
                        }

                        System.out.println(selectedIndex);
                        allUsersJList.clearSelection();
                    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                            System.out.println("Array out of bounds");
                    }

                }
            }
        });


    }

    public void populateList(ArrayList<User> users) {
        this.userArrayList = users;
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < users.size(); i++) {
            names.add(i, users.get(i).getUsername());
        }
        //removes the current user
        names.remove(user.getUsername());
        allUsersList = names;
        allUsersJList.setListData(names.toArray((new String[names.size()])));
    }

    //accessors

    public AddChatPage getChats() {
        return chats;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }
}
