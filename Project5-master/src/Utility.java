import javax.swing.*;

/**
 * This is the Utility class, which has static methods, this reduces duplication of Code
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class Utility {


    public static void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null,
                errorMessage, "Error", JOptionPane.ERROR_MESSAGE);

    }

    public static void showInformationMessage(String infoMessage) {
        JOptionPane.showMessageDialog(null, infoMessage, "Important", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningMessage(String warningMessage) {
        JOptionPane.showMessageDialog(null, warningMessage, "Warning", JOptionPane.WARNING_MESSAGE);
    }


    /**
     * Allows host and port to be changed across the project
     */
    public static String getHost() {
        return "localhost";
    }

    public static int getPortNumber() {
        return 4242;
    }


}
