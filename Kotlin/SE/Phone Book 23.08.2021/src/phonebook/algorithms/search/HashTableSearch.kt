package phonebook.algorithms.search

import kotlin.collections.HashSet

class HashTableSearch {

    private var hash: HashSet<String> = HashSet()

    fun start(find: List<String>): Int {
        var count: Int = 0
        for (elem in find) {
            if (hash.contains(elem)) count++
        }
        return count
    }

    fun create(dictionary: List<String>) {
        dictionary.forEach {
            val split = it.split(" ")
            hash.add(if (split.size < 3) split[1] else "${split[1]} ${split[2]}")
        }
    }
}