import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class handles sending new messages
 * and is called from the conversation page
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessSendNewMessages extends Thread {

    private Message currentMessage;
    private Chat currentChat;


    public ProcessSendNewMessages(Message currentMessage, Chat currentChat) {
        this.currentMessage = currentMessage;
        this.currentChat = currentChat;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(MessageServer.CURRENTUSERADDEDMESSAGE);
            oos.writeObject(currentChat.getId());
            oos.writeObject(currentMessage);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //accessors

    public Chat getCurrentChat() {
        return currentChat;
    }

    public Message getCurrentMessage() {
        return currentMessage;
    }
}
