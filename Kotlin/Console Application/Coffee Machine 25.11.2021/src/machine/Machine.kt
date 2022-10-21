package machine

data class Machine(
    private var water: Int = 400,
    private var milk: Int = 540,
    private var beans: Int = 120,
    private var cups: Int = 9,
    private var money: Int = 550
) {

    fun fill(water: Int, milk: Int, beans: Int, cups: Int) {
        this.water += water
        this.milk += milk
        this.beans += beans
        this.cups += cups
    }

    fun makeCoffee(water: Int, milk: Int, beans: Int, cups: Int, money: Int): String {
        return when {
            this.water - water <= 0 -> "Sorry, not enough water!"
            this.milk - milk <= 0 -> "Sorry, not enough milk!"
            this.beans - beans <= 0 -> "Sorry, not enough coffee beans!"
            this.cups - cups <= 0 -> "Sorry, not enough cups!"
            else -> {
                this.water -= water
                this.milk -= milk
                this.beans -= beans
                this.money += money
                this.cups--
                "I have enough resources, making you a coffee!"

            }
        }
    }

    fun take(): String {
        val temp: Int = money
        this.money = 0
        return "I gave you \$$temp"
    }

    override fun toString(): String {
        return "The coffee machine has:\n" +
                "$water of water\n" +
                "$milk of milk\n" +
                "$beans of coffee beans\n" +
                "$cups of disposable cups\n" +
                "$money of money"
    }
}