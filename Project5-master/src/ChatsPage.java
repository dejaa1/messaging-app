import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is the ChatsPage GUI and displays all of the user's chats(group and individual)
 * @author sf,an,ad,jc,av
 * @version 12-05-20
 */
public class ChatsPage extends JComponent implements Runnable {


    private User user;
    private JLabel label;
    public JFrame frame = new JFrame("Chat Application");
    private JList jlist = new JList();
    private JButton addChatButton;
    private JButton deleteAccountButton = new JButton("Delete Account");

    private JButton signOutButton = new JButton("Sign Out");
    private ArrayList<Chat> chatArrayList = new ArrayList<>();
    private JButton refreshChatsBtn = new JButton("Refresh");
    private JButton editAccountBtn = new JButton("Edit Account");
    private JButton darkThemeButton = new JButton("Dark Theme");
    JScrollPane jScrollPane = new JScrollPane();
    private ChatsPage chatsPageRef;
    private boolean isDarkb = false;

    ObjectOutputStream oos;
    ObjectInputStream ois;
    Socket socket;

    public ChatsPage(User user) {
        this.user = user;
        chatsPageRef = this;

    }

    public void run() {

        frame.setSize(400, 360);
        frame.setMaximumSize(new Dimension(400, 360));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();


        addChatButton = new JButton("Add Chat");

        placeComponents(panel);


        frame.add(panel);


        frame.setVisible(true);
        ProcessGetNewChats processGetNewChats = new ProcessGetNewChats(user, this);
        processGetNewChats.start();
        jlist.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        jlist.setSelectedIndex(getRow(e.getPoint()));
                        ProcessDeleteChat pdc = new ProcessDeleteChat(user, chatArrayList.get(getRow(e.getPoint())));
                        pdc.start();
                        chatArrayList.remove(getRow(e.getPoint()));
                        populateJList(chatArrayList);
                    } else {
                        SwingUtilities.invokeLater(new ConversationPage(chatArrayList.get(getRow(e.getPoint())), user));
                        frame.dispose();

                        if (chatArrayList.size() == 0) {
                            Utility.showWarningMessage("You have no chats!");
                        }
                    }
                } catch (Exception exc) {
                    System.out.println("There aren't any chats");
                }
            }
        });
        // darkTheme();

    }

    private int getRow(Point point) {
        return jlist.locationToIndex(point);
    }

    private void darkTheme() {

        if (isDarkb) {
            frame.getContentPane().setBackground(Color.DARK_GRAY);
            signOutButton.setBackground(Color.DARK_GRAY);
            addChatButton.setBackground(Color.DARK_GRAY);
            refreshChatsBtn.setBackground(Color.DARK_GRAY);
            darkThemeButton.setBackground(Color.BLACK);

            deleteAccountButton.setBackground(Color.BLACK);
            editAccountBtn.setBackground(Color.BLACK);

            signOutButton.setForeground(Color.WHITE);
            addChatButton.setForeground(Color.WHITE);
            deleteAccountButton.setForeground(Color.WHITE);
            refreshChatsBtn.setForeground(Color.WHITE);
            editAccountBtn.setForeground(Color.WHITE);
            darkThemeButton.setForeground(Color.WHITE);
            frame.repaint();
        } else {
            frame.getContentPane().setBackground(Color.WHITE);
            signOutButton.setBackground(Color.WHITE);
            addChatButton.setBackground(Color.WHITE);
            refreshChatsBtn.setBackground(Color.WHITE);
            darkThemeButton.setBackground(Color.WHITE);

            deleteAccountButton.setBackground(Color.WHITE);
            editAccountBtn.setBackground(Color.WHITE);

            //set foreground changes text color of buttons
            signOutButton.setForeground(Color.BLACK);
            addChatButton.setForeground(Color.BLACK);
            deleteAccountButton.setForeground(Color.BLACK);
            refreshChatsBtn.setForeground(Color.BLACK);
            editAccountBtn.setForeground(Color.BLACK);
            darkThemeButton.setForeground(Color.BLACK);
            frame.repaint();

        }
    }


    void populateJList(ArrayList<Chat> chats) {
        ArrayList<String> usersInChat = new ArrayList<>();

        for (Chat chat : chats) {
            StringBuilder sb = new StringBuilder();
            for (User user22 : chat.getUsers()) {
                if (!user22.getUsername().equals(user.getUsername()))
                    sb.append(user22.getName() + ", ");
            }
            //removes the last comma
            sb.deleteCharAt(sb.length() - 2);
            usersInChat.add(sb.toString());

        }
        jlist.setListData(usersInChat.toArray(new String[usersInChat.size()]));
    }


    public void placeComponents(JPanel panel) {
        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout());

        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        signOutButton.setPreferredSize(new Dimension(130, 50));
        editAccountBtn.setPreferredSize(new Dimension(100, 50));

        refreshChatsBtn.setPreferredSize(new Dimension(185, 50));
        deleteAccountButton.setPreferredSize(new Dimension(130, 50));
        addChatButton.setPreferredSize(new Dimension(185, 50));

        //darkThemeButton.setPreferredSize(new Dimension(127,50));


        refreshChatsBtn.addActionListener(actionListener);
        deleteAccountButton.addActionListener(actionListener);
        addChatButton.addActionListener(actionListener);
        signOutButton.addActionListener(actionListener);
        editAccountBtn.addActionListener(actionListener);
        darkThemeButton.addActionListener(actionListener);

        jScrollPane.setViewportView(jlist);

        jScrollPane.setPreferredSize(new Dimension(380, 200));
        bottomPanel.setPreferredSize(new Dimension(380, 60));
        topPanel.setPreferredSize(new Dimension(380, 60));


        panel.setLayout(new BorderLayout());
        panel.add(jScrollPane, BorderLayout.CENTER);

        topPanel.add(addChatButton);
        topPanel.add(refreshChatsBtn);
        // topPanel.add(darkThemeButton);
        panel.add(topPanel, BorderLayout.NORTH);

        bottomPanel.add(signOutButton);
        bottomPanel.add(editAccountBtn);
        bottomPanel.add(deleteAccountButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        label = new JLabel("You currently have no chats");
        label.setVisible(false);
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }


    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == darkThemeButton) {
                if (isDarkb) {
                    isDarkb = false;
                } else {
                    isDarkb = true;
                }
            }
            // darkTheme();

            if (e.getSource() == refreshChatsBtn) {
                System.out.println("refreshChatsBtn was pressed");
                ProcessGetNewChats processGetNewChats = new ProcessGetNewChats(user, chatsPageRef);
                processGetNewChats.start();
                System.out.println("listenForNewChats method was called");
            }
            if (e.getSource() == deleteAccountButton) {
                ProcessDeleteAccount processDeleteAccount = new ProcessDeleteAccount(user.getUid(), frame);
                processDeleteAccount.start();
            }
            if (e.getSource() == addChatButton) {
                ProcessGetNewChats processGetNewChats = new ProcessGetNewChats(user, chatsPageRef);
                System.out.println("Add chat button was pushed");
                AddChatPage ncd = new AddChatPage(user, chatsPageRef);
                Thread thread = new Thread(ncd);
                thread.start();
                frame.dispose();
            }
            if (e.getSource() == signOutButton) {
                frame.dispose();
                SwingUtilities.invokeLater(new HomePage());
            }
            if (e.getSource() == editAccountBtn) {
                frame.dispose();
                SwingUtilities.invokeLater(new EditAccountPage(user));
            }
        }
    };


    //accessors
    public User getUser() {
        return user;
    }

    public ArrayList<Chat> getChatArrayList() {
        return chatArrayList;
    }

    public void setChatArrayList(ArrayList<Chat> chatArrayList) {
        this.chatArrayList = chatArrayList;
    }

    public JList getJlist() {
        return jlist;
    }

    public void setJlist(JList jlist) {
        this.jlist = jlist;
    }
}
