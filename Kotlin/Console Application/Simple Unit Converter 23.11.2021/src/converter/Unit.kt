package converter

data class Unit(val pattern: Regex, val rate: Double, val singular: String, val plural: String, val type: String)
