
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class handles getting the user's chat list and is called in ChatsPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessGetNewChats extends Thread {
    private User user;
    private ChatsPage chatsPage;

    public ProcessGetNewChats(User user, ChatsPage chatsPage) {
        this.user = user;
        this.chatsPage = chatsPage;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //passes a string to the server, so the server knows what method to call
            oos.writeObject(MessageServer.CHATLIST);
            oos.flush();

            oos.writeObject(user.getUid());
            oos.flush();
            Object obj = ois.readObject();
            while (obj != null) {
                if (obj.equals(MessageServer.SUCCESS)) {
                    try {
                        System.out.println("Chats page has been refreshed");
                        ArrayList<Chat> chats = (ArrayList<Chat>) ois.readObject();

                        chatsPage.setChatArrayList(chats);
                        chatsPage.populateJList(chats);
                        break;
                    } catch (Exception e) {
                        System.out.println("The information sent from the server was not an ArrayList of Chats");
                    }

                } else if (obj.equals(MessageServer.NOCHATS)) {
                    System.out.println("No new chats were added");
                    //  Utility.showInformationMessage("You don't have any chats. Click the add button to add more");
                    break;
                }
                obj = ois.readObject();
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
}
