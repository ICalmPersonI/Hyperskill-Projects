package phonebook.algorithms.search

class LinearSearch(private val find: List<String>, private val directory: List<String>) {

    fun start(): Int {
        var count: Int = 0
        for (first in find) {
            for (second in directory) {
                if (second.contains(first)) {
                    count++
                    break
                }
            }
        }
        return count
    }
}