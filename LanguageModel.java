import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of character data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
    private Random randomGenerator;

    /** Constructs a language model with the given window length and a given seed. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
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

        // לאחר בניית המפה, מחשבים הסתברויות לכל רשימה
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    // Computes and sets the probabilities (p and cp fields) of all the characters.
    void calculateProbabilities(List probs) {
        int totalCounts = 0;
        // שלב א: ספירת סך כל המופעים ברשימה
        for (int i = 0; i < probs.getSize(); i++) {
            totalCounts += probs.get(i).count;
        }

        // שלב ב: חישוב p ו-cp (הסתברות מצטברת)
        double cumulativeProb = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCounts;
            cumulativeProb += cd.p;
            cd.cp = cumulativeProb;
        }
    }

    // Returns a random character from the given probabilities list.
    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            if (cd.cp > r) {
                return cd.chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr; // Fallback
    }

    /** Generates a random text, based on the learned probabilities. */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }

        StringBuilder generatedText = new StringBuilder(initialText);
        String currentWindow = initialText.substring(initialText.length() - windowLength);

        while (generatedText.length() < textLength + initialText.length()) {
            List probs = CharDataMap.get(currentWindow);
            
            if (probs == null) {
                break; // אם החלון לא קיים במודל, עוצרים
            }
            
            char nextChar = getRandomChar(probs);
            generatedText.append(nextChar);
            
            // עדכון החלון: לוקחים את סוף המחרוזת הנוכחית
            currentWindow = generatedText.substring(generatedText.length() - windowLength);
        }

        return generatedText.toString();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            List keyProbs = CharDataMap.get(key);
            str.append(key + " : " + keyProbs + "\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        // דוגמה להרצה:
        int windowLen = Integer.parseInt(args[0]);
        String seedText = args[1];
        int genLength = Integer.parseInt(args[2]);
        String fileName = args[3];

        LanguageModel lm = new LanguageModel(windowLen);
        lm.train(fileName);
        System.out.println(lm.generate(seedText, genLength));
    }
}