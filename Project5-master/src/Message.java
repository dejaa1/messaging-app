import java.io.Serializable;

/**
 * This is the message object
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class Message implements Serializable {
    //field cannot be called message because the class name is message
    private String message;
    //stores the uid of the user who sent the char
    private String uid;


    public Message(String message, String uid) {
        this.message = message;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
