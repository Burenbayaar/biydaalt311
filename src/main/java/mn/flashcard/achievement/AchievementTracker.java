package mn.flashcard.achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mn.flashcard.model.FlashCard;

public class AchievementTracker {

    public enum Achievement {
        SPEED("Хурдан!", "Тойрогт дундаж 5 сек доош хариулсан"),
        CORRECT("Бүгдийг зөв!", "Сүүлийн тойрогт бүх карт зөв"),
        REPEAT("Тэвчээртэй!", "Нэг картад 5-аас олон удаа хариулсан"),
        CONFIDENT("Итгэлтэй!", "Нэг картад 3+ удаа зөв хариулсан");

        private final String title;
        private final String description;

        Achievement(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle()       { return title; }
        public String getDescription() { return description; }
    }

    private static final int SPEED_THRESHOLD_MS  = 5000;
    private static final int REPEAT_THRESHOLD    = 5;
    private static final int CONFIDENT_THRESHOLD = 3;

    private final List<Long> roundTimes = new ArrayList<>();
    private final Map<String, Integer> attemptCounts = new HashMap<>();
    private final List<Achievement> unlocked = new ArrayList<>();

    public void record(FlashCard card, boolean correct, long elapsedMs) {
        roundTimes.add(elapsedMs);
        int attempts = attemptCounts.merge(card.getQuestion(), 1, Integer::sum);
        if (attempts >= REPEAT_THRESHOLD) {
            unlock(Achievement.REPEAT);
        }
        if (correct && card.getCorrectCount() >= CONFIDENT_THRESHOLD) {
            unlock(Achievement.CONFIDENT);
        }
    }

    public void onRoundEnd(List<FlashCard> cards) {
        if (!roundTimes.isEmpty()) {
            double avg = roundTimes.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0);
            if (avg < SPEED_THRESHOLD_MS) {
                unlock(Achievement.SPEED);
            }
        }
        boolean allCorrect = cards.stream().allMatch(FlashCard::wasLastCorrect);
        if (allCorrect) {
            unlock(Achievement.CORRECT);
        }
        roundTimes.clear();
    }

    public void printNewAchievements() {
        for (Achievement a : unlocked) {
            System.out.println("\n*** Амжилт нээгдлээ: " + a.getTitle());
            System.out.println("    " + a.getDescription());
        }
    }

    public List<Achievement> getUnlocked() {
        return List.copyOf(unlocked);
    }

    private void unlock(Achievement achievement) {
        if (!unlocked.contains(achievement)) {
            unlocked.add(achievement);
        }
    }
}
