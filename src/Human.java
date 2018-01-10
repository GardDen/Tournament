import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 02.01.2018.
 * Участник.
 */
public class Human implements Comparable {
    protected String name;
    private int score = 0;
    private Map<Human, Boolean> map = new HashMap<>();
    private int currentScoreOfRound = 0;

    public Human() {
    }

    public Human(String name) {
        this.name = name;
    }

    /**
     * Добавляет очки набранные во время встречи с другим участником.
     * @param currentScoreOfRound
     */
    public void addScores(int currentScoreOfRound) {
        score += currentScoreOfRound;
    }

    public void addPoint() {
        currentScoreOfRound++;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Human)) return false;

        Human human = (Human) o;

        return name.equals(human.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "<" + name + ">(" + score + ")";
    }

    @Override
    public int compareTo(Object o) {
        int result = ((Human)o).score - this.score;
        if (result == 0) {
            result = -((Human) o).name.compareTo(this.name);
        }
        return result;
    }

    public int getCurrentScoreOfRound() {
        return currentScoreOfRound;
    }

    public void setCurrentScoreOfRound(int currentScoreOfRound) {
        this.currentScoreOfRound = currentScoreOfRound;
    }
}
