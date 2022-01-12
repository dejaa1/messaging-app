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
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ProcessGetUserListTest {
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
        private User user = new User("username", "password", "Name", "uid");
        //@TODo fix, added null because constructor was edited
        private AddChatPage addChatPage = new AddChatPage(user,null);
        private ProcessGetUserList processGetUserList = new ProcessGetUserList(addChatPage);

        @Test
        public void testClassExists() {
            try {
                Class.forName("ProcessGetUserList");
            } catch(ClassNotFoundException e) {
                Assert.fail();
            }
        }

        @Test
        public void testFieldsExist() {
            try {
                AddChatPage ncd = processGetUserList.getNcd();
            } catch (NullPointerException | NumberFormatException e) {
                //checks if fields exist and are correct return type
                Assert.fail();
            }
        }
    }
}
