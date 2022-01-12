import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class handles retrieving all
 * the users to display on the AddChatPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessGetUserList extends Thread {
    private AddChatPage ncd;

    public ProcessGetUserList(AddChatPage ncd) {
        this.ncd = ncd;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //passes a string to the server, so the server knows what method to call
            System.out.println("Writing to server to get user list");
            oos.writeObject(MessageServer.GETUSERLIST);
            oos.flush();
            Object obj = ois.readObject();
            while (obj != null) {
                if (obj.equals(MessageServer.SUCCESS)) {
                    try {

                        ArrayList<User> users = (ArrayList<User>) ois.readObject();
                        ncd.populateList(users);

                        break;
                    } catch (Exception e) {
                        System.out.println("The information sent from the server" +
                                " was not an ArrayList of Users or the list isn't " +
                                "being passed a correct array");
                        e.printStackTrace();
                    }

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

    public AddChatPage getNcd() {
        return ncd;
    }
}

