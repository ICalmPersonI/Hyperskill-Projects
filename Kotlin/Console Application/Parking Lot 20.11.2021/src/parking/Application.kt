package parking

import kotlin.system.exitProcess

class Application {

    private var parking: Parking? = null
    private val space: Regex = Regex("\\s")

    fun run() {
        while (true) {
            val input: List<String> = readLine()!!.split(space)

            if (input[0] == "exit" || input[0] == "create" || parking != null) {
                when (input[0]) {
                    "create" -> {
                        val size: Int = input[1].toInt()
                        parking = Parking(size)
                        println("Created a parking lot with $size spots.")
                    }
                    "park" -> {
                        val regNumber: String = input[1].lowercase()
                        val carColor: String = input[2].lowercase()
                        val spotNumber: Int = parking!!.park(Spot(regNumber, carColor))
                        if (spotNumber != -1) println("${carColor.uppercase()} car parked in spot $spotNumber.")
                        else println("Sorry, the parking lot is full.")
                    }
                    "leave" -> {
                        val spotNumber: Int = input[1].toInt()
                        if (parking!!.leave(spotNumber)) println("Spot $spotNumber is free.")
                        else println("There is no car in spot $spotNumber.")
                    }
                    "status" -> {
                        val status: List<String> = parking!!.getStats()
                        if (status.isEmpty()) println("Parking lot is empty.")
                        else parking!!.getStats().forEach { println(it) }
                    }
                    "reg_by_color" -> {
                        val color: String = input[1].lowercase()
                        val regs: List<String> = parking!!.getRegByColor(color)
                        if (regs.isEmpty()) println("No cars with color ${color.uppercase()} were found.")
                        else regs.forEachIndexed { index, elem ->
                            print(
                                if (index == regs.lastIndex) "${elem.replace(",", "")}\n"
                                else "$elem "
                            )
                        }

                    }
                    "spot_by_color" -> {
                        val color: String = input[1].lowercase()
                        val spots: List<String> = parking!!.getSpotsBy(color, "")
                        if (spots.isEmpty()) println("No cars with color ${color.uppercase()} were found.")
                        else spots.forEachIndexed { index, elem ->
                            print(
                                if (index == spots.lastIndex) "${elem.replace(",", "")}\n"
                                else "$elem "
                            )
                        }
                    }
                    "spot_by_reg" -> {
                        val reg: String = input[1].lowercase()
                        val spots: List<String> = parking!!.getSpotsBy("", reg)
                        if (spots.isEmpty()) println("No cars with registration number ${reg.uppercase()} were found.")
                        else spots.forEachIndexed { index, elem ->
                            print(
                                if (index == spots.lastIndex) "${elem.replace(",", "")}\n"
                                else "$elem "
                            )
                        }
                    }
                    "exit" -> exitProcess(0)
                }
            } else println("Sorry, a parking lot has not been created.")
        }
    }
}