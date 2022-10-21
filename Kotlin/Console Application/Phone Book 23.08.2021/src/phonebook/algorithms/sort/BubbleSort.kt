package phonebook.algorithms.sort

import phonebook.Utils


class BubbleSort(private val list: List<String>) {

    fun start(): List<String> {
        val cache: Array<String> = list.toTypedArray()
        var correctOrderCount: Int = 0
        while (correctOrderCount != list.size - 1) {
            correctOrderCount = 0;
            for (i in 0 until cache.lastIndex) {
                val firstElem: String = Utils.skipTelNumber(cache.toList(), i)
                val secondElem: String = Utils.skipTelNumber(cache.toList(), i + 1)

                if (firstElem > secondElem) {
                    val temp: String = cache[i]
                    cache[i] = cache[i + 1]
                    cache[i + 1] = temp
                } else {
                    correctOrderCount++
                }
            }
        }

        return cache.toList()
    }

}