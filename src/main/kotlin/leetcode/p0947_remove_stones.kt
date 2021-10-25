package leetcode

import java.util.*

class Solution947 {

    fun removeStones(stones: Array<IntArray>): Int {
        val xs = mutableMapOf<Int, MutableList<Int>>()
        val ys = mutableMapOf<Int, MutableList<Int>>()
        stones.forEachIndexed { i, p ->
            xs.computeIfAbsent(p[0]) { mutableListOf() }.add(i)
            ys.computeIfAbsent(p[1]) { mutableListOf() }.add(i)
        }

        val visited = BooleanArray(stones.size)

        fun breadthFirstSearch(startIndex: Int) {
            val queue = LinkedList<Int>()
            queue.add(startIndex)
            while (queue.isNotEmpty()) {
                val current = queue.pop()
                visited[current] = true
                xs[stones[current][0]]!!.forEach { i -> if (!visited[i] && !queue.contains(i)) queue.add(i) }
                ys[stones[current][1]]!!.forEach { i -> if (!visited[i] && !queue.contains(i)) queue.add(i) }
            }
        }

        var islands = 0
        for (i in stones.indices) {
            if (visited[i]) continue
            islands++
            breadthFirstSearch(i)
        }

        return stones.size - islands
    }

}

fun main() {
    val points = arrayOf(
        intArrayOf(0, 0),
        intArrayOf(0, 1),
        intArrayOf(1, 0),
        intArrayOf(1, 2),
        intArrayOf(2, 1),
        intArrayOf(2, 2),
        intArrayOf(10, 5),
        intArrayOf(5, 5)
    )
    println(Solution947().removeStones(points))
}
