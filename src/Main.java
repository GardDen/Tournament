/**
 * Created by 1 on 02.01.2018.
 */
public class Main {
    public static void main(String[] args) {
        Tournament tournament = new Tournament("Мясник - 2018"/*ConsoleHelper.readString("Введите название турнира:")*/);
        tournament.start();

        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Потребления памяти: " + (double)usedBytes/1048576 + "Mb");
    }
}
