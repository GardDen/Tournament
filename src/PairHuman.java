/**
 * Класс представляет пару участников Human, которые будет соревноваться друг с другом
 * Created by 1 on 02.01.2018.
 */
public class PairHuman {
    private Human human1;
    private Human human2;
    boolean win = false;
    private Tournament tournament;

    public PairHuman(Human human1, Human human2, Tournament tournament) {
        this.human1 = human1;
        this.human2 = human2;
        this.tournament = tournament;
        human1.setCurrentScoreOfRound(0);
        human2.setCurrentScoreOfRound(0);
    }

    /**
     * Запускает процесс раунда между двумя участниками.
     */
    public void play() {
        int round = 1;
        while (!isWin()) {
            int score;
            System.out.println("Раунд " + round + "." + this);
            if (human2 instanceof Bot) {
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                human1.addPoint();
                win = true;
            } else {
                while ((score = ConsoleHelper.readInt("Введите цифру 1, если победил первый боец, иначе введите 0:")) != 0
                        && score != 1) {
                    System.out.println("Неверный ввод результата раунда.");
                }
                if (score == 1) {
                    human1.addPoint();
                } else {
                    human2.addPoint();
                }
            }
            round++;
        }
        human1.addScores(human1.getCurrentScoreOfRound());
        human2.addScores(human2.getCurrentScoreOfRound());
    }

    /**
     * @return true если один из участников раунда достиг необходимого количества побед ROUND_FOR_WIN.
     * @see Tournament  - ROUND_FOR_WIN
     */
    public boolean isWin() {
        if (win || human1.getCurrentScoreOfRound() == tournament.getROUND_FOR_WIN()
                || human2.getCurrentScoreOfRound() == tournament.getROUND_FOR_WIN()) {
            return true;
        } else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairHuman)) return false;

        PairHuman pairHuman = (PairHuman) o;

        if (!this.human1.equals(pairHuman.human1) && !this.human1.equals(pairHuman.human2)) return false;
        return this.human2.equals(pairHuman.human2) || this.human2.equals(pairHuman.human1);

    }

    @Override
    public int hashCode() {
        int result = human1.hashCode();
        result = 31 * result + human2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return human1 + " vs " + human2;
    }
}
