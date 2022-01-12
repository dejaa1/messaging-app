import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class handles deleting messages and is called from Conversation page
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessDeleteMessage extends Thread {
    private String chatID;
    private String message;
    private int index;

    public ProcessDeleteMessage(String chatID, int index) {
        this.chatID = chatID;
        this.message = message;
        this.index = index;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(MessageServer.DELETEMESSAGE);
            System.out.println("sent delete message to server");
            oos.flush();
            oos.writeObject(chatID);
            oos.flush();
            oos.writeObject(index);
            oos.flush();


            oos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

