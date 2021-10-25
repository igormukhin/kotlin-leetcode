package olympia

/**
 * [Problem 1](http://neerc.ifmo.ru/school/archive/1995-1996/ru-olymp-roi-1996-problems.html)
 * of Russian national programming competition, 1995-1996.
 */
fun main() {
    val k = 10

    val lastNum = (0 until k - 1).fold(10L) { acc, _ -> acc * 10 }
    val chains = mutableListOf<Long>()
    var maxLength = 2
    var maxNum = lastNum / maxLength
    var stop = false
    
    val digits = IntArray(k)
    val used = BooleanArray(10)

    fun digitsToLong(): Long {
        var n = 0L
        for (i in digits.indices) {
            n = n * 10 + digits[i]
        }
        return n
    }

    fun isColorful(num: Long): Boolean {
        val digs = BooleanArray(10)
        var curr = num
        while (curr > 0) {
            val dig = (curr % 10).toInt()
            if (digs[dig]) return false
            digs[dig] = true
            curr /= 10
        }
        return true
    }

    fun chainLen(start: Long): Int {
        var len = 1
        var curr = start
        while (true) {
            curr *= 2
            if (curr >= lastNum || !isColorful(curr)) return len
            len++
        }
    }

    fun run(pos: Int, startNum: Int) {
        if (pos >= k) {
            val num = digitsToLong()
            if (num >= maxNum) {
                stop = true
                return
            }
            val len = chainLen(num)
            if (len == maxLength) {
                chains.add(num)
            } else if (len > maxLength) {
                chains.clear()
                chains.add(num)
                maxLength = len
                maxNum = lastNum / maxLength
            }
            return
        }

        for (i in startNum..9) {
            if (stop) break
            if (used[i]) continue
            used[i] = true
            digits[pos] = i
            run(pos + 1, 0)
            used[i] = false
        }
    }
    
    run(0, 1)

    chains.forEach { num ->
        var curr = num
        for (i in 0 until maxLength) {
            if (i > 0) print(" -> ")
            print(curr)
            curr *= 2
        }
        println()
    }

}
