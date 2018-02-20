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

    static volatile TreeNode root;


    /**
     * convenience methods
     */

    // java makin me work hard for my random doubles
    static double randomDouble() // implicitly in range [0,1[
    { return ThreadLocalRandom.current().nextDouble(); }
    static double randomDouble(double upperBoundExclusive)
    { return ThreadLocalRandom.current().nextDouble(upperBoundExclusive); }
    static double randomDouble(double lowerBoundInclusive, double upperBoundExclusive)
    { return ThreadLocalRandom.current().nextDouble(lowerBoundInclusive, upperBoundExclusive); }

    // insert into Binary Search Tree to initizialize it with random doubles
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
            return; // base case

        String s = "";
        for (int i = 0; i < n; i++)
            s += "    "; // indent based on depth in tree
        if (!left) s += "_"; // right child starts with '_' underscore character
        System.out.println(s + tn.data);

        treePrint(tn.leftChild, n+1, true); // always try left children before
        treePrint(tn.rightChild, n+1, false); // right children
    }


    /**
     * main method
     */
    public static void main(String[] args) {
        try {
            // initialize tree
            root = new TreeNode(randomDouble(), null, null, null);
            for (int i = 0; i < numStartingNodes; i++)
                treeInsert(root, randomDouble());

            //treePrint(root, 0, true);
            //System.out.println("----------------------------------------");

            // thread that does inorder tree traversal recording path as string
            class TreePrinter extends Thread {
                StringBuilder str = new StringBuilder();
                int id;
                public TreePrinter(int id) {this.id = id;}

                @Override
                public void run() { // repeatedly traverse the tree for 5 seconds
                    TreeNode curr = root;
                    TreeNode prev = curr.parent;

                    TreeNode parent = null;
                    TreeNode leftChild = null;
                    TreeNode rightChild = null;

                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < fiveSecondsInMillis) {
                        // restart after finishing traversal
                        if (curr == null) {
                            curr = root;
                            prev = curr.parent;
                            str.append("\n");
                        }

                        // get all references just once
                        parent = curr.parent;
                        leftChild = curr.leftChild;
                        rightChild = curr.rightChild;

                        if (prev == parent) {
                            prev = curr;
                            if (leftChild != null) {
                                curr = leftChild;
                            } else {
                                str.append(curr.data +  " ");
                                if (rightChild != null) {
                                    curr = rightChild;
                                } else {
                                    curr = parent;
                                }
                            }
                        } else if (prev == leftChild) {
                            prev = curr;
                            str.append(curr.data + " ");
                            if (rightChild != null) {
                                curr = rightChild;
                            } else {
                                curr = parent;
                            }
                        } else if (prev == rightChild) {
                            prev = curr;
                            curr = parent;
                        } else { // if prev is one of children but they've both been set to null
                            prev = curr;
                            if (leftChild != null) { // prev should be rightChild but rightChild is null
                                curr = parent;
                            } else if (rightChild != null) { // prev should be leftChild but leftChild is null
                                str.append(curr.data + " ");
                                curr = rightChild;
                            } else { // both children are null so can't tell which child prev is
                                String[] currTraversal = str.substring(str.lastIndexOf("\n")+1).split(" ");
                                boolean alreadyVisited = false;
                                for (String s : currTraversal)
                                    if (s.equals(String.valueOf(curr.data)))
                                        alreadyVisited = true;
                                if (!alreadyVisited)
                                    str.append(curr.data + " ");
                                curr = parent;
                            }
                        }

                        try { Thread.sleep((long) randomDouble(1, 5)); }
                        catch(Exception e) { e.printStackTrace(); }
                    }
                }
            }

            TreePrinter[] ts = new TreePrinter[] { new TreePrinter(1), new TreePrinter(2) };
            for (TreePrinter t : ts)
                t.start();

            // do tree modification
            long startTime = System.currentTimeMillis();
            double minVal = 0.; double maxVal = 1.;
            TreeNode curr = root;
            while (System.currentTimeMillis() - startTime < fiveSecondsInMillis) {
                //treePrint(root, 0, true);

                boolean madeModification = false;
                boolean rightChildIsNull = false;
                boolean leftChildIsNull = false;

                if (curr.leftChild != null) {
                    if (randomDouble() < 0.1) {
                        //System.out.println("delete left child");
                        curr.leftChild = null;
                        madeModification = true;
                    }
                } else {
                    leftChildIsNull = true;
                    if (randomDouble() < 0.4) {
                        //System.out.println("create left child");
                        TreeNode aLeftChild = new TreeNode(randomDouble(minVal, curr.data), curr, null, null);
                        curr.leftChild = aLeftChild;
                        madeModification = true;
                    }
                }

                if (curr.rightChild != null) {
                    if (randomDouble() < 0.1) {
                        //System.out.println("delete right child");
                        curr.rightChild = null;
                        madeModification = true;
                    }
                } else {
                    rightChildIsNull = true;
                    if (randomDouble() < 0.4) {
                        //System.out.println("create right child");
                        TreeNode aRightChild = new TreeNode(randomDouble(curr.data, maxVal), curr, null, null);
                        curr.rightChild = aRightChild;
                        madeModification = true;
                    }
                }

                if (madeModification || (rightChildIsNull && leftChildIsNull)) {
                    minVal = 0.; maxVal = 1.;
                    curr = root;
                    try { Thread.sleep((long) randomDouble(5,20)); }
                    catch(Exception e){ e.printStackTrace(); }
                } else {
                    TreeNode tmp = curr;
                    if (!leftChildIsNull && !rightChildIsNull) {
                        if (randomDouble() < 0.5) {
                            maxVal = curr.data;
                            curr = curr.leftChild;
                        } else {
                            minVal = curr.data;
                            curr = curr.rightChild;
                        }
                    } else if (!leftChildIsNull) {
                        maxVal = curr.data;
                        curr = curr.leftChild;
                    } else if (!rightChildIsNull){
                        minVal = curr.data;
                        curr = curr.rightChild;
                    }
                }
            }

            // wait for printers to finish
            for (TreePrinter t : ts)
                t.join();

            // print TreePrinters' Strings
            for (TreePrinter t : ts)
                System.out.println(t.str + "\n");

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
    volatile TreeNode parent;
    volatile TreeNode leftChild;
    volatile TreeNode rightChild;
    TreeNode(double data, TreeNode parent, TreeNode leftChild, TreeNode rightChild) {
        this.data = data;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}
