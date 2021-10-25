import kotlin.math.max

class Solution104 {
    fun maxDepth(root: TreeNode?): Int {
        if (root == null) return 0
        return 1 + max(maxDepth(root.left), maxDepth(root.right))
    }

    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

}