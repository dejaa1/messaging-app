import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class is called on click of the refresh button
 * in Conversation. It retrieves the new messages.
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class ProcessGetNewMessages extends Thread {

    private Chat currentChat;
    private JScrollPane scrollPane;
    private ConversationPage conversationPage;

    public ProcessGetNewMessages(Chat currentChat, JScrollPane scrollPane, ConversationPage conversationPage) {
        this.currentChat = currentChat;
        this.scrollPane = scrollPane;
        this.conversationPage = conversationPage;

    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Utility.getHost(), Utility.getPortNumber());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(MessageServer.LISTENFORMESSAGESCHANGE);
            oos.flush();
            oos.writeObject(currentChat);
            oos.flush();

            String line = (String) ois.readObject();


            if (line.equals(MessageServer.SUCCESS)) {

                Chat updatedChat = (Chat) ois.readObject();
                ArrayList<Message> updatedMessageList = updatedChat.getMessages();
                ArrayList<String> messagesArrayList = new ArrayList<>();

                for (int i = 0; i < updatedMessageList.size(); i++) {
                    messagesArrayList.add(updatedMessageList.get(i).getMessage());
                }
                conversationPage.populateJList(messagesArrayList, scrollPane);
                //added
                conversationPage.setCurrentChat(updatedChat);
                conversationPage.setChatMessages(updatedChat.getMessages());
                //sets the scroll bar to the very bottom when new messages are added
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
            } else {
                System.out.println("Object in ProcessGetNewMessages was not read correctly");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //accessors

    public Chat getCurrentChat() {
        return currentChat;
    }
}
