package parking

class Parking(size: Int) {
    private val spots: Array<Spot?> = arrayOfNulls<Spot>(size)

    fun park(spot: Spot): Int {
        for (i in spots.indices) {
            if (spots[i] == null) {
                spots[i] = spot
                return i + 1
            }
        }
        return -1
    }

    fun leave(number: Int): Boolean {
        return if (spots.size > number || spots[number - 1] != null) {
            spots[number - 1] = null
            true
        } else false
    }

    fun getStats(): List<String> = spots.mapIndexed { index, spot -> if (spot != null) "${index + 1} $spot" else "" }
        .filter { it.isNotEmpty() }.toList()

    fun getRegByColor(color: String): List<String> = spots.filterNotNull().filter { it.carColor == color }
        .map { "${it.registrationNumber}," }.toList()

    fun getSpotsBy(color: String, regNumber: String): List<String> = spots
        .mapIndexed { index, spot ->
            if (spot != null && (if (color == "") spot.registrationNumber == regNumber else spot.carColor == color))
                "${index + 1}," else ""
        }
        .filter { it.isNotEmpty() }.toList()
}