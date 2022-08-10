package org.hyperskill.calculator.tip

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calcTip()
    }

    private fun calcTip() {
        val input: EditText = findViewById(R.id.edit_text)
        val rate: Slider = findViewById(R.id.slider)
        val view: TextView = findViewById(R.id.text_view)
        val result: (Float, Float) -> String =
            {number: Float, percent: Float -> "Tip amount: %.2f".format(number * (percent / 100)) }

        input.addTextChangedListener {
            if (input.text.isEmpty()) {
                view.text = ""
            } else {
                view.text = result.invoke(input.text.toString().toFloat(), rate.value)
            }
        }

        rate.addOnChangeListener {_, value, _ ->
            if (input.text.isNotEmpty()) {
                view.text = result.invoke(input.text.toString().toFloat(), value)
            }
        }
    }
}

