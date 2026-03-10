import java.util.*;

public class PlagiarismDetectionSystem {

    // n-gram -> set of document IDs
    private HashMap<String, Set<String>> index = new HashMap<>();

    private int N = 5; // using 5-grams

    // Add a document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {

            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }
    }

    // Analyze new document
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (index.containsKey(gram)) {

                for (String otherDoc : index.get(gram)) {

                    matchCount.put(otherDoc,
                            matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + doc + "\"");

            if (similarity > 60)
                System.out.println("Similarity: " + similarity + "% (PLAGIARISM DETECTED)");
            else if (similarity > 10)
                System.out.println("Similarity: " + similarity + "% (suspicious)");
            else
                System.out.println("Similarity: " + similarity + "% (low similarity)");

            System.out.println();
        }
    }

    // Generate n-grams
    private List<String> generateNGrams(String text) {

        List<String> result = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }

    public static void main(String[] args) {

        PlagiarismDetectionSystem system = new PlagiarismDetectionSystem();

        String doc1 = "machine learning is a field of artificial intelligence that focuses on data";
        String doc2 = "machine learning is a field of artificial intelligence used for predictive analysis";

        system.addDocument("essay_089.txt", doc1);
        system.addDocument("essay_092.txt", doc2);

        String newEssay = "machine learning is a field of artificial intelligence that focuses on data science";

        system.analyzeDocument("essay_123.txt", newEssay);
    }
}
