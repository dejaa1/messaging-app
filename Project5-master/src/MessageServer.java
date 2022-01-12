//import sun.rmi.server.DeserializationChecker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the server and handles most processes in the app
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */

public class MessageServer implements Serializable {
    //constant for login
    public static final String LOGIN = "login";
    //constant for signup
    public static final String SIGNUP = "signup";
    //constant for retrieving a users list of chats
    public static final String CHATLIST = "getchatlist";
    //constant for adding a chat
    public static final String ADDCHAT = "addchat";
    //constant for retrieving a chat
    public static final String GETCHAT = "getchatmessages";
    //constant for deleting an account
    public static final String DELETEACCT = "deleteaccount";
    //constant for when there is a success
    public static final String SUCCESS = "success";
    //constant for when there is a failure
    public static final String FAILURE = "failure";
    //constant for when the user does not have any chats
    public static final String NOCHATS = "NoChats";
    //constant for listening to changes in a user's chats
    public static final String LISTENFORCHATSCHANGE = "checkForChangeToUserChatList";
    //constant for listening to changes in a user's messages in a chat (from other users)
    public static final String LISTENFORMESSAGESCHANGE = "checkForChangeToUserChatMessages";
    //constant for when a currentUser added a message
    public static final String CURRENTUSERADDEDMESSAGE = "currentUserAddedMessage";
    //constant that is being passed if there is no change in messages
    public static final String NOMESSAGESCHANGED = "nomessageschanged";
    //constant for knowing if user does not exist
    public static final String USERDNE = "userDNE";
    //constant for getting a user list
    public static final String GETUSERLIST = "getUserList";
    //constant for when user edits password
    public static final String EDITUSERPASSWORD = "userEditPassword";
    //constant for when user is deleting a chat
    public static final String DELETECHAT = "deleteChat";
    //constant for when user is editing a message
    public static final String EDITMESSAGE = "editMessage";
    //constant for when user deletes a message
    public static final String DELETEMESSAGE = "deleteMessage";

    private int port;
    private static Map<String, User> users;
    private static Map<String, Chat> chats;

    //private PrintWriter printWriter;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;

    /**
     * Constructor-intializes users and chats HashMap
     *
     * @param port
     */
    public MessageServer(int port) {
        this.port = port;
        users = new HashMap<>();
        chats = new HashMap<>();
    }

    public static void main(String[] args) {

        MessageServer server = new MessageServer(4242);
        server.initialize();
    }

    public void initialize() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                socket = serverSocket.accept();
                //reading the input from the client
                System.out.println("User connected");
                ois = new ObjectInputStream(socket.getInputStream());
                //writes object to the client
                oos = new ObjectOutputStream(socket.getOutputStream());

                //This switch statement determines which method to all
                String methodToCall = (String) ois.readObject();
                System.out.println(methodToCall);
                switch (methodToCall) {
                    case LOGIN: {
                        System.out.println("Running login");
                        User user = (User) ois.readObject();
                        handleLogin(user);
                        break;
                    }
                    case SIGNUP: {
                        System.out.println("Running signup");
                        User user = (User) ois.readObject();
                        handleSignup(user);
                        break;
                    }
                    case CHATLIST: {
                        System.out.println("Retrieving chats for the user");
                        String uid = (String) ois.readObject();
                        ArrayList<Chat> chats = loadChats(uid);
                        if (chats.isEmpty()) {
                            oos.writeObject(NOCHATS);
                            oos.flush();
                        } else {
                            oos.writeObject(SUCCESS);
                            oos.flush();
                            try {
                                oos.writeObject(chats);
                                oos.flush();
                            } catch (NullPointerException npe) {
                                System.out.println("97: Null");
                            }
                        }
                        break;
                    }
                    case LISTENFORCHATSCHANGE: {
                        String uid = (String) ois.readObject();
                        System.out.println(uid);
                        ArrayList<Chat> chats = loadChats(uid);
                        if (chats.isEmpty()) {
                            oos.writeObject(NOCHATS);
                            oos.flush();
                            System.out.println(NOCHATS + " for this user");
                        } else {
                            oos.writeObject(SUCCESS);
                            oos.flush();
                            System.out.println(SUCCESS);
                            oos.writeObject(chats);
                            oos.flush();

                        }
                        break;
                    }
                    case LISTENFORMESSAGESCHANGE: {
                        try {
                            Chat currentChat = (Chat) ois.readObject();
                            String currentChatId = currentChat.getId();
                            checkForMessagesChange(currentChat, currentChatId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case CURRENTUSERADDEDMESSAGE: {
                        String currentChatId = (String) ois.readObject();
                        Message currentMessage = (Message) ois.readObject();
                        updateMessagesChange(currentChatId, currentMessage);
                        break;
                    }
                    case MessageServer.ADDCHAT: {
                        Chat chatToAdd = (Chat) ois.readObject();
                        addChat(chatToAdd);
                        break;
                    }
                    case MessageServer.GETCHAT: {
                        System.out.println("Running getchat");
                        Object obj = ois.readObject();
                        if (obj instanceof Chat) {
                            handleGetChat((Chat) obj);
                        }
                        break;
                    }
                    case DELETEACCT: {
                        Object obj = ois.readObject();
                        if (obj instanceof String) {
                            String uid = (String) obj;
                            deleteAccount(uid);
                        }
                        break;
                    }
                    case GETUSERLIST: {
                        System.out.println("Getting list of users");
                        oos.writeObject(SUCCESS);
                        oos.flush();
                        oos.writeObject(turnMapIntoArrayList(users));
                        oos.flush();
                        break;
                    }
                    case EDITUSERPASSWORD: {
                        System.out.println("editing user's password");
                        User user = (User) ois.readObject();
                        String password = (String) ois.readObject();
                        handleEditPassword(user, password);
                        break;
                    }
                    case DELETECHAT: {
                        System.out.println("Deleting Chat from User");
                        String userUID = (String) ois.readObject();
                        String chatID = (String) ois.readObject();
                        removeChatFromUser(userUID, chatID);
                        break;
                    }
                    case EDITMESSAGE: {
                        System.out.println("editing a message");
                        String chatID = (String) ois.readObject();
                        String message = (String) ois.readObject();
                        int messageIndex = (int) ois.readObject();
                        handleEditMessage(chatID, message, messageIndex);
                        break;
                    }
                    case DELETEMESSAGE: {
                        System.out.println("deleting a message");
                        String chatID = (String) ois.readObject();
                        int index = (int) ois.readObject();
                        handleDeleteMessage(chatID, index);
                        break;
                    }

                }
                System.out.print("Users stored in server: ");
                try {
                    for (Map.Entry<String, User> userEntry : users.entrySet()) {
                        System.out.print(userEntry.getValue().getUsername() + ", ");
                    }
                } catch (NullPointerException npe) {
                    System.out.println("154:A value was null");
                }

            }
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Server Error: " + exception.getMessage());
            exception.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            System.out.println("A value was null");
            npe.printStackTrace();
        }
    }

    /**
     * Removes the chatID from the user's chatId arraylist, thus deleting the conversatin
     *
     * @param userUID
     * @param chatID
     * @throws IOException
     */
    private void removeChatFromUser(String userUID, String chatID) throws IOException {
        if (users.containsKey(userUID)) {
            User usert = users.get(userUID);
            ArrayList<String> chatIDS = usert.getChats();
            chatIDS.remove(chatID);
            usert.setChats(chatIDS);
            try {
                oos.writeObject(SUCCESS);
                oos.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            oos.writeObject(FAILURE);
            oos.flush();
        }
    }

    /**
     * Turns the user hashmap into an ArrayList that can be sent to the client
     *
     * @param currentUsers
     * @return
     */
    private ArrayList<User> turnMapIntoArrayList(Map<String, User> currentUsers) {
        ArrayList<User> listOfUsers = new ArrayList<>();
        for (Map.Entry<String, User> userEntry : currentUsers.entrySet()) {
            listOfUsers.add(userEntry.getValue());
        }
        return listOfUsers;
    }

    /**
     * Adds a chat object to the database. First, the chat is added to all the users
     * and then the chat is added to the server
     *
     * @param chatToAdd
     */
    private void addChat(Chat chatToAdd) {
        ArrayList<User> userList = chatToAdd.getUsers();
        String chatID = chatToAdd.getId();
        Chat tempChat = chatToAdd;
        for (User user : userList) {
            if (users.containsKey(user.getUid())) {
                User tempuser = users.get(user.getUid());
                ArrayList<String> userChatIDS = tempuser.getChats();
                userChatIDS.add(chatID);
                //updates user chat id list in the map
                users.get(user.getUid()).setChats(userChatIDS);

            } else {
                //removes user from chat if it doesn't exist
                ArrayList<User> userArrayList = tempChat.getUsers();
                userArrayList.remove(user);
                tempChat.setUsers(userArrayList);
            }
        }
        //adds chat to map
        chats.put(chatToAdd.getId(), chatToAdd);
        try {
            oos.writeObject(SUCCESS);
            oos.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * removes the user from the users map
     *
     * @param uid3
     */
    private synchronized void deleteAccount(String uid3) {
        if (uid3 != null) {
            if (users.containsKey(uid3)) {

                users.remove(uid3);
                System.out.println("The user has been deleted");
                try {
                    oos.writeObject(SUCCESS);
                    oos.flush();

                } catch (IOException ioException) {
                    System.out.println("Error writing to client from deleteAccount");
                }
            } else {
                try {
                    oos.writeObject(FAILURE);
                    oos.flush();

                } catch (IOException ioException) {
                    System.out.println("Error writing to client from deleteAccount");
                    ioException.printStackTrace();
                }
            }
        }
    }

    /**
     * this method checks if new messages were added by other users (not current user)
     *
     * @param currentChat
     * @param currentChatId
     */
    private synchronized void checkForMessagesChange(Chat currentChat,
                                                     String currentChatId) {
        Chat currentChatServerMessagesList = chats.get(currentChatId);
        try {
            oos.writeObject(SUCCESS);
            oos.flush();
            oos.writeObject(chats.get(currentChatId));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * this is a method for editing a message
     */
    private void handleEditMessage(String chatID, String message, int index) {
        if (chats.containsKey(chatID)) {
            Chat newChat = chats.get(chatID);
            try {
                newChat.getMessages().get(index).setMessage(message);
            } catch (IndexOutOfBoundsException e) {
                try {
                    oos.writeObject(FAILURE);
                    oos.flush();
                } catch (IOException ioException) {
                    e.printStackTrace();
                }
            }
            try {
                oos.writeObject(SUCCESS);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                oos.writeObject(FAILURE);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method for deleting a message from a chat
     *
     * @param chatID
     * @param index
     */
    private void handleDeleteMessage(String chatID, int index) {
        if (chats.containsKey(chatID)) {
            Chat newChat = chats.get(chatID);
            try {
                newChat.getMessages().remove(index);
            } catch (IndexOutOfBoundsException e) {
                try {
                    oos.writeObject(FAILURE);
                    oos.flush();
                } catch (IOException ioException) {
                    e.printStackTrace();
                }
            }
            try {
                oos.writeObject(SUCCESS);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                oos.writeObject(FAILURE);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this method adds a message to the chat in the server that current user added
     *
     * @param currentChatId
     * @param message
     */
    private synchronized void updateMessagesChange(String currentChatId, Message message) {
        Chat currentChat = chats.get(currentChatId);
        currentChat.addMessage(message);
    }

    /**
     * @param uid
     * @return list of Chat objects
     */
    private synchronized ArrayList<Chat> loadChats(String uid) {
        // list of chats that will be sent back to the client
        ArrayList<Chat> chatList = new ArrayList<>();
        System.out.println(users.get(uid).getUid());
        User user = users.get(uid);
        if (user.getChats() == null) {
            user.setChats(new ArrayList<String>());
        }
        //if the user doesn't exist
        if (!users.containsKey(uid)) {
            try {
                oos.writeObject(USERDNE);
                oos.flush();
            } catch (IOException ioException) {
                System.out.println("Error writing to client from loadChat");
                ioException.printStackTrace();
            }

        }

        if (!user.getChats().isEmpty()) {
            for (String chatID : user.getChats()) {
                //retrieving the chat based on the chatID
                chatList.add(chats.get(chatID));

            }
        }
        return chatList;
    }

    /**
     * Handles logging in process for a user
     * Method is thread safe
     *
     * @param user
     *
     */
    public synchronized void handleLogin(User user) {
        boolean userExists = false;
        boolean isCorrectCredentials = false;
        User user1 = null;

        for (Map.Entry<String, User> userEntry : users.entrySet()) {
            if (userEntry.getValue().getUsername().equals(user.getUsername())) {
                userExists = true;
                if (userEntry.getValue().getPassword().equals(user.getPassword())) {
                    isCorrectCredentials = true;
                    user1 = userEntry.getValue();
                    break;
                }
            }
        }
        try {
            if (isCorrectCredentials) {
                oos.writeObject(MessageServer.SUCCESS);
                oos.flush();
                oos.writeObject(user1);
                oos.flush();
                System.out.println("------User: " + user.getUsername() +
                        " connected to server and successfully logged in-----");
            }
            if (!userExists || !isCorrectCredentials) {
                oos.writeObject(MessageServer.FAILURE);
                oos.flush();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * handles a sign up process for a user
     * Method is threadSafe
     *
     * @param user
     */
    public synchronized void handleSignup(User user) {
        boolean userAlreadyExists = false;

        for (Map.Entry<String, User> userEntry : users.entrySet()) {
            if (userEntry.getValue().getUsername().equals(user.getUsername())) {
                userAlreadyExists = true;
            }
        }

        if (userAlreadyExists) {
            try {
                oos.writeObject(MessageServer.FAILURE);
                oos.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        } else {
            try {
                users.put(user.getUid(), user);
                oos.writeObject(MessageServer.SUCCESS);
                oos.flush();
                System.out.println("-----User: " + user.getUsername() +
                        " connected to server and added to users database-----");
            } catch (IOException ioException) {
                System.out.println("Error writing object");
                ioException.printStackTrace();
            }
        }

    }

    public synchronized void handleGetChat(Chat chat) {
        try {
            chat.getId();
            oos.writeObject(MessageServer.SUCCESS);
            oos.flush();
            System.out.println("chat you clicked on was found");

        } catch (Exception e) {
            try {
                oos.writeObject(MessageServer.FAILURE);
                oos.flush();
            } catch (IOException ioException) {
                System.out.println("Error writing message");
                ioException.printStackTrace();
            }
            System.out.println("chat you clicked on could NOT be found");
        }
    }

    /**
     * checks if the user that needs to be edited exists and changes their password if they exist
     *
     * @param user
     * @param password
     */
    private void handleEditPassword(User user, String password) {
        Boolean userWasFound = false;
        try {
            for (Map.Entry<String, User> userEntry : users.entrySet()) {
                if (userEntry.getValue().getUsername().equals(user.getUsername()) &&
                        !password.equals(userEntry.getValue().getPassword())) {
                    userWasFound = true;
                    userEntry.getValue().setPassword(password);
                    oos.writeObject(SUCCESS);
                    oos.flush();
                }
            }
            if (!userWasFound) {
                oos.writeObject(FAILURE);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * getter methods
     *
     * @return
     */
    public static Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Chat> getChats() {
        return chats;
    }

    public int getPort() {
        return port;
    }
}
