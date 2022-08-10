package phonebook

import java.io.File
import java.util.function.Supplier
import kotlin.system.exitProcess

class Application {

    fun run() {
        val find: File = File("F:\\find.txt")
        val directory: File = File("F:\\directory.txt")

        val search: Search = Search(find.readLines(), directory.readLines())
        val tasks: Array<Supplier<String>> = arrayOf(
            Supplier {
                println("Start searching (linear search)...")
                return@Supplier search.executeLinearSearch()
            },
            Supplier {
                println("Start searching (bubble sort + jump search)...")
                return@Supplier search.executeBubbleSortAndJumpSearch()
            },
            Supplier {
                println("Start searching (quick sort + binary search)..")
                return@Supplier search.executeQuickSortAndBinarySearch()
            },
            Supplier {
                println("Start searching (hash table)...")
                return@Supplier search.executeHashTableSearch()
            }
        )

        tasks.forEach { println(it.get()) }
        exitProcess(0)
    }
}