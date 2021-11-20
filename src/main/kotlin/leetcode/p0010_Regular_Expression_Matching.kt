package leetcode

fun main() {
    check(Solution10().isMatch("a", "ab*"))
    check(!Solution10().isMatch("aa", "a"))
    check(Solution10().isMatch("aa", "a*"))
    check(Solution10().isMatch("ab", ".*"))
    check(Solution10().isMatch("aab", "c*a*b"))
    check(!Solution10().isMatch("mississippi", "mis*is*p*."))
    check(Solution10().isMatch("mississippi", "mis*is*ip*."))
    check(Solution10().isMatch("aacdeee", "a*a*acde*"))
}

class Solution10 {

    fun isMatch(s: String, p: String): Boolean {
        return isMatch(s, p, 0, 0)
    }

    private fun isMatch(str: String, pattern: String, strStartAt: Int, patternStartAt: Int): Boolean {
        var i = strStartAt
        var k = patternStartAt
        while (true) {
            if (i == str.length && k == pattern.length) {
                return true
            } else if (k < pattern.length - 1 && pattern[k + 1] == '*') {
                val letter = pattern[k]
                k += 2
                while (i < str.length) {
                    if (isMatch(str, pattern, i, k)) {
                        return true
                    }
                    if (letter != '.' && str[i] != letter) return false
                    i++
                }
            } else if (k < pattern.length && (pattern[k] == '.'
                        || (i < str.length && pattern[k] == str[i]))) {
                k++
                i++
            } else {
                return false
            }
        }
    }
}