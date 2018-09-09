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
            boolean rightChildIsNull = false;
            boolean leftChildIsNull = false;

            if (curr.leftChild != null) {
                if (Utils.randomDouble() < 0.1) {
                    curr.leftChild = null;
                    madeModification = true;
                }
            } else {
                leftChildIsNull = true;
                if (Utils.randomDouble() < 0.4) {
                    TreeNode aLeftChild = new TreeNode(Utils.randomDouble(minVal, curr.data), curr, null, null);
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
                rightChildIsNull = true;
                if (Utils.randomDouble() < 0.4) {
                    TreeNode aRightChild = new TreeNode(Utils.randomDouble(curr.data, maxVal), curr, null, null);
                    curr.rightChild = aRightChild;
                    madeModification = true;
                }
            }

            if (madeModification || (rightChildIsNull && leftChildIsNull)) {
                minVal = 0.; maxVal = 1.;
                curr = root;
                try { Thread.sleep((long) Utils.randomDouble(5,20)); }
                catch(Exception e){ e.printStackTrace(); }
            } else {
                TreeNode tmp = curr;
                if (!leftChildIsNull && !rightChildIsNull) {
                    if (Utils.randomDouble() < 0.5) {
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

    }
}
