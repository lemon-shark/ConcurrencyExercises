import java.util.concurrent.ThreadLocalRandom;

public class q2 {

    static final int numStartingNodes = 15; // number of nodes in initial tree

    static TreeNode root;


    /**
     * convenience methods
     */

    // more typing now, less typing later :)
    static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
    static double randomDouble(double lowerBoundInclusive, double upperBoundExclusive) {
        return ThreadLocalRandom.current().nextDouble(lowerBoundInclusive, upperBoundExclusive);
    }

    // binary search tree insert. recursive. assumes tn is not null
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


    /**
     * the main method
     */

    public static void main(String[] args) {
        root = new TreeNode(randomDouble(), null, null, null);

        for (int i = 0; i < numStartingNodes; i++) {
            treeInsert(root, randomDouble());
        }

        class TreePrinter extends Thread {
            @Override
            public void run() {
                //
            }
        }
        class TreeModifier extends Thread {
            @Override
            public void run() {
                //
            }
        }

        Thread[] threads = new Thread[3];
        threads[2] = new TreeModifier();
        for (int i = 0; i < 2; i++) threads[i] = new TreePrinter();

        for (int i = 0; i < 3; i++) threads[i].start();

        try {
            for (int i = 0; i < 3; i++) threads[i].join();
        } catch(Exception e){
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
    }
}

class TreeNode {
    volatile double data;
    volatile TreeNode parent, leftChild, rightChild;
    TreeNode(double data, TreeNode parent, TreeNode leftChild, TreeNode rightChild) {
        this.data = data;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}
