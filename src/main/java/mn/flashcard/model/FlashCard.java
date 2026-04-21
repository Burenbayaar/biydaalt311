package mn.flashcard.model;

public class FlashCard {
    private final String question;
    private final String answer;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private boolean lastAnswerCorrect = false;

    public FlashCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public void recordAnswer(boolean correct) {
        if (correct) correctCount++;
        else incorrectCount++;
        lastAnswerCorrect = correct;
    }

    public String getQuestion()     { return question; }
    public String getAnswer()       { return answer; }
    public int getCorrectCount()    { return correctCount; }
    public int getIncorrectCount()  { return incorrectCount; }
    public boolean wasLastCorrect() { return lastAnswerCorrect; }
}
