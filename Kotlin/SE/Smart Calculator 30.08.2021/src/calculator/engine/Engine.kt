package calculator.engine

import java.math.BigInteger
import java.util.*

class Engine(private var inputLine: String, private val variableStorage: MutableMap<String, BigInteger>) {


    private val isLetterOrDigit: Regex = Regex("-*\\d+|[A-Za-z]+")
    private val isLetter: Regex = Regex("[A-Za-z]+")
    private val isOperator: Regex = Regex("[*/+-]+")

    fun calculation(): BigInteger {
        val equation:  String = infixToPostfix()
        val stack: Stack<BigInteger> = Stack()
        for (elem in equation.split(" ")) {
            if (elem.isEmpty()) continue
            if (isLetterOrDigit.matches(elem)) {
                if (isLetter.matches(elem)) stack.push(toBI(variableStorage.getOrDefault(elem, 0).toString()))
                else stack.push(toBI(elem))
            } else {
                val first: BigInteger = stack.pop()
                val second: BigInteger = stack.pop()
                when (elem) {
                    "+" -> stack.push(first + second)
                    "-" -> stack.push(second - first)
                    "*" -> stack.push(first * second)
                    "/" -> stack.push(second / first)
                }
            }
        }
        return stack.pop()
    }

    private fun toBI(number: String): BigInteger = BigInteger(number)

    private fun infixToPostfix(): String {
        var result: String = ""
        val stack: Stack<String> = Stack()
        for (elem in preparationEquation().split(" ")) {
            when {
                isLetterOrDigit.matches(elem) -> result += "$elem "
                elem == "(" -> stack.push(elem)
                elem == ")" -> {
                    while (!stack.isEmpty() && stack.peek() != "(") result += "${stack.pop()} "
                    stack.pop()
                }
                else -> {
                    while (!stack.isEmpty() && getPriority(elem) <= getPriority(stack.peek())) result += "${stack.pop()} "
                    stack.push(elem)
                }
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek() == "(") return "Invalid expression"
            result += "${stack.pop()} "
        }
        return result
    }

    private fun getPriority(operator: String): Int {
        when (operator) {
            "+", "-" -> return 1
            "*", "/" -> return 2
        }
        return -1
    }


    private fun preparationEquation(): String {
        val equation: String = inputLine.replace("--", "+").replace("[+]+".toRegex(), "+")
        var finallyEquation: String = ""
        var i: Int = 0
        while (i < equation.length - 1) {
            when {
                equation[i].isLetter() ->  finallyEquation += "${variableStorage[equation[i].toString()]} "
                (i == 0 || isOperator.matches(equation[i - 1].toString()))
                        && equation[i] == '-' && equation[i + 1].isDigit() -> finallyEquation += "${equation[i]}"
                isOperator.matches(equation[i].toString()) ->  finallyEquation += "${equation[i]} "
                equation[i] == ')' || equation[i] == '(' -> finallyEquation += "${equation[i]} "
                equation[i].isDigit() && !equation[i + 1].isDigit() -> finallyEquation += "${equation[i]} "
                !equation[i].isDigit() && equation[i + 1].isDigit() -> finallyEquation += "${equation[i]} "
                else -> finallyEquation += equation[i]
            }
            i++
        }

        return finallyEquation +
                if (inputLine.last().isLetter()) variableStorage[inputLine.last().toString()] else inputLine.last()
    }
}