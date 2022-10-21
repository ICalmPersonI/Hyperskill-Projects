package phonebook.algorithms.sort


import phonebook.Utils

class QuickSort(array: List<String>) {

    private val sortedArray: Array<String> = array.toTypedArray()

    fun start(start: Int, end: Int): List<String> {
        sort(start, end)
        return sortedArray.toList()
    }

    private fun sort(start: Int, end: Int) {
        if (start < end) {
            val pi: Int = partition(start, end)
            sort(start, pi - 1)
            sort(pi + 1, end)
        }
    }

    private fun partition(start: Int, end: Int): Int {
        val pivot: String = Utils.skipTelNumber(sortedArray.toList(), end)
        var i: Int = (start - 1)

        for (j in start until end) {
            val elem = Utils.skipTelNumber(sortedArray.toList(), j)
            if (elem < pivot) {
                i++

                val temp: String = sortedArray[j]
                sortedArray[j] = sortedArray[i]
                sortedArray[i] = temp
            }
        }

        val temp: String = sortedArray[end]
        sortedArray[end] = sortedArray[i + 1]
        sortedArray[i + 1] = temp

        return i + 1

    }
}