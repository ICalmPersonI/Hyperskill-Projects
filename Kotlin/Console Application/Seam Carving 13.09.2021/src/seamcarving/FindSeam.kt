package seamcarving


class FindSeam(var image: Array<Array<Double>>) {

    private val energy: Array<Array<Double>> = image.copyOf()
    private val sum: Array<Array<Double>> = Array(image.size) { Array(image[0].size) { 0.0 } }
    private val seam: MutableList<NodeCoordinates> = mutableListOf()

    init { findSum() }

    fun find() {
        val min: Double = sum[0].minOrNull()!!
        var prevIndex: Int = sum[0].indexOfFirst { it == min }

        seam.add(NodeCoordinates(0, prevIndex))
        for (i in sum.indices) {
            if (i + 1 > sum.lastIndex) break
            var currentIndex: Int = prevIndex
            val applicants: List<Double> = listOf(
                if (prevIndex + 1 > energy[0].lastIndex) Double.MAX_VALUE else sum[i + 1][prevIndex + 1],
                sum[i + 1][prevIndex],
                if (prevIndex - 1 < 0) Double.MAX_VALUE else sum[i + 1][prevIndex - 1]
            )

            val minValue: Double = applicants.minOrNull()!!
            when (applicants.indexOfFirst { it == minValue }) {
                0 -> currentIndex += 1
                2 -> currentIndex -= 1
            }

            seam.add(NodeCoordinates(i + 1, currentIndex))
            prevIndex = currentIndex
        }

        seam.forEach { image[it.y][it.x] = -1.0 }
    }

    private fun findSum() {
        sum[energy.lastIndex] = energy[energy.lastIndex]

        for (i in (energy.lastIndex - 1) downTo 0) {
            for (j in energy[0].indices) {
                sum[i][j] = sum[i + 1][j]
                if (j > 0 && sum[i + 1][j - 1] < sum[i][j]) sum[i][j] = sum[i + 1][j - 1]
                if (j < energy[0].lastIndex && sum[i + 1][j + 1] < sum[i][j]) sum[i][j] = sum[i + 1][j + 1]

                sum[i][j] += energy[i][j]
            }
        }
    }
}