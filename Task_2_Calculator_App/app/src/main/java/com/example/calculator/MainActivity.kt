package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastnum=false
    private var stateerror=true
    private var show =false
    private var lastdot=false
    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onAllClearClick(view: View) {
        binding.datatview.text=""
        binding.resultview.text=""
        stateerror=true
        lastnum=false
        lastdot=false
        binding.resultview.visibility=View.GONE
        checkspace()
    }

    fun onClearClick(view: View) {
        binding.datatview.text=""
        lastnum=false
        checkspace()
    }

    fun onOperatorClick(view: View) {
        if(!stateerror&&lastnum){
            binding.datatview.append((view as Button).text)
            lastdot=false
            lastnum=false
            onEqual()

        }
    }

    fun digits(view: View) {
        checkspace()
        if(stateerror) {

            if ((view as Button).text== binding.btndot.text) {
                binding.datatview.text = binding.btn0.text
                binding.datatview.append(binding.btndot.text)
                stateerror=false
            }
            else {
                binding.datatview.text = (view as Button).text
                stateerror = false
            }
        }
        else{
            if ((view as Button).text== binding.btndot.text){
            if(!binding.datatview.text.contains(binding.btndot.text))
            binding.datatview.append((view as Button).text)
            }
            else{
                binding.datatview.append((view as Button).text)
            }
        }
        lastnum=true
        onEqual()
    }
    fun checkspace(){
        if (binding.datatview.text=="")
        {
            stateerror=true
        }
    }

    fun onbackClick(view: View) {
        binding.datatview.text=binding.datatview.text.reversed().drop(1)
        binding.datatview.text=binding.datatview.text.reversed().toString()

        try {
            val lastchar = binding.datatview.text.toString().last()
            if(lastchar.isDigit()){
                onEqual()
                lastnum=true
            }

        }
        catch (e:Exception){
            binding.resultview.text=""
            stateerror=true
            binding.resultview.visibility=View.GONE
            Log.e("last char error",e.toString())
        }
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.datatview.text=binding.resultview.text.toString().drop(1)

    }

    fun onEqual() {
        if (lastnum && !stateerror) {
            val txt = binding.datatview.text.toString()
            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                val roundedResult = String.format("%.2f", result)
                binding.resultview.visibility = View.VISIBLE
                binding.resultview.text = buildString {
                    append("=")
                    append(roundedResult)
                }
            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.resultview.text = buildString {
                    append("Error")
                }
                stateerror = true
                lastnum = false
            }
        }
    }

    fun specialClick(view: View){
val clicked=view as Button
    val buttontext=clicked.text.toString()
    if (!stateerror)
    {
    when(buttontext)
    {
        "√" ->{
            val value=binding.datatview.text.toString().toFloat()
            binding.datatview.text= kotlin.math.sqrt(value).toString()
        }
        "π" -> {
            var value=binding.datatview.text.toString().toFloat()
            value= (value*3.14).toFloat()
            binding.datatview.text=value.toString()
        }
        "x²" -> {

            val inputValue = binding.datatview.text.toString().toDouble()
            val result = inputValue.pow(2)
            binding.datatview.text = result.toString()
        }
        "!" -> {
            val inputValue = binding.datatview.text.toString().toInt()
            if (inputValue < 21) {
                val result = factorial(inputValue)
                binding.datatview.text = result.toString()
            }
        }
        "°" -> {
            val inputValue = binding.datatview.text.toString().toDouble()
            val result = Math.toRadians(inputValue)
            binding.datatview.text = result.toString()
        }
        "sin" -> {
            val inputValue = binding.datatview.text.toString().toDouble()
            val result = Math.sin(Math.toRadians(inputValue))
            binding.datatview.text = result.toString()
        }
        "cos" -> {
            val inputValue = binding.datatview.text.toString().toDouble()
            val result = Math.cos(Math.toRadians(inputValue))
            binding.datatview.text = result.toString()
        }
        "tan" -> {
            val inputValue = binding.datatview.text.toString().toDouble()
            val result = Math.tan(Math.toRadians(inputValue))
            binding.datatview.text = result.toString()
        }
        "Inv" -> {
            val inputValue = binding.datatview.text.toString().toDouble()
            val result = 1 / inputValue
            binding.datatview.text = result.toString()
        }
        "e" -> {
            var value=binding.datatview.text.toString().toDouble()
            value=value*2.7183
            binding.datatview.text = value.toString()
        }
        "ln" -> {
            val inputValue = binding.datatview.text.toString().toDouble()
            val result = ln(inputValue)
            binding.datatview.text = result.toString()
        }
        "log" -> {
            val inputValue = binding.datatview.text.toString().toDouble()
            val result = log10(inputValue)
            binding.datatview.text = result.toString()
        }

    }
        onEqual()
}
}
    private fun factorial(n: Int): Long {
        return if (n == 0 || n == 1) 1
        else n * factorial(n - 1)
    }
    fun dropdown(view:View) {
        if(show){
            binding.hide1.visibility=View.GONE
            binding.hide2.visibility=View.GONE
            binding.btndropdown.icon=getDrawable(R.drawable.up)
            show=false
        }
        else {
            binding.hide1.visibility = View.VISIBLE
            binding.hide2.visibility = View.VISIBLE
            binding.btndropdown.icon=getDrawable(R.drawable.down)
            show =true
        }
    }

}