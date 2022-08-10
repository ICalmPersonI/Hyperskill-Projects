package calculator

import calculator.engine.Engine
import java.lang.NullPointerException
import java.lang.NumberFormatException
import java.math.BigInteger
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayDeque

class Application {

    private val patternOfVariable: Regex = Regex("[A-Za-z]+")
    private val patternOfEquation: Regex = Regex("[+/()*]+")
    private val patternOfVariableAssignment: Regex = Regex(".+=.+")
    private val patternOfDigit: Regex = Regex("[-+]*\\d+")
    private val patternOfValueOfVariable: Regex = Regex("[-+]*\\d+|[A-Za-z]+")

    private val variableStorage: MutableMap<String, BigInteger> = mutableMapOf()

    fun run() {
        val scanner: Scanner = Scanner(System.`in`)
        while (true) {
            val inputLine: String = scanner.nextLine().replace("\\s++".toRegex(), "")
            when {
                patternOfDigit.matches(inputLine) -> println(inputLine.replace("+", ""))
                inputLine.isEmpty() -> continue
                inputLine.contains("/") && !inputLine.contains(patternOfDigit) -> {
                    when (inputLine) {
                        "/help" -> println("The program calculates the sum of numbers")
                        "/exit" -> break
                        else -> println("Unknown command")
                    }
                }
                patternOfVariable.matches(inputLine) -> showValueOfVariable(inputLine)
                else -> {
                    when {
                        patternOfEquation.find(inputLine)?.value?.length != null -> equation(inputLine)
                        patternOfVariableAssignment.matches(inputLine) -> variable(inputLine)
                        else -> println("Unknown variable")
                    }
                }
            }
        }
        println("Bye!")
    }

    private fun showValueOfVariable(line: String) {
        println(if (variableStorage.containsKey(line)) variableStorage[line] else "Unknown variable")
    }

    private fun variable(line: String) {
        val splitVariable: List<String> = line.split("=".toRegex())
        when {
            !patternOfVariable.matches(splitVariable[0]) -> println("Invalid identifier")
            !patternOfValueOfVariable.matches(splitVariable[1]) -> println("Invalid assignment")
            else -> {
                try {
                    variableStorage[splitVariable[0]] = BigInteger(splitVariable[1])
                } catch (e: NumberFormatException) {
                    val value: BigInteger =
                        BigInteger(variableStorage.getOrDefault(splitVariable[1], 0).toString())
                    if (variableStorage.containsKey(splitVariable[1])) variableStorage[splitVariable[0]] = value
                    else println("Invalid assignment")
                }
            }
        }
    }

    private fun equation(line: String) {
        when {
            (line.contains(")") && !line.contains("("))
                    || (line.contains("(") && !line.contains(")"))
                    || line.contains("**")
                    || line.contains("//") -> println("Invalid expression")
            else -> {
                var allVariablesAvailable: Boolean = true
                line.filter { it.isLetter() }.forEach {
                    allVariablesAvailable = allVariablesAvailable and variableStorage.containsKey(it.toString())
                }
                if (allVariablesAvailable) {
                    val calculationEngine: Engine = Engine(line, variableStorage)
                    println(calculationEngine.calculation())
                } else println("Unknown variable")
            }
        }
    }
}

