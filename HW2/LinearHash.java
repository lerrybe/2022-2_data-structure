import java.util.*;

public class LinearHash {
    int M;
    int splitIdx;
    int HTinitSize;
    int currentSize;
    int countWords = 0;

    ArrayList<LinkedList<String>> table;

    // DEFAULT constructor for the Hash table
    public LinearHash(int HTinitSize) {
        this.HTinitSize = HTinitSize;
        this.M = HTinitSize;
        this.splitIdx = 0;
        this.currentSize = this.M;

        this.table = new ArrayList<>();

        for (int i = 0; i < this.M; i++) {
            LinkedList<String> initialLinkedList = new LinkedList<>();
            table.add(initialLinkedList);
        }
    }

    // DEFAULT insert `word' to the Hash table.
    public int insertUnique(String word) {
        int result = -1;
        if (lookup(word) > 0) {
            return result;
        }

        int insertIdx = (int) MyUtil.ELFhash(word, this.M);

        if (insertIdx < this.splitIdx) {
            insertIdx = (int) MyUtil.ELFhash(word, 2 * this.M);
        }

        result = insertWithLinearHashing(word, insertIdx);
        return result;
    }

    private int insertWithLinearHashing(String word, int insertIdx) {
        int result = -1;

        if (table.get(insertIdx).isEmpty()) {
            // CASE: empty linkedList (add without collision)
            table.get(insertIdx).add(word);
            result = insertIdx;
            this.countWords += 1;
        } else {
            // CASE: not empty linkedList (add with collision)
            // 1. add new entry
            LinkedList<String> initialLinkedList = new LinkedList<>();
            table.add(initialLinkedList);
            this.currentSize += 1;

            // 2. add word
            table.get(insertIdx).add(word);
            result = insertIdx;
            this.countWords += 1;

            // 3. rehash words
            if (!table.get(splitIdx).isEmpty()) {
                int splitEntrySize = table.get(splitIdx).size();

                for (int i = 0; i < splitEntrySize; i++) {
                    String targetWord = table.get(splitIdx).remove(0);
                    int rehashIdx = (int) MyUtil.ELFhash(targetWord, 2 * this.M);
                    table.get(rehashIdx).add(targetWord);
                }
            }
            this.splitIdx += 1;

            if (splitIdx >= this.M) {
                this.M *= 2;
                this.splitIdx = 0;
            }
        }
        return result;
    }

    // DEFAULT look up `word' in the Hash table.
    public int lookup(String word) {
        int targetHashEntry = (int) MyUtil.ELFhash(word, this.M);
        if (targetHashEntry >= this.splitIdx) {
            // split region
            if (table.get(targetHashEntry).contains(word)) {
                return table.get(targetHashEntry).size();
            } else {
                return (-1) * table.get(targetHashEntry).size();
            }
        } else {
            targetHashEntry = (int) MyUtil.ELFhash(word, 2 * this.M);
            if (table.get(targetHashEntry).contains(word)) {
                return table.get(targetHashEntry).size();
            } else {
                return (-1) * table.get(targetHashEntry).size();
            }
        }
    }

    // DEFAULT
    public int wordCount() {
        return this.countWords;
    }

    // DEFAULT
    public int emptyCount() {
        int cnt = 0;
        for (LinkedList<String> strings : table) {
            if (strings.isEmpty()) {
                cnt += 1;
            }
        }
        return cnt;
    }

    // DEFAULT 2^k + collisions in the current round
    public int size() {
        return this.M + splitIdx;
    }

    // DEFAULT Print keys in the hash table
    public void print() {
        for (int i = 0; i < table.size(); i++) {
            LinkedList<String> list = table.get(i);
            list.sort(new Comparator<String>() {
                @Override
                public int compare(String word1, String word2) {
                    return word1.compareTo(word2);
                }
            });
            // is Empty Entry
            if (list.isEmpty()) {
                System.out.println("[" + i + ":]");
            // is not Empty Entry
            } else {
                System.out.print("[" + i + ":");
                for (String word: list) {
                    System.out.print(" " + word);
                }
                System.out.println("]");
            }
        }
    }
}
