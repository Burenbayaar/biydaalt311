package mn.flashcard.cli;

import mn.flashcard.organizer.CardOrganizer;
import mn.flashcard.organizer.RandomSorter;
import mn.flashcard.organizer.RecentMistakesFirstSorter;
import mn.flashcard.organizer.WorstFirstSorter;

public class FlashcardCLI {

    private static final String HELP_TEXT =
            "Хэрэглээ: flashcard <cards-file> [options]\n\n"
            + "Options:\n"
            + "  --help                    Тусламжийн мэдээлэл харуулах\n"
            + "  --order <order>           Дараалал"
            + " [random|worst-first|recent-mistakes-first]\n"
            + "  --repetitions <num>       Нэг картыг хэдэн удаа зөв хариулах"
            + " (default: 1)\n"
            + "  --invertCards             Асуулт хариултыг сольж харуулах"
            + " (default: false)\n";

    private String cardsFile;
    private CardOrganizer organizer = new RandomSorter();
    private int repetitions = 1;
    private boolean invertCards = false;

    public void parse(String[] args) {
        if (args.length == 0) {
            printErrorAndExit("Алдаа: cards-file заавал шаардлагатай.");
        }
        for (String arg : args) {
            if ("--help".equals(arg)) {
                System.out.println(HELP_TEXT);
                System.exit(0);
            }
        }
        cardsFile = args[0];
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--order" -> {
                    if (i + 1 >= args.length) {
                        printErrorAndExit("Алдаа: --order утга шаардана.");
                    }
                    organizer = parseOrder(args[++i]);
                }
                case "--repetitions" -> {
                    if (i + 1 >= args.length) {
                        printErrorAndExit("Алдаа: --repetitions утга шаардана.");
                    }
                    repetitions = parseRepetitions(args[++i]);
                }
                case "--invertCards" -> invertCards = true;
                default -> printErrorAndExit("Алдаа: үл мэдэгдэх сонголт: " + args[i]);
            }
        }
    }

    private CardOrganizer parseOrder(String order) {
        return switch (order) {
            case "random" -> new RandomSorter();
            case "worst-first" -> new WorstFirstSorter();
            case "recent-mistakes-first" -> new RecentMistakesFirstSorter();
            default -> {
                printErrorAndExit("Алдаа: --order утга буруу: '" + order + "'");
                yield null;
            }
        };
    }

    private int parseRepetitions(String value) {
        try {
            int n = Integer.parseInt(value);
            if (n < 1) {
                printErrorAndExit("Алдаа: --repetitions 1-ээс их байх ёстой.");
            }
            return n;
        } catch (NumberFormatException e) {
            printErrorAndExit("Алдаа: --repetitions тоон утга шаардана: '" + value + "'");
            return 1;
        }
    }

    private void printErrorAndExit(String message) {
        System.err.println(message);
        System.err.println("Тусламж: flashcard --help");
        System.exit(1);
    }

    public String getCardsFile()        { return cardsFile; }
    public CardOrganizer getOrganizer() { return organizer; }
    public int getRepetitions()         { return repetitions; }
    public boolean isInvertCards()      { return invertCards; }
}
