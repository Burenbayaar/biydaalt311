package mn.flashcard.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mn.flashcard.model.FlashCard;

public class CardFileReader {
    public List<FlashCard> read(String filePath) throws IOException {
        List<FlashCard> cards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String question = null;
            String answer = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Q: ")) {
                    question = line.substring(3).trim();
                } else if (line.startsWith("A: ")) {
                    answer = line.substring(3).trim();
                }
                if (question != null && answer != null) {
                    cards.add(new FlashCard(question, answer));
                    question = null;
                    answer = null;
                }
            }
        }
        return cards;
    }
}
