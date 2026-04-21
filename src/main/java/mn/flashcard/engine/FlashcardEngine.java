package mn.flashcard.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import mn.flashcard.achievement.AchievementTracker;
import mn.flashcard.model.FlashCard;
import mn.flashcard.organizer.CardOrganizer;

/**
 * Flashcard сургалтын гол loop-г удирдах класс.
 *
 * <p>Алгоритм:
 * <ol>
 *   <li>Картуудыг organizer-р зохион байгуулна.</li>
 *   <li>Хэрэглэгчид асуулт харуулж хариулт хүлээнэ.</li>
 *   <li>Зөв хариулсан тоо {@code repetitions}-д хүрсэн картыг жагсаалтаас хасна.</li>
 *   <li>Бүх карт дууссан үед тойрог дуусна.</li>
 * </ol>
 */
public class FlashcardEngine {

    private static final String DIVIDER = "─".repeat(50);

    private final List<FlashCard> cards;
    private final CardOrganizer organizer;
    private final int repetitions;
    private final boolean invertCards;
    private final AchievementTracker tracker;
    private final Scanner scanner;

    // Картаар хичнээн удаа зөв хариулсныг хянана
    private final Map<FlashCard, Integer> correctCounts = new HashMap<>();

    /**
     * @param cards       бүх карт
     * @param organizer   дараалал тодорхойлогч
     * @param repetitions нэг картыг хэдэн удаа зөв хариулах шаардлагатай
     * @param invertCards асуулт/хариултыг сольж харуулах эсэх
     * @param tracker     амжилт хянагч
     */
    public FlashcardEngine(
            List<FlashCard> cards,
            CardOrganizer organizer,
            int repetitions,
            boolean invertCards,
            AchievementTracker tracker) {
        this.cards = cards;
        this.organizer = organizer;
        this.repetitions = repetitions;
        this.invertCards = invertCards;
        this.tracker = tracker;
        this.scanner = new Scanner(System.in);

        for (FlashCard card : cards) {
            correctCounts.put(card, 0);
        }
    }

    /**
     * Сургалтыг эхлүүлж дуустал ажиллана.
     */
    public void run() {
        printWelcome();

        // Дуусаагүй картуудын ажлын жагсаалт
        List<FlashCard> remaining = new ArrayList<>(cards);
        int round = 1;

        while (!remaining.isEmpty()) {
            System.out.println("\n" + DIVIDER);
            System.out.printf("  Тойрог %d  —  %d карт үлдсэн%n", round, remaining.size());
            System.out.println(DIVIDER);

            // Тойрог бүрт дахин зохион байгуулна
            List<FlashCard> ordered = organizer.organize(remaining);
            List<FlashCard> completedThisRound = new ArrayList<>();

            for (FlashCard card : ordered) {
                askCard(card, completedThisRound);
            }

            // Тойрог дууссан үед амжилт шалгана
            tracker.onRoundEnd(ordered);
            tracker.printNewAchievements();

            // Шаардлагат давталтад хүрсэн картуудыг хасна
            remaining.removeAll(completedThisRound);
            round++;
        }

        printSummary();
    }

    /**
     * Нэг картын асуулт харуулж, хариултыг шалгана.
     */
    private void askCard(FlashCard card, List<FlashCard> completedThisRound) {
        String question = invertCards ? card.getAnswer()   : card.getQuestion();
        String answer   = invertCards ? card.getQuestion() : card.getAnswer();

        System.out.println();
        System.out.println("Асуулт: " + question);
        System.out.print("Хариулт: ");

        long start = System.currentTimeMillis();
        String userAnswer = scanner.nextLine().trim();
        long elapsed = System.currentTimeMillis() - start;

        boolean correct = userAnswer.equalsIgnoreCase(answer);
        card.recordAnswer(correct);
        tracker.record(card, correct, elapsed);

        if (correct) {
            int count = correctCounts.merge(card, 1, Integer::sum);
            System.out.println("✓ Зөв!");

            if (count >= repetitions) {
                completedThisRound.add(card);
                System.out.printf(
                    "  (%d/%d удаа зөв хариуллаа — карт дууслаа)%n",
                    count, repetitions
                );
            } else {
                System.out.printf(
                    "  (%d/%d — дахин асуух болно)%n",
                    count, repetitions
                );
            }
        } else {
            System.out.println("✗ Буруу. Зөв хариулт: " + answer);
            // Буруу хариулсан үед зөв тоолуурыг дахин тэглэнэ
            correctCounts.put(card, 0);
        }
    }

    /**
     * Сургалт дууссаны дараах нийт үр дүнг хэвлэнэ.
     */
    private void printSummary() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  Сургалт дууслаа!");
        System.out.println(DIVIDER);

        int totalCorrect   = cards.stream().mapToInt(FlashCard::getCorrectCount).sum();
        int totalAttempts  = cards.stream()
                .mapToInt(c -> c.getCorrectCount() + c.getIncorrectCount())
                .sum();
        int percentage     = totalAttempts == 0 ? 0 : totalCorrect * 100 / totalAttempts;

        System.out.printf("  Нийт хариулсан : %d%n",  totalAttempts);
        System.out.printf("  Зөв хариулт    : %d%n",  totalCorrect);
        System.out.printf("  Зөв хувь       : %d%%%n", percentage);
        System.out.println();

        System.out.println("  Картаар дэлгэрэнгүй:");
        System.out.printf("  %-40s %6s %6s%n", "Асуулт", "Зөв", "Буруу");
        System.out.println("  " + "─".repeat(55));

        for (FlashCard card : cards) {
            String q = card.getQuestion();
            if (q.length() > 38) {
                q = q.substring(0, 35) + "...";
            }
            System.out.printf(
                "  %-40s %6d %6d%n",
                q,
                card.getCorrectCount(),
                card.getIncorrectCount()
            );
        }

        // Нээгдсэн бүх амжилтуудыг хэвлэнэ
        List<AchievementTracker.Achievement> achieved = tracker.getUnlocked();
        if (!achieved.isEmpty()) {
            System.out.println();
            System.out.println("  🏆 Нийт нээгдсэн амжилтууд:");
            for (AchievementTracker.Achievement a : achieved) {
                System.out.printf("     • %s — %s%n", a.getTitle(), a.getDescription());
            }
        }

        System.out.println(DIVIDER);
    }

    private void printWelcome() {
        System.out.println(DIVIDER);
        System.out.println("  Flashcard сургалтанд тавтай морил!");
        System.out.printf("  Нийт карт    : %d%n", cards.size());
        System.out.printf("  Давталт      : %d%n", repetitions);
        System.out.printf("  Карт эргүүлэх: %s%n", invertCards ? "тийм" : "үгүй");
        System.out.println("  Гарахыг хүсвэл Ctrl+C дарна уу.");
        System.out.println(DIVIDER);
    }
}