package com.example.maksimdimitrov.calculator.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.maksimdimitrov.calculator.R
import com.example.maksimdimitrov.calculator.utilities.OPERATOR_ADD
import com.example.maksimdimitrov.calculator.utilities.OPERATOR_DIVIDE
import com.example.maksimdimitrov.calculator.utilities.OPERATOR_MULTIPLY
import com.example.maksimdimitrov.calculator.utilities.OPERATOR_SUBSCTRACT
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var valueOne = 0.0
    var valueTwo = 0.0
    var operator = ""
    var removeText = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNumbersOnClickListeners()
        setActionsOnClickListeners()
    }

    fun setNumbersOnClickListeners() {
        val numbers = listOf(btn_zero
                , btn_one
                , btn_two
                , btn_three
                , btn_four
                , btn_five
                , btn_six
                , btn_seven
                , btn_eight
                , btn_nine)

        for (el in numbers) {
            el.setOnClickListener {
                if (removeText) {
                    input_field.setText("")
                    removeText = false
                }
                input_field.setText("${input_field.text}${el.text}")
            }
        }
    }

    fun setActionsOnClickListeners() {

        val operators = mapOf(
                btn_divide to OPERATOR_DIVIDE
                , btn_mult to OPERATOR_MULTIPLY
                , btn_substract to OPERATOR_SUBSCTRACT
                , btn_add to OPERATOR_ADD)

        for (pair in operators) {
            pair.key.setOnClickListener {
                if (input_field.text.isNotEmpty() && !containsOtherOperator(input_field.text.substring(input_field.text.length - 1))) {
                    if (containsOtherOperator()) {
                        computeResult()
                    }
                    operator = pair.value
                    valueOne = (input_field.text.toString()).toDouble()
                    input_field.setText("${input_field.text}$operator")
                    removeText = false
                }
            }
        }

        btn_dot.setOnClickListener {
            val zero = getString(R.string.zero)
            val dot = getString(R.string.dot)
            val inputText = input_field.text.toString()
            var resultStr = ""
            if (operator == "") {
                if (!inputText.contains(dot)) {
                    resultStr = if (inputText.isEmpty()) "$zero$dot" else dot
                }
            } else {
                val strTwo = inputText.substring(inputText.indexOf(operator) + 1)
                if (!strTwo.contains(dot)) {
                    resultStr = if (strTwo.isEmpty()) "$zero$dot" else dot
                }
            }
            input_field.setText("$inputText$resultStr")
        }

        btn_del.setOnClickListener {
            if (input_field.text.isNotEmpty()) {
                val formatedInput = input_field.text.substring(0, input_field.text.length - 1)
                input_field.setText(formatedInput)
            }
        }

        btn_del.setOnLongClickListener {
            input_field.setText("")
            true
        }

        btn_equal.setOnClickListener {
            computeResult()
            removeText = true
        }
    }

    fun containsOtherOperator(input: String = input_field.text.toString()): Boolean {
        return input.contains(OPERATOR_DIVIDE)
                || input.contains(OPERATOR_ADD)
                || input.contains(OPERATOR_MULTIPLY)
                || (input.contains(OPERATOR_SUBSCTRACT) && input.indexOf(OPERATOR_SUBSCTRACT) != 0)

    }

    fun computeResult() {
        val inputText = input_field.text.toString()
        if (inputText.isNotEmpty() && operator.isNotEmpty()) {
            val valueTwoString = inputText.substring(inputText.indexOf(operator) + 1)
            if (valueTwoString.isNotEmpty()) {
                valueTwo = valueTwoString.toDouble()
                val result = when (operator) {
                    OPERATOR_ADD -> valueOne + valueTwo
                    OPERATOR_SUBSCTRACT -> valueOne - valueTwo
                    OPERATOR_MULTIPLY -> valueOne * valueTwo
                    OPERATOR_DIVIDE -> valueOne / valueTwo
                    else -> Double.NaN
                }
                valueOne = result
                valueTwo = 0.0
                input_field.setText((if (result % 1.0 == 0.0) result.toLong() else result).toString())
                operator = ""
            }
        }

    }
}

