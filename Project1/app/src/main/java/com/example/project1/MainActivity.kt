package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton.setOnClickListener {
            val user = UserModel(usernameField.text.toString(),passwordField.text.toString())
            if(Users.login(user)) {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("Type", 1)
                startActivityForResult(intent, 1)
            }
        }

        createAccountButton.setOnClickListener{
            val newUser = UserModel(usernameField.text.toString(),passwordField.text.toString())
            Users.addUser(newUser)
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("Type", 0)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                loggedOut.text = getString(R.string.successful_logout)
            } else {
                loggedOut.text = getString(R.string.unsuccessful_logout)
            }
            usernameField.text.clear()
            passwordField.text.clear()
        }
    }
}
