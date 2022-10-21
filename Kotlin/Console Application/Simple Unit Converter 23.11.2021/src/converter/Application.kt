package converter

import kotlin.system.exitProcess


class Application {

    private val separator: Regex = Regex("\\s+")
    private val query: Regex = Regex("[+-]*\\d+\\.*\\d*\\s(.+\\s*.*)\\s.+\\s.+");

    private val units: List<Unit> = listOf(
        Unit(Regex("(m)|(meter)|(meters)"), 1.0,
            "meter", "meters", "length"),
        Unit(Regex("(mm)|(millimeter)|(millimeters)"), 0.001,
            "millimeter", "millimeters", "length"),
        Unit(Regex("(cm)|(centimeter)|(centimeters)"), 0.01,
            "centimeter", "centimeters", "length"),
        Unit(Regex("(km)|(kilometer)|(kilometers)"), 1000.0,
            "kilometer", "kilometers", "length"),
        Unit(Regex("(mi)|(mile)|(miles)"), 1609.35,
            "mile", "miles", "length"),
        Unit(Regex("(yd)|(yard)|(yards)"), 0.9144,
            "yard", "yards", "length"),
        Unit(Regex("(ft)|(foot)|(feet)"), 0.3048,
            "foot", "feet", "length"),
        Unit(Regex("(in)|(inch)|(inches)"), 0.0254,
            "inch", "inches", "length"),
        Unit(Regex("(g|gram|grams)"), 1.0,
            "gram", "grams", "weight"),
        Unit(Regex("(kg)|(kilogram)|(kilograms)"), 1000.0,
            "kilogram", "kilograms", "weight"),
        Unit(Regex("(mg)|(milligram)|(milligrams)"), 0.001,
            "milligram", "milligrams", "weight"),
        Unit(Regex("(lb)|(pound)|(pounds)"), 453.592,
            "pound", "pounds", "weight"),
        Unit(Regex("(oz)|(ounce)|(ounces)"), 28.3495,
            "ounce", "ounces", "weight"),
        Unit(Regex("(c)|(dc)|(celsius)"), 28.3495,
            "degree Celsius", "degrees Celsius", "temperature"),
        Unit(Regex("(f)|(df)|(fahrenheit)"), 28.3495,
            "degree Fahrenheit", "degrees Fahrenheit", "temperature"),
        Unit(Regex("(k)|(kelvin)|(kelvins)"), 28.3495,
            "kelvin", "kelvins", "temperature"),
    )

    /*
    Input format
    <number> +
    <(unit name) or (degree + unit name) or (degrees + unit name)> +
    <random word like "to" or "in"> +
    <(unit name) or (degree + unit name) or (degrees + unit name)>
    Examples:
    1 degree Celsius to kelvins
    1 kn to feet
    3 pount to ounces
    3 kelvins to grams
    1 F in K
     */
    fun run() {
        while (true) {
            print("Enter what you want to convert (or exit): ")
            val input: String = readLine()!!.lowercase() // input format
            if (input == "exit") exitProcess(0)
            else if (!input.matches(query)) println("Parse error")
            else {
                val parsedInput: List<String> = input.split(separator)
                val value: Double = parsedInput[0].toDouble()

                val firstUnit: Unit? = units.firstOrNull {
                    parsedInput[
                            if (units.none { e -> parsedInput[1].matches(e.pattern) }
                                && parsedInput[1].matches("degree[s]*".toRegex())) 2
                            else 1
                    ].matches(it.pattern)
                }
                val secondUnit: Unit? = units.firstOrNull { parsedInput[parsedInput.lastIndex].matches(it.pattern) }
                when {
                    firstUnit == null || secondUnit == null ->
                        println("Conversion from ${firstUnit?.plural ?: "???"} " +
                                "to ${secondUnit?.plural ?: "???"} is impossible")
                    firstUnit.type != secondUnit.type ->
                        println("Conversion from ${firstUnit.plural} to ${secondUnit.plural} is impossible")
                    value < 0 && firstUnit.type != "temperature" ->
                        println("${firstUnit.type.first().uppercase() + firstUnit.type.substring(1)} " +
                                "shouldn't be negative")
                    else -> {
                        val result: Double =
                            if (firstUnit.type == "temperature") fromCelsius(toCelsius(value, firstUnit), secondUnit)
                            else fromMeters(toMeters(value, firstUnit), secondUnit)

                        println(
                            "$value ${if (value == 1.0) firstUnit.singular else firstUnit.plural} " +
                                    "is $result ${if (result == 1.0) secondUnit.singular else secondUnit.plural}"
                        )
                    }
                }
            }
        }
    }

    private fun toMeters(value: Double, unit: Unit): Double = value * unit.rate
    private fun fromMeters(value: Double, unit: Unit): Double = value / unit.rate


    private fun toCelsius(value: Double, unit: Unit): Double {
        return when (unit.singular) {
            "degree Fahrenheit" -> (value - 32) * 5 / 9
            "kelvin" -> value - 273.15
            else -> value
        }
    }

    private fun fromCelsius(value: Double, unit: Unit): Double {
        return when (unit.singular) {
            "degree Fahrenheit" -> (value * 9 / 5) + 32
            "kelvin" -> value + 273.15
            else -> value
        }
    }

}