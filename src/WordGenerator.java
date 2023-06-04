import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordGenerator {
    private final TrieNode trie;

    public WordGenerator(String filePath) {
        trie = new TrieNode();
        readWordsFromFile(filePath);
    }

    public static void main(String[] args) {
        WordGenerator wordGenerator = new WordGenerator("C:\\Users\\יאיר\\Desktop\\database.txt");

        String word = wordGenerator.getLargestKnownWord();

        List<String> options = wordGenerator.generateWords(word);
        if (options.isEmpty()) {
            System.out.println("No options found.");
        } else {
            System.out.println("Options:");
            for (String option : options) {
                System.out.println(option);
            }
        }
    }

    private void readWordsFromFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim();
                if (!word.isEmpty())
                    trie.insert(word);
            }
            reader.close();


        } catch (IOException e) {
            System.out.println("Error reading the word database file: " + e.getMessage());
        }
    }

    public String getLargestKnownWord() {
        // Prompt the user to enter the known words
        System.out.print("Enter the largest word you already know: ");
        Scanner scanner = new Scanner(System.in);
        String knownWord = scanner.nextLine();
        String normalizedWord = knownWord.replaceAll("[^א-ת]", "");
        if (!normalizedWord.isEmpty())
            trie.insert(normalizedWord);

        return normalizedWord;
    }

    public List<String> generateWords(String inputWord) {
        Set<String> generatedWordsSet = new HashSet<>(); // Use a Set to store unique words

        // Step 1: Generate all possible combinations of the known word letters
        List<String> combinations = generateCombinations(inputWord);

        // Step 2: Loop through each combination
        for (String combination : combinations) {
            // Step 3: Add a Hebrew letter to the beginning
            for (char c = 'א'; c <= 'ת'; c++) {
                String newWord = c + combination;
                if (newWord.length() == inputWord.length() + 1 && trie.search(newWord))
                    generatedWordsSet.add(newWord);
            }

            // Step 4: Add a Hebrew letter to the end
            for (char c = 'א'; c <= 'ת'; c++) {
                String newWord = combination + c;
                if (newWord.length() == inputWord.length() + 1 && trie.search(newWord))
                    generatedWordsSet.add(newWord);
            }

            // Step 5: Add a Hebrew letter in the middle
            for (int i = 0; i < combination.length(); i++) {
                for (char c = 'א'; c <= 'ת'; c++) {
                    String newWord = combination.substring(0, i) + c + combination.substring(i);
                    if (newWord.length() == inputWord.length() + 1 && trie.search(newWord))
                        generatedWordsSet.add(newWord);
                }
            }
        }

        // Convert the Set to a List and return it
        return new ArrayList<>(generatedWordsSet);
    }

    private List<String> generateCombinations(String word) {
        List<String> combinations = new ArrayList<>();
        generateCombinationsHelper(word.toCharArray(), 0, combinations);
        return combinations;
    }

    private void generateCombinationsHelper(char[] letters, int start, List<String> combinations) {
        combinations.add(new String(letters, 0, start)); // Add the current combination

        if (start == letters.length)
            return;

        for (int i = start; i < letters.length; i++) {
            swap(letters, start, i);
            generateCombinationsHelper(letters, start + 1, combinations);
            swap(letters, start, i); // backtrack
        }
    }

    private void swap(char[] letters, int i, int j) {
        char temp = letters[i];
        letters[i] = letters[j];
        letters[j] = temp;
    }
}