package org.hyperskill.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.lang.NumberFormatException
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttons()
    }

    private enum class Operation {
        NONE,
        EQUAL,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }

    private fun buttons() {
        val edit: EditText = findViewById(R.id.editText)
        val dot: Button = findViewById(R.id.dotButton)
        val clear: Button = findViewById(R.id.clearButton)
        val numbers: List<Button> = listOf(
            findViewById(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9)
        )
        val operations: List<Button> = listOf (
            findViewById(R.id.addButton),
            findViewById(R.id.subtractButton),
            findViewById(R.id.multiplyButton),
            findViewById(R.id.divideButton),
            findViewById(R.id.equalButton)
        )

        var total: Double = 0.0
        var lastOperation: Operation = Operation.NONE

        clear.setOnClickListener {
            edit.hint = "0"
            edit.setText("")
            total = 0.0
            lastOperation = Operation.NONE
        }

        dot.setOnClickListener {
            val input: String = edit.text.toString()
            if (input.contains('-') && input.length == 1) edit.setText("-0.")
            else if (!input.contains('.')) edit.setText(edit.text.toString() + ".")
        }

        numbers.forEach { b ->
            b.setOnClickListener {
                val input: String = edit.text.toString()
                when {
                    input == "0" -> edit.setText(b.text)
                    !(input == "-" && b.id == R.id.button0) -> edit.append(b.text)
                }
                edit.hint = edit.text

            }
        }

        operations.forEach { b ->
            b.setOnClickListener {
                try {
                    val input: Double = edit.text.toString().toDouble()
                    when(b.id) {
                        R.id.addButton -> total = calculate(total, input, lastOperation)
                            .also { lastOperation = Operation.ADD }.also { edit.setText("") }
                        R.id.subtractButton -> {
                            if (input == 0.0) {
                                edit.setText("-")
                            } else {
                                total = calculate(total, input, lastOperation)
                                lastOperation = Operation.SUBTRACT
                                edit.setText("")
                            }
                        }
                        R.id.multiplyButton -> total = calculate(total, input, lastOperation)
                            .also { lastOperation = Operation.MULTIPLY }.also { edit.setText("") }
                        R.id.divideButton -> total = calculate(total, input, lastOperation)
                            .also { lastOperation = Operation.DIVIDE }.also { edit.setText("") }
                        R.id.equalButton -> {
                            total = calculate(total, input, lastOperation)
                            lastOperation = Operation.EQUAL
                            edit.setText(formatView(total))
                            edit.hint = ""
                        }
                        else -> throw Exception()
                    }
                } catch (e: NumberFormatException) {
                    edit.setText(formatView(total))
                }
            }
        }
    }

    private fun formatView(output: Double): String = DecimalFormat("0.#").format(output).toString()

    private fun calculate(first: Double, second: Double,  operation: Operation): Double {
        return when(operation) {
            Operation.ADD -> first + second
            Operation.SUBTRACT -> first - second
            Operation.MULTIPLY -> first * second
            Operation.DIVIDE -> first / second
            Operation.EQUAL -> first
            Operation.NONE -> second
        }
    }
}
