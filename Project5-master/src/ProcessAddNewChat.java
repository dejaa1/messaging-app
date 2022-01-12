import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class is called when a New Chat is created from AddChatPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessAddNewChat extends Thread {
    private User user;
    private Chat chat;

    public ProcessAddNewChat(User user, Chat chat) {
        this.user = user;
        this.chat = chat;
    }

    @Override
    public void run() {
        try {

            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(MessageServer.ADDCHAT);
            oos.flush();
            oos.writeObject(chat);
            oos.flush();
            String line = (String) ois.readObject();
            if (line.equals(MessageServer.SUCCESS)) {
                Utility.showInformationMessage("Your chat has successfully been created!");
            }

            oos.close();
            ois.close();
            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //accessors

    public User getUser() {
        return user;
    }

    public Chat getChat() {
        return chat;
    }
}
