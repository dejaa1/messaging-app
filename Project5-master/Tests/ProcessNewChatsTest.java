import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.*;

public class ProcessNewChatsTest {
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
       //@TODO chatspage is now null
        private ProcessGetNewChats processGetNewChats = new ProcessGetNewChats(user,null);

        @Test
        public void testClassExists() {
            try {
                Class.forName("ProcessGetNewChats");
            } catch(ClassNotFoundException e) {
                Assert.fail();
            }
        }

        @Test
        public void testFieldsExist() {
            try {
                User user = processGetNewChats.getUser();
            } catch (NullPointerException | NumberFormatException e) {
                //checks if fields exist and are correct return type
                Assert.fail();
            }
        }
    }
}
