package leetcode

import java.util.*

fun main() {
    check(
        Solution23().mergeKLists(
            arrayOf(asLinkedList(1, 4, 5), asLinkedList(1, 3, 4), asLinkedList(2, 6))
        )!!.asArray().contentEquals(arrayOf(1, 1, 2, 3, 4, 4, 5, 6))
    )
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null

    fun asArray() : Array<Int> {
        val list = mutableListOf<Int>()
        var curr: ListNode? = this
        while (curr != null) {
            list.add(curr.`val`)
            curr = curr.next
        }
        return list.toTypedArray()
    }
}

fun asLinkedList(vararg nums : Int) : ListNode? {
    if (nums.isEmpty()) return null
    var first : ListNode? = null
    var prev : ListNode? = null
    nums.asSequence().forEach { value ->
        if (first == null) {
            first = ListNode(value)
            prev = first
        } else {
            val curr = ListNode(value)
            prev!!.next = curr
            prev = curr
        }
    }
    return first
}
class Solution23 {
    fun mergeKLists(lists: Array<ListNode?>): ListNode? {
        val heads = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })

        fun poll(i: Int) {
            if (lists[i] == null) return
            val value = lists[i]!!.`val`
            lists[i] = lists[i]!!.next
            heads.add(Pair(value, i))
        }

        for (i in lists.indices) {
            poll(i)
        }

        var first : ListNode? = null
        var prev : ListNode? = null

        while (heads.isNotEmpty()) {
            val (value, listIndex) = heads.poll()

            if (first == null) {
                first = ListNode(value)
                prev = first
            } else {
                val curr = ListNode(value)
                prev!!.next = curr
                prev = curr
            }

            poll(listIndex)
        }

        return first
    }

}