package phonebook

import phonebook.algorithms.search.BinarySearch
import phonebook.algorithms.search.HashTableSearch
import phonebook.algorithms.search.JumpSearch
import phonebook.algorithms.search.LinearSearch
import phonebook.algorithms.sort.BubbleSort
import phonebook.algorithms.sort.QuickSort
import phonebook.algorithms.sort.SortWithComparator
import java.util.function.Supplier

class Search(private val find: List<String>, private val directory: List<String>) {

    fun executeLinearSearch(): String {
        val start: Long = System.currentTimeMillis()
        val count: Int = LinearSearch(find, directory).start()
        val end: Long = System.currentTimeMillis()

        return "Found $count / 500 entries. ${timeCalculation(end - start, "Time taken")}\n"
    }

    private var sortedDirectory: List<String> = emptyList()

    private val bubbleSort: Supplier<List<String>> = Supplier { return@Supplier BubbleSort(directory).start() }
    private val quickSort: Supplier<List<String>> = Supplier { return@Supplier QuickSort(directory).start(0, directory.lastIndex) }
    private val jumpSearch: Supplier<Int> = Supplier { return@Supplier JumpSearch(find, sortedDirectory).start() }
    private val binarySearch: Supplier<Int> = Supplier { return@Supplier BinarySearch(find, sortedDirectory).start() }

    fun executeBubbleSortAndJumpSearch(): String {
        sortedDirectory = emptyList()
        return combinedSearch(bubbleSort, jumpSearch)
    }

    fun executeQuickSortAndBinarySearch(): String {
        sortedDirectory = emptyList()
        return combinedSearch(quickSort, binarySearch)
    }

    private fun combinedSearch(sortType: Supplier<List<String>>, searchType: Supplier<Int>): String {
        val startTotal: Long = System.currentTimeMillis()

        val startSortTime: Long = System.currentTimeMillis()
        val sort: Thread = Thread {
            sortedDirectory = sortType.get()
        }
        sort.start()
        while (sortedDirectory.isEmpty()) {
            if ((System.currentTimeMillis() - startSortTime) / 1000 / 60 >= 2L) {
                sort.interrupt()
                sortedDirectory = SortWithComparator(directory).start()
                break
            }
            Thread.sleep(1000)
        }
        val endSortTime: Long = System.currentTimeMillis() - startSortTime

        val startSearchTime: Long = System.currentTimeMillis()
        val count: Int = searchType.get()
        val endSearchTime: Long = System.currentTimeMillis() - startSearchTime

        val endTotalTime: Long = System.currentTimeMillis() - startTotal
        return "Found $count / 500 entries. ${timeCalculation(endTotalTime, "Time taken")}\n" +
                "${timeCalculation(endSortTime, "Sorting time")}\n" +
                "${timeCalculation(endSearchTime, "Searching time")}\n"
    }


    fun executeHashTableSearch(): String {
        val startTotal: Long = System.currentTimeMillis()

        val startCreatingTime: Long = System.currentTimeMillis()
        val search: HashTableSearch = HashTableSearch()
        search.create(directory)
        val endCreatingTime: Long = System.currentTimeMillis() - startCreatingTime

        val startSearchTime: Long = System.currentTimeMillis()
        val count: Int = search.start(find)
        val endSearchTime: Long = System.currentTimeMillis() - startSearchTime

        val endTotalTime: Long = System.currentTimeMillis() - startTotal
        return "Found $count / 500 entries. ${timeCalculation(endTotalTime, "Time taken")}\n" +
                "${timeCalculation(endCreatingTime, "Creating time")}\n" +
                "${timeCalculation(endSearchTime, "Searching time")}\n"
    }

    private fun timeCalculation(time: Long, type: String): String {
        val min = time / 1000 / 60
        val sec = time / 1000 % 60
        val ms = time - min * 1000 * 60 - sec * 1000
        return "$type: $min min. $sec sec. $ms ms."
    }
}