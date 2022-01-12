import org.junit.Test;
import org.junit.After;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.rules.Timeout;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ProcessSendNewMessagesTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        @After
        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        //Tests
        private Message message1 = new Message("Hello", "uid1");
        private Message message2 = new Message("Hi", "uid2");
        private Message message3 = new Message("How are you?", "uid1");
        private Message message4 = new Message("I'm doing well", "uid2");
        private Message message5 = new Message("Hi guys", "uid3");
        private ArrayList<Message> messages = new ArrayList<Message>();

        private User user1 = new User("username1", "password", "Name1", "uid1");
        private User user2 = new User("username2", "password", "Name2", "uid2");
        private User user3 = new User("username3", "password", "Name3", "uid3");
        private ArrayList<User> users = new ArrayList<User>();

        private Socket socket;
        private MessageServer messageServer;
        //@TODO we are not using user thread
      //  private Userthread userthread1 = new Userthread(socket, messageServer);
      //  private Userthread userthread2 = new Userthread(socket, messageServer);
      //  private Userthread userthread3 = new Userthread(socket, messageServer);
      //  private ArrayList<Userthread> userthreads = new ArrayList<Userthread>();

        private Chat groupChat;
        public void initializeFields() {
            messages.add(message1);
            messages.add(message2);
            messages.add(message3);
            messages.add(message4);
            messages.add(message5);

            users.add(user1);
            users.add(user2);
            users.add(user3);
        }

        //constructor
        private ProcessSendNewMessages processSendNewMessages = new ProcessSendNewMessages(message1, groupChat);
        //Problem

        @Test
        public void testClassExists() {
            try {
                Class.forName("ProcessSendNewMessages");
            } catch(ClassNotFoundException e) {
                Assert.fail();
            }
        }

        @Test
        public void testFieldsExist() {
            try {
                Message currentMessage = processSendNewMessages.getCurrentMessage();
                Chat currentChat = processSendNewMessages.getCurrentChat();
            } catch (NullPointerException | NumberFormatException e) {
                //checks if fields exist and are correct return type
                Assert.fail();
            } catch (Exception e) {
                Assert.fail();
            }
        }
    }
}
