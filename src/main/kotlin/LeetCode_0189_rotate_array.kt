import kotlin.random.Random

class Solution189 {
    fun rotate(nums: IntArray, k: Int) {
        if (nums.size < 2) return
        val kk = k % nums.size
        if (kk < 1) return

        var pos = 0
        var mem = nums[pos]
        for (i in nums.indices) {
            val nextPos = (pos + kk) % nums.size
            val temp = mem
            mem = nums[nextPos]
            nums[nextPos] = temp
            pos = nextPos

            //println("i = $i, pos = $pos, mem = $mem, nums = ${nums.contentToString()}")
            if (((i.toLong() + 1) * kk) % nums.size == 0L) {
                //if (((i + 1) * kk) % nums.size == 0) {
                pos++
                mem = nums[pos]
                //println("hey pos = $pos, mem = $mem")
            }
        }
    }

}

private fun memoryRotate(nums: IntArray, k: Int) {
    val orig = IntArray(nums.size) { nums[it] }
    orig.forEachIndexed { i, n -> nums[(i + k) % nums.size] = n }
}

fun main() {
    randomTests()
    runTest(6, 4)

/*
    val input = Utils.readInput("LeetCode189").lines()
    val nums = input[0].split(',').map { it.toInt() }.toIntArray()
    val k = input[1].toInt()
    runTest(nums.size, k);
*/
}

private fun randomTests() {
    val tests = 100000
    val maxSize = 1000
    repeat(tests) { testNum ->
        val size = Random.nextInt(3, maxSize)
        val k = Random.nextInt(1, size - 1)

        println("--- Test $testNum")
        runTest(size, k)
    }
}

private fun runTest(size: Int, k: Int) {
    val nums = IntArray(size) { it + 1 }
    val orig = nums.copyOf()

    println("nums.size = ${nums.size}")
    println("k = $k")

    memoryRotate(nums, k)
    //println("rotated: " + nums.contentToString())

    Solution189().rotate(orig, k)
    //println("tested: " + orig.contentToString())

    if (!orig.contentEquals(nums)) {
        throw RuntimeException("Test failed")
    }
}