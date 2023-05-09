public class AVL extends BST {
    private final String LL = "LL", LR = "LR", RR = "RR", RL = "RL";
    private final String PASS = "PASS";

    public AVL() {}

    @Override
    public void insert(String key) {
        this.root = insertAVLKey(this.root, key);
    }

    private Node insertAVLKey(Node T, String key) {
        String type;
        if (T == null) {
            T = new Node(key);
        } else if (key.compareTo(T.key) < 0) {
            // CASE: key < T.key
            T.left = insertAVLKey(T.left, key);
            T.setHeightOf(1 + Math.max(T.getHeightOf(T.left), T.getHeightOf(T.right)));
            type = getType(T);
            if (!type.equals(PASS)) {
                T = getFixedTree(T, type);
            }
        } else if (key.compareTo(T.key) > 0) {
            // CASE: key > T.key
            T.right = insertAVLKey(T.right, key);
            T.setHeightOf(1 + Math.max(T.getHeightOf(T.left), T.getHeightOf(T.right)));
            type = getType(T);
            if (!type.equals(PASS)) {
                T = getFixedTree(T, type);
            }
        } else {
            // CASE: key == T.key
            T.frequency += 1;
        }
        return T;
    }

     private Node getFixedTree(Node T, String type) {
        Node fixedTree;

        if (type.equals(RR)) {
            fixedTree = rotateLeft(T);
        } else if (type.equals(RL)) {
            T.right = rotateRight(T.right);
            fixedTree = rotateLeft(T);
        } else if (type.equals(LL)) {
            fixedTree = rotateRight(T);
        } else if (type.equals(LR)) {
            T.left = rotateLeft((T.left));
            fixedTree = rotateRight(T);
        } else {
            fixedTree = null;
        }
        return fixedTree;
     }

    private String getType(Node T) {
        String type;
        if (T.getHeightOf(T.left) - T.getHeightOf(T.right) >= 2) {
            // CASE: LL or LR
            if (T.getHeightOf(T.left.left) >= T.getHeightOf(T.left.right)) {
                type = LL;
            } else {
                type = LR;
            }
        } else if (T.getHeightOf(T.right) - T.getHeightOf(T.left) >= 2) {
            // CASE: RR or RL
            if (T.getHeightOf(T.right.right) >= T.getHeightOf((T.right.left))) {
                type = RR;
            } else {
                type = RL;
            }
        } else {
            // CASE: PASS
            type = PASS;
        }
        return type;
    }

     private Node rotateRight(Node T) {
        Node tmpLeft = T.left;
        if (tmpLeft == null) return null;
        Node tmpLeftRight = tmpLeft.right;
        tmpLeft.right = T;
        T.left = tmpLeftRight;

        T.setHeightOf(1 + Math.max(T.getHeightOf(T.left), T.getHeightOf(T.right)));
        tmpLeft.setHeightOf(1 + Math.max(T.getHeightOf(tmpLeft.left), T.getHeightOf(tmpLeft.right)));
        return tmpLeft;
     }

    private Node rotateLeft(Node T) {
        Node tmpRight = T.right;
        if (tmpRight == null) return null;
        Node tmpRightLeft = tmpRight.left;
        tmpRight.left = T;
        T.right = tmpRightLeft;

        T.setHeightOf(1 + Math.max(T.getHeightOf(T.right), T.getHeightOf(T.left)));
        tmpRight.setHeightOf(1 + Math.max(T.getHeightOf(tmpRight.right), T.getHeightOf(tmpRight.left)));
        return tmpRight;
    }
}
