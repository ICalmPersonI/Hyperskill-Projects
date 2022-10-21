package phonebook.algorithms.search

import phonebook.Utils
import kotlin.math.floor
import kotlin.math.sqrt

class JumpSearch(private val find: List<String>, private val directory: List<String>) {

    fun start(): Int {
        var count: Int = 0
        for (record in find) if (search(record)) count++
        return count
    }

    private fun search( record: String): Boolean {
        val jump: Int = floor(sqrt(directory.size.toDouble())).toInt()

        for (i in 0..directory.lastIndex step jump) {
            var elem = Utils.skipTelNumber(directory, i)

            if (elem == record) {
                return true
            }

            if (elem > record) {
                val start: Int = i - jump

                for (j in start..i) {
                    if (j < 0) {
                        return false
                    }

                    elem = Utils.skipTelNumber(directory, j)

                    if (elem == record) {
                        return true
                    }
                }
                return false
            }
        }
        return false
    }
}