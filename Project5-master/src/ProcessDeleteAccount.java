import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class handles deleting a user account and is called from ChatsPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessDeleteAccount extends Thread {
    private String uid;
    private JFrame chatFrame;

    public ProcessDeleteAccount(String uid, JFrame frame) {
        this.uid = uid;
        this.chatFrame = frame;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            //passes a string to the server, so the server knows what method to call
            oos.writeObject(MessageServer.DELETEACCT);

            //sends the user's uid to the server, so the acct can be deleted
            oos.writeObject(uid);
            oos.flush();

            //this code is for debugging purposes (shows if user logged in and connected succesfully)
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String line = (String) ois.readObject();
            while (line != null) {
                if (line.equals(MessageServer.SUCCESS)) {
                    SwingUtilities.invokeLater(new HomePage());
                    chatFrame.setVisible(false);
                    Utility.showInformationMessage("Your account has successfully been deleted");
                    oos.close();
                    ois.close();
                    socket.close();
                    break;
                }
                if (line.equals(MessageServer.FAILURE)) {
                    SwingUtilities.invokeLater(new HomePage());
                    chatFrame.setVisible(false);
                    Utility.showErrorMessage("Your account has already been deleted!");
                    oos.close();
                    ois.close();
                    socket.close();
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
