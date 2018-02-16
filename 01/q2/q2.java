import java.util.concurrent.ThreadLocalRandom;
import java.lang.System.*;

/**
 * This class implements the multithreaded tree access program described in the PDF of
 * Assignment 1.
 *
 * - There is a binary search tree whose data are doubles.
 * - There are two Traversal threads and one Modifier thread.
 * - The Traversal threads do an inorder traversal, recording their path in local strings. When they
 *   finish a traversal, they record a newline and wait 5-20ms and then restart.  They repeat this
 *   for 5 seconds.
 * - The Modifier thread randomly traverse a single branch of the tree.  Where applicable, it
 *   modifies nodes in the tree with predeterined probabilities.  if it modifies a node or reaches
 *   a leaf, it restarts after waiting 1-5ms.  It repeats for 5 seconds.  If it finishes before
 *   the other two threads, it joins on them.
 * - When all is said and done, the Modifier thread prints the strings the other threads stored.
 *
 * In this implementation, the Modifier thread is the main thread.
 */
public class q2 {

    static final int numStartingNodes = 15; // number of nodes in initial tree
    static final long fiveSecondsInMillis = 5000; // number of milliseconds in 5 seconds

    static TreeNode root;


    /**
     * convenience methods
     */

    // java makin me work hard for my random doubles
    static double randomDouble()
    { return ThreadLocalRandom.current().nextDouble(); }
    static double randomDouble(double upperBoundExclusive)
    { return ThreadLocalRandom.current().nextDouble(upperBoundExclusive); }
    static double randomDouble(double lowerBoundInclusive, double upperBoundExclusive)
    { return ThreadLocalRandom.current().nextDouble(lowerBoundInclusive, upperBoundExclusive); }

    // binary search tree insert
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
    static void treePrint(TreeNode tn, int n, boolean left) {
        if (tn == null)
            return;

        String s = "";
        for (int i = 0; i < n; i++)
            s += "    ";
        if (!left) s += "_";
        System.out.println(s + tn.data);

        treePrint(tn.leftChild, n+1, true);
        treePrint(tn.rightChild, n+1, false);
    }


    /**
     * main method
     */
    public static void main(String[] args) {
        try {
            root = new TreeNode(randomDouble(), null, null, null);
            for (int i = 0; i < numStartingNodes; i++)
                treeInsert(root, randomDouble());

            treePrint(root, 0, true);
            System.out.println();

            // inorder tree traversal recording path as string
            class TreePrinter extends Thread {
                StringBuilder str = new StringBuilder();
                @Override
                public void run() { // repeatedly traverse the tree for 5 seconds
                    long startTime = System.currentTimeMillis();

                    int down = 0;
                    int upFromLeft = 1;
                    int upFromRight = 2;

                    int direction = down;
                    TreeNode curr = root;
                    while (System.currentTimeMillis() - startTime < fiveSecondsInMillis) {
                        try { Thread.sleep((long) randomDouble(5, 20)); }
                        catch(Exception e) { e.printStackTrace(); }

                        if (direction == down) {
                            if (curr.leftChild != null) {
                                curr = curr.leftChild;
                            } else {
                                direction = upFromLeft;
                            }
                        } else if (direction == upFromLeft) {
                            str.append(curr.data + " ");
                            if (curr.rightChild != null) {
                                curr = curr.rightChild;
                                direction = down;
                            } else {
                                direction = upFromRight;
                            }
                        } else if (direction == upFromRight) {
                            if (curr == root) {
                                direction = down;
                                str.append("\n");
                                continue;
                            }

                            if (curr == curr.parent.leftChild) {
                                direction = upFromLeft;
                            } else if (curr == curr.parent.rightChild) {
                                direction = upFromRight;
                            }
                            curr = curr.parent;
                        }
                    }
                }
            }

            TreePrinter[] ts = new TreePrinter[] { new TreePrinter(), new TreePrinter() };
            for (int i = 0; i < ts.length; i++)
                ts[i].start();

            // do tree modification
            long startTime = System.currentTimeMillis();
            boolean timesUp = false;
            TreeNode curr = root;
            while (!timesUp) {
                try { Thread.sleep((long) randomDouble(1,5)); }
                catch(Exception e){ e.printStackTrace(); }

                if (randomDouble() < 0.5) { // check leftChild first
                    if (curr.leftChild != null) {
                        if (randomDouble() < 0.1) {
                        }
                    } else {
                        if (randomDouble() < 0.4) {
                        }
                    }
                }
            }

            for (int i = 0; i < ts.length; i++)
                ts[i].join();

            // print TreePrinters' Strings
            for (int i = 0; i < ts.length; i++)
                System.out.println(ts[i].str + "\n");

        } catch(Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
    }
}


/**
 * Class TreeNode
 */
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
