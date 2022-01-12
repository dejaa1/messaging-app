import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * this class checks if the chat exists in the chats list and send appropriate
 * response to the server (SUCCESS or FAILURE)
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessGetChat extends Thread {

    private Chat currentChat;
    private User user;

    public ProcessGetChat(Chat currentChat, User user) {
        this.currentChat = currentChat;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //passes a string, so the server knows what method to call
            oos.writeObject(MessageServer.GETCHAT);

            //passes the chatId to server, server checks if credentials are valid
            oos.writeObject(currentChat);

            oos.flush();

            //this code is for debugging purposes (shows if user signed up and connected succesfully)
            BufferedReader bfr = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            String line = bfr.readLine();

            while (line != null) {
                if (line.equals(MessageServer.SUCCESS)) {
                    System.out.println("----the chat you requested was found----");
                    // SwingUtilities.invokeLater(new ConversationPage(currentChat,user));
                }
                if (line.equals(MessageServer.FAILURE)) {
                    System.out.println("----the chat you requested was NOT found----");
                }
                System.out.println(line);
                line = bfr.readLine();
            }
            oos.close();
            bfr.close();
            socket.close();
        } catch (
                UnknownHostException e) {
            e.printStackTrace();
        } catch (
                IOException ioException) {
            ioException.printStackTrace();
        }
    }
}