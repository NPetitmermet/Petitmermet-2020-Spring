package HW1.nicho.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        concat.setOnClickListener {
            val inputOne = editText.text.toString()
            val inputTwo = editText2.text.toString()
            val output = "$inputOne$inputTwo"
            text1.text = output
        }
        add.setOnClickListener {
            var convertOne = 0;
            var convertTwo = 0;
            try {
                convertOne = editText.text.toString().toInt()
            } catch(e:NumberFormatException){
            }
            try {
                convertTwo = editText2.text.toString().toInt()
            } catch(e:NumberFormatException){
            }
            val output = convertOne + convertTwo
            text2.text = output.toString()
        }
    }
}
