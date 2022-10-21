package parking

data class Spot(
    var registrationNumber: String,
    var carColor: String
) {
    override fun toString(): String = "$registrationNumber $carColor"
}
