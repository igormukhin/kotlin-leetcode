class Solution198 {

    fun rob(nums: IntArray): Int {
        val bests = IntArray(nums.size) { -1 }

        for (i in nums.indices) {
            robRec(nums, bests, i)
        }

        return bests.asSequence().maxOrNull()!!
    }

    private fun robRec(nums: IntArray, bests: IntArray, startAt: Int): Int {
        if (bests[startAt] != -1) {
            return bests[startAt]
        }

        var best = 0
        for (i in startAt + 2 until nums.size) {
            val curr = robRec(nums, bests, i)
            if (curr > best) best = curr
        }

        val result = nums[startAt] + best
        bests[startAt] = result
        return result
    }
}

fun main() {
    println(Solution198().rob(intArrayOf(1,2,3,4,5,6,7,8,9,10)))
}