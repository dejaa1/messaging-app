import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class handles the SignUpPage process on a thread
 * This class is called from SignUpPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessSignUp extends Thread {
    private User user;
    private JFrame frame;

    public ProcessSignUp(User user, JFrame frame) {
        this.user = user;
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //passes a string, so the server knows what method to call
            oos.writeObject(MessageServer.SIGNUP);
            oos.flush();
            //passes the user object to server, server checks if credentials are valid in handleLogin method
            oos.writeObject(user);
            oos.flush();

            Object obj = ois.readObject();

            if (obj.equals(MessageServer.SUCCESS)) {
                System.out.println("----home page was launched----");
                Utility.showInformationMessage("You successfully Signed Up!");
                SwingUtilities.invokeLater(new HomePage());
                frame.dispose();
            } else if (obj.equals(MessageServer.FAILURE)) {
                Utility.showErrorMessage("User already exists! Please sign in!");
                System.out.println("User account could not be created");
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

    //accessor

    public User getUser() {
        return user;
    }
}
