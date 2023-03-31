package com.example.tippy

import android.animation.ArgbEvaluator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount : EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel:TextView
    private lateinit var tvTipAmount:TextView
    private lateinit var tvTotalAmount:TextView
    private lateinit var tvTipDiscription:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDiscription = findViewById(R.id.tvTipDiscription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT %"
        updateTipDiscription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG,"onProgressChange $p1")
                tvTipPercentLabel.text = "$p1 %"
                computeTipAndTotal()
                updateTipDiscription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        etBaseAmount.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChange $p0")
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDiscription(tipPercent: Int) {
        val tipDiscription = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..20 -> "Good"
            in 21..25 -> "Great"
            else -> "Amazing"
        }
        tvTipDiscription.text = tipDiscription
//        color for tip discription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this,R.color.bad_color),
            ContextCompat.getColor(this,R.color.best_color)
        )as Int

        tvTipDiscription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        //1 Get the value from base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        //2 Compute the tip and total amount
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = tipAmount + baseAmount
        //3 Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
//        tvTotalAmount.text = totalAmount.toString()
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}