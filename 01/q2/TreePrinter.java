/**
 * Traversal Thread.
 *
 * Repeatedly do inorder tree traversal, recording traversal as one line of text each time.
 * Wait for 5 to 20 ms between each traversal. Stop after 5 seconds.
 */
class TreePrinter extends Thread {
    StringBuilder str = new StringBuilder();

    TreeNode root;

    public TreePrinter(TreeNode root) {
        this.root = root;
    }

    @Override
    public void run() {
        TreeNode curr = root;
        TreeNode prev = root.parent;

        TreeNode parent = null;
        TreeNode leftChild = null;
        TreeNode rightChild = null;

        long startTime = System.currentTimeMillis();

        // For 5 seconds, do an iterative, inorder tree traversal. Restart from root at leaves
        while (System.currentTimeMillis() - startTime < Utils.fiveSecondsInMillis) {

            // Since no locking, need to get all related tree variables at once
            // and at beginning of iteration. Also, since TreeModifier runs on
            // same tree concurrently, it is possible that parent, leftChild, and
            // rightChild might already be null.
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
            } else if (prev == leftChild || prev.data < curr.data) {
                prev = curr;
                str.append(curr.data + " ");
                if (rightChild != null) {
                    curr = rightChild;
                } else {
                    curr = parent;
                }
            } else if (prev == rightChild || prev.data > curr.data) {
                prev = curr;
                curr = parent;
            }

            // restart traversal if at leaf
            if (curr == null) {
                curr = root;
                prev = curr.parent;
                str.append("\n");
            }

            try { Thread.sleep((long) Utils.randomDouble(5, 20)); }
            catch(Exception e) { e.printStackTrace(); }
        }
    }
}

