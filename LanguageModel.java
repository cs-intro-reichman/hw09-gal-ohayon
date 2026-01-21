import java.util.HashMap;
import java.util.Random;

public class LanguageModel {
    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    public void train(String fileName) {
        In in = new In(fileName);
        String corpus = in.readAll();
        for (int i = 0; i <= corpus.length() - windowLength - 1; i++) {
            String window = corpus.substring(i, i + windowLength);
            char nextChar = corpus.charAt(i + windowLength);
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(nextChar);
        }
        for (List probs : CharDataMap.values()) calculateProbabilities(probs);
    }

    void calculateProbabilities(List probs) {
        int totalCounts = 0;
        for (int i = 0; i < probs.getSize(); i++) totalCounts += probs.get(i).count;
        double cumulativeProb = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCounts;
            cumulativeProb += cd.p;
            cd.cp = cumulativeProb;
        }
    }

    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            if (cd.cp >= r) return cd.chr;
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) return initialText;
        StringBuilder sb = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - windowLength);
        while (sb.length() < textLength + initialText.length()) {
            List probs = CharDataMap.get(window);
            if (probs == null) break;
            char nextChar = getRandomChar(probs);
            sb.append(nextChar);
            window = sb.substring(sb.length() - windowLength);
        }
        return sb.toString();
    }
}