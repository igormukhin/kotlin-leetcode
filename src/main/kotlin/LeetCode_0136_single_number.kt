class Solution136 {
    fun singleNumber(nums: IntArray): Int {
        val seen = mutableSetOf<Int>()
        for (n in nums) {
            if (seen.contains(n)) {
                seen.remove(n)
            } else {
                seen.add(n)
            }
        }
        return seen.first()
    }
}