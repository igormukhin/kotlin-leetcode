package leetcode

fun main() {
    val result = Solution25().reverseKGroup(
        asLinkedList(1, 2, 3), 2
    )!!.asArray()
    println("result = ${result.contentToString()}")

    check(result.contentEquals(arrayOf(2, 1)))
}

class Solution25 {
    fun reverseKGroup(head: ListNode?, k: Int): ListNode? {
        if (k == 1) return head
        var newHead: ListNode? = null
        var newTail: ListNode? = null

        var currHead = head
        while (currHead != null) {
            var i = 0
            var splitHead = currHead
            while (i < k && splitHead != null) {
                i++
                splitHead = splitHead.next
            }
            if (i < k) {
                newTail!!.next = currHead
                break
            } else {
                if (newHead == null) {
                    newHead = reverse(currHead, k)
                    newTail = currHead
                } else {
                    newTail!!.next = reverse(currHead, k)
                    newTail = currHead
                }
                currHead = splitHead
            }
        }

        return newHead
    }

    private fun reverse(head: ListNode, k: Int): ListNode {
        var newHead = head
        var restHead = head.next
        newHead.next = null

        var i = 1
        while (i < k && restHead != null) {
            val newHeadWas = newHead
            newHead = restHead
            restHead = newHead.next
            newHead.next = newHeadWas
            i++
        }

        return newHead
    }
}