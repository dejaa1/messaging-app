/**
 * Run this main method to run the program
 *
 * @author sf, an, ad, jc, av
 * @version 12-05-20
 */
public class Test {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new HomePage());
        Thread thread2 = new Thread(new HomePage());
        Thread thread3 = new Thread(new HomePage());
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
