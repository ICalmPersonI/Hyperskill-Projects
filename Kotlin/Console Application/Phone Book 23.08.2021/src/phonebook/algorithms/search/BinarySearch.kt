package phonebook.algorithms.search

import phonebook.Utils


class BinarySearch(private val find: List<String>, private val directory: List<String>) {

    fun start(): Int {
        var count: Int = 0
        for (record in find) if (search(directory, record)) count++
        return count
    }

    private fun search(list: List<String>, record: String): Boolean {
        var left: Int = 0
        var right: Int = list.lastIndex

        while(left <= right) {
            val mid: Int = left + (right - left) / 2
            val elem: String = Utils.skipTelNumber(list, mid)

            if (record == elem) {
                return true
            } else if (record < elem) {
                right = mid - 1
            } else {
                left = mid + 1
            }
        }

        return false
    }
}