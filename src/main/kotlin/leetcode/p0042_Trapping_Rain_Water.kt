package leetcode
class Solution42 {
    fun trap(heights: IntArray): Int {
        var water = 0
        val slopes = mutableListOf<Int>() // positions of slopes
        for (i in heights.indices) {
            val prevHeight = if (i > 0) heights[i - 1] else 0
            if (heights[i] > prevHeight && slopes.isNotEmpty()) { // going up
                val newHeight = heights[i]
                var bottom = prevHeight
                do {
                    val leftIndex = slopes.last()
                    val leftHeight = heights[leftIndex]
                    if (newHeight >= leftHeight) {
                        water += (leftHeight - bottom) * (i - leftIndex - 1)
                        slopes.removeAt(slopes.size - 1)
                        bottom = leftHeight
                    } else {
                        water += (newHeight - bottom) * (i - leftIndex - 1)
                        bottom = newHeight
                    }
                } while (bottom < newHeight && slopes.isNotEmpty())
                slopes.add(i)

            } else if (prevHeight > heights[i]) { // going down
                if (slopes.isEmpty()) {
                    slopes.add(i - 1)
                } else if (heights[slopes.last()] == heights[i - 1]) {
                    slopes[slopes.size - 1] = i - 1
                } else {
                    slopes.add(i - 1)
                }
            }
        }
        return water
    }
}

fun main() {
    println(Solution42().trap(intArrayOf(9, 6, 8, 8, 5, 6, 3)))
}