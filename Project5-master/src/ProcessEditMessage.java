
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class handles editing messages and is called in the Conversation page
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessEditMessage extends Thread {

    private String chatID;
    private String message;
    private int index;

    public ProcessEditMessage(String chatID, String message, int index) {
        this.chatID = chatID;
        this.message = message;
        this.index = index;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(MessageServer.EDITMESSAGE);
            oos.flush();
            oos.writeObject(chatID);
            oos.flush();
            oos.writeObject(message);
            oos.flush();
            oos.writeObject(index);
            oos.flush();

            String line = (String) ois.readObject();

            if (line.equals(MessageServer.SUCCESS)) {
                System.out.println("Message has been edited!");
            } else {
                Utility.showErrorMessage("Message could not be edited!");
                System.out.println("Edit Message: either index out of bounds or message/chat DNE");
            }
            oos.close();
            ois.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
