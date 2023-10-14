package com.example.calcapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow


class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var calcText: TextView
    private var operand1 = 0.0
    private var operator = ""
    private var canAddDecimal = true
    private var canAddOperation = true
    private var canAddEquals = false


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.resultText)
        calcText = findViewById(R.id.calcText)

        findViewById<Button>(R.id.delButton).setOnLongClickListener {
            onClearClick()
            true
        }
    }

    @SuppressLint("SetTextI18n")
    fun onDigitClick(view: View) {
        if (view is Button && resultText.text.toString() != "NaN") {
            if(view.text.toString() == ".") {
                if (canAddDecimal && resultText.text.toString() != "-") {
                    resultText.append(view.text.toString())
                }
                canAddDecimal = false
            } else if (view.text.toString() == "%") {
                if(resultText.text.toString() != "-"){
                    var enteredNum = resultText.text.toString().toDouble()
                    enteredNum /= 100.0
                    resultText.text = enteredNum.toString()
                }
            }else if (view.text.toString() == "1/x") {
                if (resultText.text.toString() != "0"){
                    var enteredNum = resultText.text.toString().toDouble()
                    enteredNum = 1/enteredNum
                    resultText.text = enteredNum.toString()
                }
            } else {
                if (resultText.text.toString() == "0")
                    resultText.text = view.text.toString()
                else
                    resultText.append(view.text.toString())

                canAddOperation = true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun onOperatorClick(view: View) {
        if (view is Button && resultText.text.toString() != "-") {
            if (canAddOperation) {
                onEqualsClick(view)
                canAddOperation = false
                operand1 = resultText.text.toString().toDouble()
                if(!operand1.isNaN()) {
                    var resText = resultText.text.toString()
                    operator = view.text.toString()

                    if (resText.endsWith("."))
                        resText = resText.removeSuffix(".")

                    calcText.text = "$resText $operator"
                    resultText.text = "0"
                    canAddDecimal = true
                    canAddEquals = true
                }
            }
            else {
                operator = view.text.toString()
                calcText.text = calcText.text.subSequence(0, calcText.length() - 1)
                calcText.append(view.text.toString())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun onEqualsClick(view: View) {
        if(resultText.text.toString() != "-" && canAddEquals){
            val operand2 = resultText.text.toString().toDouble()
            val result = when (operator) {
                "+" -> operand1 + operand2
                "-" -> operand1 - operand2
                "×" -> operand1 * operand2
                "÷" -> if (operand2 != 0.0) operand1 / operand2 else Double.NaN
                "^" -> operand1.pow(operand2)
                else -> operand2
            }
            val df = DecimalFormat("#.#####")
            var roundRes = df.format(result).replace(",", ".")
            if (roundRes == "-0") roundRes = "0"

            var resText = resultText.text.toString()

            if (resText.endsWith("."))
                resText = resText.removeSuffix(".")

            canAddEquals = false
            calcText.append(" $resText =")
            if(result.isNaN()) {
                resultText.text = "NaN"
                return
            }
            resultText.text = roundRes
        }
    }

    private fun onClearClick() {
        resultText.text = "0"
        calcText.text = ""
        operator = ""
        canAddDecimal = true
        canAddOperation = true
    }

    fun onDeleteClick(view: View) {
        if (resultText.length() > 0) {
            if (resultText.text.toString() == "NaN" || resultText.length() == 1) {
                onClearClick()
                return
            }
            resultText.text = resultText.text.subSequence(0, resultText.length() - 1)
        }
    }
}
