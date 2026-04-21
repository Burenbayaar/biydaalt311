package mn.flashcard.organizer;

import java.util.List;
import mn.flashcard.model.FlashCard;

public interface CardOrganizer {
    List<FlashCard> organize(List<FlashCard> cards);
}
