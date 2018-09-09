import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    static final int fiveSecondsInMillis = 5000;

    static double randomDouble() // implicitly in range [0,1[
    { return ThreadLocalRandom.current().nextDouble(); }

    static double randomDouble(double upperBoundExclusive)
    { return ThreadLocalRandom.current().nextDouble(upperBoundExclusive); }

    static double randomDouble(double lowerBoundInclusive, double upperBoundExclusive)
    { return ThreadLocalRandom.current().nextDouble(lowerBoundInclusive, upperBoundExclusive); }

    // insert random data into binary tree. for initialization
    static void treeInsert(TreeNode tn, double data) {
        if (data <= tn.data) {
            if (tn.leftChild == null) {
                tn.leftChild = new TreeNode(data, tn, null, null);
            } else {
                treeInsert(tn.leftChild, data);
            }
        } else {
            if (tn.rightChild == null) {
                tn.rightChild = new TreeNode(data, tn, null, null);
            } else {
                treeInsert(tn.rightChild, data);
            }
        }
    }

    // for debugging. "pretty-print" tree.
    static void printTree(TreeNode tn) { _printTree(tn, 0, true); }

    static void _printTree(TreeNode tn, int depth, boolean isLeftChild) {
        if (tn == null)
            return; // base case

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < depth; i++)
            s.append("    "); // indent based on depth in tree
        if (!isLeftChild)
            s.append("_"); // right child starts with '_' underscore character
        s.append(tn.data);
        System.out.println(s);

        _printTree(tn.leftChild, depth+1, true); // always try left children before
        _printTree(tn.rightChild, depth+1, false); // right children
    }
}
