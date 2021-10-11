class Solution4 {
    fun findMedianSortedArrays(nums1: IntArray, nums2: IntArray): Double {

        fun findAtPos(start1: Int, size1: Int, start2: Int, size2: Int, startT: Int, sizeT: Int, targetPos: Int): Int {
            println("nums1: ${nums1.asList().subList(start1, start1 + size1)}")
            println("nums2: ${nums2.asList().subList(start2, start2 + size2)}")
            println("target: startT=$startT, sizeT=$sizeT, targetPos=$targetPos")

            if (size1 == 0) {
                return nums2[start2 + targetPos]
            } else if (size2 == 0) {
                return nums1[start1 + targetPos]
            }

            val mid2 = size2 / 2
            val val2 = nums2[start2 + mid2]
            var found1 = nums1.binarySearch(val2, start1, start1 + size1)
            found1 = if (found1 >= 0) {
                found1
            } else {
                -found1 - 1
            } - start1

            val posT = mid2 + found1
            return if (posT == targetPos) {
                val2
            } else if (posT < targetPos) {
                findAtPos(start1 + found1, size1 - found1, start2 + mid2 + 1, size2 - mid2 - 1,
                    startT + found1 + mid2 + 1, sizeT - found1 - mid2 - 1, targetPos - found1 - mid2 - 1)
            } else {
                findAtPos(start1, found1, start2, mid2, startT, sizeT - (size1 - found1) - (size2 - mid2), targetPos)
            }
        }

        fun findAtPos(pos: Int): Int {
            println("Search at pos: $pos")
            val res = findAtPos(0, nums1.size, 0, nums2.size, 0, nums1.size + nums2.size, pos)
            println("Result: $res")
            return res
        }

        val fullSize = nums1.size + nums2.size
        val mid = fullSize / 2
        return if (mid * 2 != fullSize) {
            findAtPos(mid).toDouble()
        } else {
            0.5 * (findAtPos(mid - 1) + findAtPos(mid))
        }
    }


}

fun main() {
    val nums1 = intArrayOf(3, 5, 6)
    val nums2 = intArrayOf(1, 2, 4)
    println("r = ${Solution4().findMedianSortedArrays(nums1, nums2)}")
}
