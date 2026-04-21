package mn.flashcard;

import java.io.IOException;
import java.util.List;
import mn.flashcard.achievement.AchievementTracker;
import mn.flashcard.cli.FlashcardCLI;
import mn.flashcard.model.FlashCard;
import mn.flashcard.reader.CardFileReader;
import mn.flashcard.engine.FlashcardEngine;

/**
 * Програмын оролтын цэг.
 */
public class Main {

    public static void main(String[] args) {
        FlashcardCLI cli = new FlashcardCLI();
        cli.parse(args);

        CardFileReader reader = new CardFileReader();
        List<FlashCard> cards;

        try {
            cards = reader.read(cli.getCardsFile());
        } catch (IOException e) {
            System.err.println("Алдаа: файл унших боломжгүй — " + e.getMessage());
            System.exit(1);
            return;
        }

        if (cards.isEmpty()) {
            System.err.println("Алдаа: картын файлд карт олдсонгүй.");
            System.exit(1);
            return;
        }

        AchievementTracker tracker = new AchievementTracker();

        FlashcardEngine engine = new FlashcardEngine(
            cards,
            cli.getOrganizer(),
            cli.getRepetitions(),
            cli.isInvertCards(),
            tracker
        );

        engine.run();
    }
}