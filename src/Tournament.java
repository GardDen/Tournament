import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 02.01.2018.
 */
public class Tournament {
    public final static int NUMBER_ROUND_FOR_WIN = 2;
    private final int MAX_TOUR = 4;// для конкретного турнира берем 4 тура, так как на 5 туров времени может не хватить
    //в то же время 4 туров должно быть достаточно для составления общего рейтинга
    private int countPlayedPair = 0;
    private int tour;
    private String name;
    private Date date;
    private List<Human> listHumans;
    private List<PairHuman> listPairs = new ArrayList<>();
    
    public Tournament(String name) {
        this.name = name;
        date = new Date();
        tour = 1;
        initMapHumans(ConsoleHelper.readInt("Введите число участников турнира: "));
    }

    public void initMapHumans(int countHumans) {
        listHumans = new ArrayList<>(countHumans);
        for (int i = 0; i < countHumans; i++) {
            addHuman("Введите имя(фамилию и имя) участника турнира: ");
        }
    }

    public Human addHuman(String info) {
        String name = ConsoleHelper.readString(info);
        Human human = new Human(name);
        if (!listHumans.contains(human)) {
            listHumans.add(human);
        } else {
            human = addHuman("Такой человек уже зарегестрирован, попробуйте ещё раз:");
        }
        return human;
    }

    public void start() {
        while (tour <= MAX_TOUR) {
            Collections.sort(listHumans);
            startTour();
            tour++;
        }
        Collections.sort(listHumans);
        printListHumans();
    }

    private void startTour() {
        System.out.println("Тур " + tour);
        printListHumans();
        generatePair();
        printListPairOfTour();
        for (int i = countPlayedPair; i < listPairs.size(); i++) {
            listPairs.get(i).play();
        }
        countPlayedPair = listPairs.size();
    }

    /**
     * Пары генерируются на основе их рейтинга(количества набранных очков)
     * Первостепенная задача определить лидеров. Поэтому:
     * Участники с более высоким рейтингом в первую очередь оревнуются с теми у кого тоже высокий рейтинг.
     * Во вторую очередь соревнуются с теми у кого рейтинг чуть ниже.
     * Два участники встречаются лишь однажды.
     * Если количество боцов нечетное или если в конце списка образуются бойцы которые уже встречались друг с другом,
     * и невозможно сгенерировать пары, то в таких случаях боец получает бонусные балы и переходит с ледующий тур.
     * Учесть нечетное количество бойцов!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private void generatePair() {
        List<Human> tempList = new ArrayList<>(listHumans);
        int k = 1;
        int i = 0;
        while (tempList.size() > 0) {
            try {
                PairHuman pairHuman = new PairHuman(tempList.get(i), tempList.get(k));
                addPair(pairHuman);
                //System.out.println("Добавили) - >>>" + pairHuman);
                tempList.remove(k);
                tempList.remove(i);
                k = i + 1;
            } catch (ExceptionGeneratePair e) {
                k++;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Нету возможности создать пару c " + tempList.get(i) + ", он пропускает тур!");
                tempList.add(new Bot());//чтобы отобразить результаты используем костыль бот - мнимый противник
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
        if (!listPairs.contains(pair)) {
            listPairs.add(pair);
        } else {
            throw new ExceptionGeneratePair();
        }
    }

    /**
     * Выводит список пар конкретного тура
     */
    private void printListPairOfTour() {
        for (int i = countPlayedPair; i < listPairs.size(); i++) {
            System.out.println(listPairs.get(i));
        }
    }

    private void printListHumans() {
        for (Human human: listHumans) {
            System.out.println(human);
        }
    }

    public List<Human> getListHumans() {
        return listHumans;
    }
}
