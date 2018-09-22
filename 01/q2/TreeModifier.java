/**
 * Modifier Thread.
 *
 * Repeatedly traverse a random path from root to leaf. Before restarting, wait 1-5 ms.
 * While traversing the random path, add a left child with 40% probabilty when one is
 * missing, and do the same for a right child. Similarly remove children 10% of the time
 * when they do exist. If any modification is made, restart from root (after wait).
 */
public class TreeModifier extends Thread {
    TreeNode root;

    public TreeModifier(TreeNode root) {
        this.root = root;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        double minVal = 0.;
        double maxVal = 1.;
        TreeNode curr = root;

        while (System.currentTimeMillis() - startTime < Utils.fiveSecondsInMillis) {
            boolean madeModification = false;

            if (curr.leftChild != null) {
                if (Utils.randomDouble() < 0.1) {
                    curr.leftChild = null;
                    madeModification = true;
                }
            } else {
                if (Utils.randomDouble() < 0.4) {
                    TreeNode aLeftChild = new TreeNode(Utils.randomDouble(minVal, curr.data), curr);
                    curr.leftChild = aLeftChild;
                    madeModification = true;
                }
            }

            if (curr.rightChild != null) {
                if (Utils.randomDouble() < 0.1) {
                    curr.rightChild = null;
                    madeModification = true;
                }
            } else {
                if (Utils.randomDouble() < 0.4) {
                    TreeNode aRightChild = new TreeNode(Utils.randomDouble(curr.data, maxVal), curr);
                    curr.rightChild = aRightChild;
                    madeModification = true;
                }
            }

            if (madeModification || (curr.leftChild == null && curr.rightChild == null)) {
                minVal = 0.; maxVal = 1.;
                curr = root;
                try { Thread.sleep((long) Utils.randomDouble(1, 6)); }
                catch(Exception e){ e.printStackTrace(); }
            } else {
                TreeNode tmp = curr;
                if (curr.leftChild != null && curr.rightChild != null) {
                    if (Utils.randomDouble() < 0.5) {
                        maxVal = curr.data;
                        curr = curr.leftChild;
                    } else {
                        minVal = curr.data;
                        curr = curr.rightChild;
                    }
                } else if (curr.leftChild != null) {
                    maxVal = curr.data;
                    curr = curr.leftChild;
                } else if (curr.rightChild != null){
                    minVal = curr.data;
                    curr = curr.rightChild;
                }
            }
        }

    }
}
