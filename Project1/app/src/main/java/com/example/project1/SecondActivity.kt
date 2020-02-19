package com.example.project1

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_second.*


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val receivedInfo = intent.getIntExtra("Type", 0)
        val user = Users.getCurrentUser()
        if(receivedInfo == 0){
            val message = "Welcome ${user?.userName}, thank you for joining "
            welcomeMessage.text = message
        }else if(receivedInfo == 1){
            val message = "Welcome back ${user?.userName}, thank you for your continued use!"
            welcomeMessage.text = message
        }
        logout.setOnClickListener{
            Users.logout()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
