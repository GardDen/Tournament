import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Task:
 * 1 бонусный бал сколько?  метод play
 * 2 GUI
 * 3 Удаление людей во время турнира.
 * 4 добавление людей опоздавших посреди турнира
 *
 * Created by 1 on 02.01.2018.
 * Соревновения проходят на подобие швейцарской системы в MAX_TOUR туров.
 * Участники Human встречаются и между ними проводятся раунды, пока один из участников не наберет нужного
 * числа побед ROUND_FOR_WIN. В случае победы в раунде участнику начисляется одно очко, при поражении 0 очков.
 * <p>
 *     В каждом туре генерируются пары участников PairHuman на основе их количества очков.
 *     Два участника встречаются между собой лишь однажды.
 * </p>
 *
 * Чем больше значение MAX_TOUR, тем выше эффективность определения рейтинга участников соревнования.
 *
 * @see Human
 * @see PairHuman
 *
 */
public class Tournament {
    private final int ROUND_FOR_WIN = 2;
    private final int MAX_TOUR = 4;
    private final int POINT_VICTORY = 1;
    private final int POINT_LOSS = 0;
    private int countPlayedPair = 0;
    private int tour;
    private String name;
    private Date date;
    private List<Human> humans = new ArrayList<>();
    private List<PairHuman> pairs = new ArrayList<>();
    
    public Tournament(String name) {
        this.name = name;
        date = new Date();
        tour = 1;
        initHumans(ConsoleHelper.readInt("Введите число участников турнира: "));
    }

    /**
     * Инициализирует список участников соревнования.
     * @param countHumans
     */
    public void initHumans(int countHumans) {
        for (int i = 0; i < countHumans; i++) {
            addHuman("Введите имя(фамилию и имя) участника турнира: ");
        }
    }

    /**
     * Запрашивает данные участника и добавляет его в список участников соревнования, если там его ещё нет.
     * @param info
     * @return human
     */
    public Human addHuman(String info) {
        String name = ConsoleHelper.readString(info);
        Human human = new Human(name);
        if (!humans.contains(human)) {
            humans.add(human);
        } else {
            human = addHuman("Такой человек уже зарегестрирован, попробуйте задать другое имя: ");
        }
        return human;
    }

    /**
     * Стартует процесс соревнования.
     */
    public void start() {
        while (tour <= MAX_TOUR) {
            Collections.sort(humans);
            startTour();
            tour++;
        }
        Collections.sort(humans);
        printListHumans();
    }

    /**
     * Стартует следующий тур соревнования.
     * В этом методе используется переменная countPlayedPair, для того чтобы цикл работал в очередном туре только с
     * парами которые ещё не встречались.
     */
    private void startTour() {
        System.out.println("Тур " + tour);
        printListHumans();
        generatePair();
        printListPairOfTour();
        for (int i = countPlayedPair; i < pairs.size(); i++) {
            pairs.get(i).play();
        }
        countPlayedPair = pairs.size();
        check();


    }

    /**
     * Этот медот позволяет внести изменения в процесс турнира в конце тура.
     * Дает возможность удалить участника из списка или добавить нового участника.
     */
    private void check() {
        int check = ConsoleHelper.readInt("Тур " + tour + "завершен. \n" +
                "Требуются внесение изменений в список участников? Введите\n" +
                "1. Добавить участника.\n" +
                "2. Удалить участника.\n" +
                "3. Оставить без изменений\n");
        switch (check) {
            case 1:
                initHumans(1);
                System.out.println("провера всех списков и коэффициентов и зависимостей, в связи с добавлением " +
                        "человека");
                break;
            case 2:
                removeHuman();
                break;
            case 3:
                break;
            default:
                System.out.println("Такого пункта не существует, попробуйте ещё раз");
                check();
                break;
        }
    }

    /**
     * plug
     */
    private void removeHuman() {
        System.out.println("провера всех списков и коэффициентов и зависимостей, в связи с удалением " +
                "человека");
        String humanName = ConsoleHelper.readString("Введите имя человека которрого желаете исключить из сиска");
        humans.remove(new Human(humanName));
    }

    /**
     * Пары генерируются на основе количества набранных очков.
     * Первостепенная задача определить лидеров. Поэтому:
     * - Участники с более высоким рейтингом в первую очередь соревнуются с теми, у кого тоже высокий рейтинг.
     * Если количество бойцов нечетное или если в конце списка образуются бойцы которые уже встречались друг с другом,
     * и невозможно сгенерировать пары, то в таких случаях боец получает бонусные балы и переходит с ледующий тур.
     * Для корректного отображения статистики, в таком случае используется ненастоящий противник - Bot.
     *
     * @see Bot
     * @see PairHuman
     * @see ExceptionGeneratePair
     */
    private void generatePair() {
        List<Human> tempList = new ArrayList<>(humans);
        int k = 1;
        int i = 0;
        while (tempList.size() > 0) {
            try {
                PairHuman pairHuman = new PairHuman(tempList.get(i), tempList.get(k), this);
                addPair(pairHuman);
                tempList.remove(k);
                tempList.remove(i);
                k = i + 1;
            } catch (ExceptionGeneratePair e) {
                k++;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Нету возможности создать пару c " + tempList.get(i) + ", он пропускает тур!");
                tempList.add(new Bot());
            }
        }
    }

    /**
     * Добавляет пару участников в список пар.
     * Если пара попала в список пар, значит она будет играть в этом туре.
     * И после этого уже не сможет больше встретиться в этом турнире.
     * @param pair пара участник 1 и участник 2, которые будут соревноваться
     * @throws ExceptionGeneratePair бросается когда пара уже внесена в список.
     */
    public void addPair(PairHuman pair) throws ExceptionGeneratePair {
        if (!pairs.contains(pair)) {
            pairs.add(pair);
        } else {
            throw new ExceptionGeneratePair();
        }
    }


    public int getPOINT_VICTORY() {
        return POINT_VICTORY;
    }

    public int getPOINT_LOSS() {
        return POINT_LOSS;
    }

    public int getROUND_FOR_WIN() {
        return ROUND_FOR_WIN;
    }

    /**
     * Выводит список пар конкретного тура
     */
    private void printListPairOfTour() {
        for (int i = countPlayedPair; i < pairs.size(); i++) {
            System.out.println(pairs.get(i));
        }
    }

    private void printListHumans() {
        for (Human human: humans) {
            System.out.println(human);
        }
    }
}
