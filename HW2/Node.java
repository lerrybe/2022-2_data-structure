public class Node {
    public String key;
    public Node left;
    public Node right;
    private int height;
    public int frequency = 1;
    public int probe = 0;

    public Node(String key) {
        this.key = key;
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    public Node(String key, Node left, Node right) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.height = 1;
    }

    public Node(String key, Node left, Node right, int height) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.height = height;
    }

    public int getHeightOf(Node T) {
        // Check null node's height
        if (T == null) return 0;
        else return T.height;
    }

    public void setHeightOf(int height) {
        this.height = height;
    }
}
