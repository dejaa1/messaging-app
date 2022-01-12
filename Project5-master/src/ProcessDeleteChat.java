import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class handles deleting a chat from the user's list of chats
 * and is called by righ-clicking on a chat from ChatsPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessDeleteChat extends Thread {
    private User user;
    private Chat chat;

    public ProcessDeleteChat(User user, Chat chat) {
        this.user = user;
        this.chat = chat;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            //passes a string to the server, so the server knows what method to call
            oos.writeObject(MessageServer.DELETECHAT);
            oos.flush();

            //sends the user's uid to the server, so the acct can be deleted
            oos.writeObject(user.getUid());
            oos.flush();
            oos.writeObject(chat.getId());
            oos.flush();

            //this code is for debugging purposes (shows if user logged in and connected succesfully)
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String line = (String) ois.readObject();
            while (line != null) {
                if (line.equals(MessageServer.SUCCESS)) {


                    Utility.showInformationMessage("The chat has successfully been deleted");

                    break;
                } else if (line.equals(MessageServer.FAILURE)) {
                    Utility.showErrorMessage("The chat could not be deleted at this time. Please try again later");
                    break;
                }

                line = (String) ois.readObject();
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


}
