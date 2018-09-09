import java.lang.System.*;

/* - There is a binary search tree whose data are doubles.
 * - There are two Traversal threads and one Modifier thread.
 * - The Traversal threads do an inorder traversal, recording their path in local strings. When they
 *   finish a traversal, they record a newline and wait 5-20ms and then restart. They repeat this
 *   for 5 seconds.
 * - The Modifier thread randomly traverses a single branch of the tree starting from the root.
 *   Where applicable, it modifies nodes in the tree with predetermined probabilities. If it
 *   modifies a node or reaches a leaf, it restarts after waiting 1-5ms. It repeats for 5 seconds.
 * - After Traversal and Modifier threads are done, the strings recorded by the traversal threads
 *   are printed to stdout.
 */
public class q2 {

    static final int numStartingNodes = 15; // number of nodes in initial tree

    static volatile TreeNode root;

    public static void main(String[] args) throws Exception {
        /** Initialize the tree */
        root = new TreeNode(Utils.randomDouble(), null, null, null);
        for (int i = 0; i < numStartingNodes; i++)
            Utils.treeInsert(root, Utils.randomDouble());

        /** Traversal Threads. Repeatedly do inorder traversal, storing string repr. of traversal */
        TreePrinter[] tps = new TreePrinter[] {new TreePrinter(root), new TreePrinter(root)};
        for (TreePrinter tp : tps)
            tp.start();

        /** Modifier Thread. Repeatedly traverse tree, add and remove nodes randomly */
        TreeModifier tm = new TreeModifier(root);
        tm.start();

        /** wait for TreePrinters and TreeModifier to finish */
        for (TreePrinter tp : tps)
            tp.join();
        tm.join();

        /** print TreePrinters' Strings */
        System.out.println("A");
        System.out.println(tps[0].str + "\n");
        System.out.println("B");
        System.out.println(tps[1].str);

        /** print the tree is at is now to see if it's valid */
        //printTree(root);

        /** check that Traversal Threads always traversed inorder, and binary tree stayed valid. */
        //boolean firstTreePrinterIsGood  = Utils.isValidTraversalString(tps[0].str.toString());
        //boolean secondTreePrinterIsGood = Utils.isValidTraversalString(tps[1].str.toString());
        //System.out.println(firstTreePrinterIsGood && secondTreePrinterIsGood);
    }
}
