
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class handles editing account information and is called from ChatsPage
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessEditAccount extends Thread {
    private String password;
    private User user;

    public ProcessEditAccount(String password, User user) {
        this.password = password;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(MessageServer.EDITUSERPASSWORD);
            oos.flush();
            oos.writeObject(user);
            oos.flush();
            oos.writeObject(password);
            oos.flush();

            Object obj = ois.readObject();

            if (obj.equals(MessageServer.SUCCESS)) {
                Utility.showInformationMessage("Your password has been successfully changed!");
            } else if (obj.equals(MessageServer.FAILURE)) {
                Utility.showInformationMessage("This is your current password. Type a new one!");
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
