import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * This is the Chat model
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class Chat implements Serializable {
    //holds the name of the chat(only for groupchats)
    private String name;
    //holds the id for the chat
    private String id;
    //determines if chat is a group chat or not
    private boolean groupchat;
    //holds all the message in a chat
    private ArrayList<Message> messages;
    //holds all the users in the chat
    private ArrayList<User> users;


    public Chat(ArrayList<Message> messages, ArrayList<User> users, boolean groupchat) {
        Random random = new Random();
        //ID consists of a String is this format: "IDs_of_users+rand_int" ('+' is excluded)
        this.id = "" + users.get(0).getUsername() + users.get(1).getUsername() + random.nextInt(1000000);
        this.groupchat = groupchat;
        this.messages = messages;
        this.users = users;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isGroupchat() {
        return groupchat;
    }

    public void setGroupchat(boolean groupchat) {
        this.groupchat = groupchat;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    //method for adding messages (used in ConversationPage page)
    public void addMessage(Message message) {
        messages.add(message);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }


}
