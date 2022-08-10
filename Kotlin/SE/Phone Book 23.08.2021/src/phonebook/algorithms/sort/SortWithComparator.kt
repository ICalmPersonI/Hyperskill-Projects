package phonebook.algorithms.sort

class SortWithComparator(private val array: List<String>) {

    fun start(): List<String> {
        //val sortedArray: Array<String> = array.toTypedArray()
        return array.sortedWith(myCustomComparator)
    }

    private val myCustomComparator =  Comparator<String> { a, b ->
        var split = a.split(" ")
        val first: String = if (split.size < 3) split[1] else "${split[1]} ${split[2]}"
        split = b.split(" ")
        val second: String = if (split.size < 3) split[1] else "${split[1]} ${split[2]}"
        when {
            (first == second) -> 0
            (first < second) -> -1
            else -> 1
        }
    }
}