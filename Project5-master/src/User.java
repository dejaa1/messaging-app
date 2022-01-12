import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This is the model for a user
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 *
 */
public class User implements Serializable {
    //username
    private String username;
    //name
    private String name;
    //password
    private String password;
    //user id
    private String uid;
    private ArrayList<String> chatIDs;

    //used for signing up a user; this version user is what is stored on the server
    public User(String username, String password, String name, String uid) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.uid = uid;
        this.chatIDs = new ArrayList<>();
    }

    //used for logging the user in
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.chatIDs = new ArrayList<>();
    }


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String id) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getChats() {
        return chatIDs;
    }

    public void setChats(ArrayList<String> currentChatIDs) {
        this.chatIDs = currentChatIDs;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}

