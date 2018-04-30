class TreeNode {
    protected volatile double data;
    protected volatile TreeNode parent;
    protected volatile TreeNode leftChild;
    protected volatile TreeNode rightChild;

    TreeNode(double data, TreeNode parent, TreeNode leftChild, TreeNode rightChild) {
        this.data = data;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}
