package mn.flashcard.organizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mn.flashcard.model.FlashCard;

public class WorstFirstSorter implements CardOrganizer {
    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparingInt(FlashCard::getIncorrectCount).reversed());
        return sorted;
    }
}
