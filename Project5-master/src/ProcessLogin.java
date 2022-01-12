


import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class runs a thread that handles the login process
 * This class is called from LoginPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessLogin extends Thread {
    private User user;
    private JFrame frame;

    public ProcessLogin(User user, JFrame frame) {
        this.user = user;
        this.frame = frame;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void run() {

        try {
            System.out.println("Running");
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //passes a string to the server, so the server knows what method to call
            oos.writeObject(MessageServer.LOGIN);
            oos.flush();
            oos.writeObject(user);
            oos.flush();

            //this code is for debugging purposes (shows if user logged in and connected successfully)
            Object obj = ois.readObject();
            while (obj != null) {
                if (obj.equals(MessageServer.SUCCESS)) {
                    System.out.println("----chats page was launched----");
                    User newUser = (User) ois.readObject();
                    SwingUtilities.invokeLater(new ChatsPage(newUser));
                    frame.dispose();
                    break;
                }
                if (obj.equals(MessageServer.FAILURE)) {
                    Utility.showErrorMessage("User could not be found! Please check your credentials or sign up!");
                    System.out.println("User account can't be found");
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
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
