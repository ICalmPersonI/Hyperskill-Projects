package phonebook

class Utils {

    companion object {
        fun skipTelNumber(list: List<String>, index: Int): String {
            val split = list[index].split(" ")
            return if (split.size < 3) split[1] else "${split[1]} ${split[2]}"
        }
    }
}