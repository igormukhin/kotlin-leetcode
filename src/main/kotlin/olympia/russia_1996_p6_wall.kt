package olympia

import olympia.Side.LEFT
import olympia.Side.RIGHT

/**
 * [Problem 6](http://neerc.ifmo.ru/school/archive/1995-1996/ru-olymp-roi-1996-problems.html)
 * of Russian national programming competition, 1995-1996.
 */
fun main() {
    val width = 30
    val height = 25

    val wall = Wall(width, height)
    wall.fillRow(0)

    wall.supportOuter(0, 0, width - 1)
    wall.supportMid(0, 0, width - 1)

    wall.print()
    println("Bricks: ${wall.count()}")
}

private class Wall(val width: Int, val height: Int) {
    val bricks = Array(height) { BooleanArray(width) }

    fun print() {
        bricks.forEachIndexed { rowIndex, row ->
            if (rowIndex % 2 == 0) print("  ")
            row.forEach {
                print(if (it) "[__]" else "    ")
            }
            println()
        }
    }

    fun under(row: Int, brick: Int, side: Side)
            = brick + (if (side == RIGHT) 1 else 0) + (if (row % 2 == 0) 0 else -1)

    fun hasBrickUnder(row: Int, brick: Int, side: Side): Boolean {
        if (row == height - 1) return true
        val brickUnder = under(row, brick, side)
        if (brickUnder < 0 || brickUnder >= width) return false
        return bricks[row + 1][brickUnder]
    }

    fun count(): Int {
        return bricks.sumOf { r -> r.sumOf { b -> (if (b) 1 else 0).toInt() } }
    }
}

private fun Wall.fillRow(rowIndex: Int) {
    for (i in 0 until width) {
        bricks[rowIndex][i] = true
    }
}

private enum class Side {
    RIGHT, LEFT
}

private fun Wall.supportOuter(rowIndex: Int, left: Int, right: Int) {
    supportBrick(rowIndex, left, RIGHT)
    supportBrick(rowIndex, right, LEFT)
}

private fun Wall.supportBrick(row: Int, brick: Int, side: Side) {
    var currRow = row
    var currBrick = brick
    while (currRow < height - 1) {
        currBrick = under(currRow, currBrick, side)
        if (currBrick < 0) currBrick = 0
        if (currBrick >= width) currBrick = width - 1
        currRow++
        if (bricks[currRow][currBrick]) break
        bricks[currRow][currBrick] = true
    }
}

private fun Wall.supported(row: Int, brick: Int) : Boolean {
    return hasBrickUnder(row, brick, LEFT) || hasBrickUnder(row, brick, RIGHT)
}

private fun Wall.supportMid(row: Int, left: Int, right: Int) {
    if (right - left < 2) return

    val w = right - left + 1
    var mid = left + (w / 2)
    if (mid % 2 == 0) mid--
    if (!supported(row, mid)) {
        supportBrick(row, mid, LEFT)
        supportMid(row, left, mid)
    }

    val mid2 = mid + 1
    if (!supported(row, mid2)) {
        supportBrick(row, mid2, RIGHT)
        supportMid(row, mid2, right)
    }
}

