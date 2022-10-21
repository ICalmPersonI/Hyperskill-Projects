package machine

class Menu {

    private val machine: Machine = Machine()

    fun run() {
        while (true) {
            print("Write action (buy, fill, take, remaining, exit):")
            when (readLine()!!) {
                "buy" -> buy()
                "fill" -> fill()
                "take" -> println(machine.take())
                "remaining" -> println(machine.toString())
                "exit" -> break
            }
            println()
        }
    }

    private fun buy() {
        print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
        println(when (readLine()!!) {
            "1" -> machine.makeCoffee(250, 0, 16, 1, 4)
            "2" -> machine.makeCoffee(350, 75, 20, 1, 7)
            "3" -> machine.makeCoffee(200, 100, 12, 1, 6)
            else -> ""
        })
    }

    private fun fill() {
        print("Write how many ml of water do you want to add:")
        val water: Int =  readLine()!!.toInt()
        print("Write how many ml of milk do you want to add:")
        val milk: Int = readLine()!!.toInt()
        print("Write how many grams of coffee beans do you want to add:")
        val beans: Int = readLine()!!.toInt()
        print("Write how many disposable cups of coffee do you want to add:")
        val cups: Int = readLine()!!.toInt()
        println(machine.fill(water, milk, beans, cups))
    }

}