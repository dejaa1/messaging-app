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

public class ChatTest {
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
        private  Message message2 = new Message("Hi", "uid2");
        private  Message message3 = new Message("How are you?", "uid1");
        private  Message message4 = new Message("I'm doing well", "uid2");
        private  Message message5 = new Message("Hi guys", "uid3");
        private  ArrayList<Message> messages = new ArrayList<Message>();

        private  User user1 = new User("username1", "password", "Name1", "uid1");
        private  User user2 = new User("username2", "password", "Name2", "uid2");
        private  User user3 = new User("username3", "password", "Name3", "uid3");
        private  ArrayList<User> users = new ArrayList<User>();
        private  ArrayList<User> users1 = new ArrayList<User>();

        private  Socket socket;
        private  MessageServer messageServer;

        public void initializeFields() {
            messages.add(message1);
            messages.add(message2);
            messages.add(message3);
            messages.add(message4);
            messages.add(message5);

            users.add(user1);
            users.add(user2);
            users.add(user3);
            users1.add(user1);
            users1.add(user2);
        }

        @Test
        public void testClassExists() {
            try {
                Class.forName("Chat");
            } catch (ClassNotFoundException e) {
                Assert.fail();
            }
        }

        @Test
        public void testFieldsExist() {
            //constructor
            initializeFields();
            Chat chat = new Chat(messages, users, false);

            try {
                String name = chat.getName();
                String id = chat.getId();
                ArrayList<Message> messages = chat.getMessages();
                ArrayList<User> users = chat.getUsers();
                boolean test1 = chat.isGroupchat();
            } catch (NullPointerException | NumberFormatException e) {
                //checks if fields exist and are correct return type
                Assert.fail();
            }
        }

        @Test
        public void testValidInput() {
            //constructors
            initializeFields();
            Chat chat1 = new Chat(messages, users, true);
            Chat chat2 = new Chat(messages, users1, false);

            try {
                chat1.setId("abcd");
                chat2.setId("abcd");
                boolean test1 = chat1.isGroupchat();
                boolean test2 = chat2.isGroupchat();
                chat1.setGroupchat(true);
                chat2.setGroupchat(false);
                chat1.addMessage(null);
                chat2.addMessage(null);
                ArrayList<User> testOne = new ArrayList<User>();
                chat1.setUsers(testOne);
                chat2.setUsers(testOne);
            } catch (NullPointerException | NumberFormatException e) {
                //checks if fields exist and are correct return type
                Assert.fail();
            }
        }

        @Test
        public void testInvalidInput() {
            //constructors
            initializeFields();
            Chat chat1 = new Chat(messages, users, true);
            Chat chat2 = new Chat(messages, users1, false);

            try {
                String name = chat1.getName();
                String name1 = chat2.getName();
                String id = chat1.getId();
                String id2 = chat2.getId();
                chat1.setId("abcd");
                chat2.setId("abcd");
                ArrayList<Message> messages = chat1.getMessages();
                ArrayList<Message> messages3 = chat2.getMessages();
                ArrayList<User> users = chat1.getUsers();
                ArrayList<User> users1 = chat2.getUsers();
                boolean test1 = chat1.isGroupchat();
                boolean test2 = chat2.isGroupchat();
                chat1.setGroupchat(true);
                chat2.setGroupchat(false);
                chat1.addMessage(null);
                chat2.addMessage(null);
                ArrayList<User> testOne = new ArrayList<User>();
                chat1.setUsers(testOne);
                chat2.setUsers(testOne);
            } catch (NullPointerException | NumberFormatException e) {
                Assert.fail();
            }
        }
    }
}
