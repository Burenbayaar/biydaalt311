package mn.flashcard.organizer;

import java.util.ArrayList;
import java.util.List;
import mn.flashcard.model.FlashCard;

public class RecentMistakesFirstSorter implements CardOrganizer {

    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> wrong = new ArrayList<>();
        List<FlashCard> correct = new ArrayList<>();

        for (FlashCard card : cards) {
            if (!card.wasLastCorrect()) {
                wrong.add(card);
            } else {
                correct.add(card);
            }
        }

        List<FlashCard> result = new ArrayList<>(wrong);
        result.addAll(correct);
        return result;
    }
}
