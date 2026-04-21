package mn.flashcard.organizer;

import java.util.List;
import mn.flashcard.model.FlashCard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentMistakesFirstSorterTest {

    private final RecentMistakesFirstSorter sorter = new RecentMistakesFirstSorter();

    @Test
    void wrongCardsComesFirst() {
        FlashCard correct = new FlashCard("Q1", "A1");
        FlashCard wrong = new FlashCard("Q2", "A2");
        correct.recordAnswer(true);
        wrong.recordAnswer(false);
        List<FlashCard> result = sorter.organize(List.of(correct, wrong));
        assertEquals(wrong, result.get(0));
        assertEquals(correct, result.get(1));
    }

    @Test
    void allCorrectOrderUnchanged() {
        FlashCard card1 = new FlashCard("Q1", "A1");
        FlashCard card2 = new FlashCard("Q2", "A2");
        card1.recordAnswer(true);
        card2.recordAnswer(true);
        List<FlashCard> result = sorter.organize(List.of(card1, card2));
        assertEquals(card1, result.get(0));
        assertEquals(card2, result.get(1));
    }

    @Test
    void emptyListReturnsEmpty() {
        List<FlashCard> result = sorter.organize(List.of());
        assertTrue(result.isEmpty());
    }
}