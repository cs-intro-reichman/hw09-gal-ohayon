import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {
    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    private ArrayList<String> keysInOrder;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
        keysInOrder = new ArrayList<String>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
        keysInOrder = new ArrayList<String>();
    }

    public void train(String fileName) {
        CharDataMap.clear();
        keysInOrder.clear();

        In in = new In(fileName);
        String corpus = in.readAll();

        for (int i = 0; i <= corpus.length() - windowLength - 1; i++) {
            String window = corpus.substring(i, i + windowLength);
            char nextChar = corpus.charAt(i + windowLength);

            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
                keysInOrder.add(window);
            }
            probs.update(nextChar);
        }

        for (List probs : CharDataMap.values()) calculateProbabilities(probs);
    }

    void calculateProbabilities(List probs) {
        int totalCounts = 0;
        for (int i = 0; i < probs.getSize(); i++)
            totalCounts += probs.get(i).count;

        double cumulativeProb = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCounts;
            cumulativeProb += cd.p;
            cd.cp = cumulativeProb;
        }
        if (probs.getSize() > 0) probs.get(probs.getSize() - 1).cp = 1.0;
    }

    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            if (r < cd.cp) return cd.chr;
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) return initialText;

        StringBuilder sb = new StringBuilder(initialText);
        int targetLength = initialText.length() + textLength;

        while (sb.length() < targetLength) {
            String window = sb.substring(sb.length() - windowLength);
            List probs = CharDataMap.get(window);
            if (probs == null) break;
            char nextChar = getRandomChar(probs);
            sb.append(nextChar);
        }

        return sb.toString();
    }

    public String toString() {
        StringBuilder out = new StringBuilder();

        for (int k = 0; k < keysInOrder.size(); k++) {
            String key = keysInOrder.get(k);
            out.append(key).append(" : ((");

            List probs = CharDataMap.get(key);
            for (int i = 0; i < probs.getSize(); i++) {
                String s = probs.get(i).toString();
                if (s.length() >= 2 && s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
                    s = s.substring(1, s.length() - 1);
                }
                out.append(s);
                if (i < probs.getSize() - 1) out.append("\n");
            }

            out.append("))\n");
        }

        return out.toString();
    }
}
