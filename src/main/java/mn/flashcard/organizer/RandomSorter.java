package mn.flashcard.organizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mn.flashcard.model.FlashCard;

public class RandomSorter implements CardOrganizer {
    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> shuffled = new ArrayList<>(cards);
        Collections.shuffle(shuffled);
        return shuffled;
    }
}
