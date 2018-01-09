import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 1 on 02.01.2018.
 */
public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readString(String info) {
        System.out.print(info);
        String text;
        try {
            text = reader.readLine();
            if (text.isEmpty()) throw new IOException();
        } catch (IOException e) {
            text = readString("Неверный ввод, повторите ввод.");
        }
        return text;
    }

    public static int readInt(String info) {
        int number;
        try {
            number = Integer.parseInt(readString(info));
        } catch (NumberFormatException e) {
            number = readInt("Неверный ввод числа, повторите ввод:");
        }
        return number;
    }
}
