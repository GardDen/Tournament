/**
 * Используется если при генерации пар невозможно подобрать соперника.
 * Created by 1 on 05.01.2018.
 */
public class Bot extends Human {
    private static int countBot = 0;

    public Bot() {
        name = "Bot #" + ++countBot;
    }
}
