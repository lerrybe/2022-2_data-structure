public class BST { // Binary Search Tree implementation
    public Node root;
    public int sumFrequency = 0;
    public int sumProbe = 0;
    public int numKeys = 0;

    public BST() {
        this.root = null;
    }

    public void insert(String key) {
        this.root = insertKey(this.root, key);
    }

    private Node insertKey(Node T, String key) {
        if (T == null) {
            T = new Node(key);
        } else if (key.compareTo(T.key) < 0) {
            T.left = insertKey(T.left, key);
        } else if (key.compareTo(T.key) > 0){
            T.right = insertKey(T.right, key);
        } else {
            T.frequency += 1;
        }
        return T;
    }

    public boolean find(String key) {
        Node result = findKey(this.root, key);
        return result != null;
    }

    private Node findKey(Node T, String key) {
        if (T == null) {
            return null;
        } else if (T.key.compareTo(key) == 0) {
            T.probe += 1;
            return T;
        } else if (T.key.compareTo(key) > 0) {
            T.probe += 1;
            return findKey(T.left, key);
        } else {
            T.probe += 1;
            return findKey(T.right, key);
        }
    }

    public int size() {
        this.numKeys = 0;
        this.inOrderForGetSize(this.root);
        return this.numKeys;
    }

    private void inOrderForGetSize(Node T) {
        if (T == null) return;
        inOrderForGetSize(T.left);
        this.numKeys += 1;
        inOrderForGetSize(T.right);
    }

    public int sumFreq() {
        this.sumFrequency = 0;
        this.inOrderForSumFreq(this.root);
        return this.sumFrequency;
    }

    private void inOrderForSumFreq(Node T) {
        if (T == null) return;
        inOrderForSumFreq(T.left);
        this.sumFrequency += T.frequency;
        inOrderForSumFreq(T.right);
    }

    public int sumProbes() {
        this.sumProbe = 0;
        this.inOrderForSumProbes(this.root);
        return this.sumProbe;
    }

    private void inOrderForSumProbes(Node T) {
        if (T == null) return;
        inOrderForSumProbes(T.left);
        this.sumProbe += T.probe;
        inOrderForSumProbes(T.right);
    }

    public void resetCounters() {
        this.inOrderForReset(this.root);
    }

    private void inOrderForReset(Node T) {
        if (T == null) return;
        inOrderForReset(T.left);
        T.frequency = 1;
        T.probe = 0;
        inOrderForReset(T.right);
    }

    public void print() {
        inOrderForPrint(this.root);
    }

    private void inOrderForPrint(Node T) {
        if (T == null) return;
        inOrderForPrint(T.left);
        System.out.printf("[%s:%d:%d]", T.key, T.frequency, T.probe);
        System.out.println();
        inOrderForPrint(T.right);
    }
}

// References
// lecture PPT slides
// Easy learning data structure
