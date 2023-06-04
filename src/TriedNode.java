class TrieNode {
    private static final int ALPHABET_SIZE = 29; // 27 characters in the Hebrew alphabet
    private final TrieNode[] children;
    private boolean isEndOfWord;

    public TrieNode() {
        children = new TrieNode[ALPHABET_SIZE];
        isEndOfWord = false;
    }

    public void insert(String word) {
        TrieNode current = this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = getAlphabetIndex(c);
            if (current.children[index] == null)
                current.children[index] = new TrieNode();
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode current = this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = getAlphabetIndex(c);
            if (current.children[index] == null)
                return false;
            current = current.children[index];
        }
        return current != null && current.isEndOfWord;
    }

    private int getAlphabetIndex(char c) {
        if (c == '"')
            return 0;
         else if (c == '\'')
            return 1;
        else if (c >= 'א' && c <= 'ת')
            return c - 'א' + 2;
        else
            throw new IllegalArgumentException("Unsupported character: " + c);
    }
}
