package leetcode

import kotlin.math.max

fun main() {
    println(Solution3().lengthOfLongestSubstring("abcabcbb"))
}

class Solution3 {
    fun lengthOfLongestSubstring(s: String): Int {
        val char2pos = mutableMapOf<Char, Int>()
        var result = 0
        var i = 0
        var j = 0
        while (j < s.length) {
            val ch = s[j]
            if (ch in char2pos) {
                i = max(i, char2pos[ch]!! + 1)
            }
            char2pos[ch] = j
            j++
            result = max(result, j - i)
        }

        return result
    }
}