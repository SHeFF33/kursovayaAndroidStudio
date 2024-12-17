package com.dshevarev.kursovaya.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.models.User

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPhone: EditText = findViewById(R.id.user_phone)
        val userPassword: EditText = findViewById(R.id.user_pass)
        val button: Button = findViewById(R.id.button_reg)
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val phone = userPhone.text.toString().trim()
            val pass = userPassword.text.toString().trim()

            if (login.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else if (pass.length < 8) {
                Toast.makeText(this, "Пароль должен быть не менее 8 символов", Toast.LENGTH_LONG).show()
            } else if(phone.length > 11){
                Toast.makeText(this, "Номер телефона не должен превышать 11 символов", Toast.LENGTH_LONG).show()
            } else {
                val db = DBHelper(this, null)
                if (db.isEmailUnique(email)) {
                    val user = User(email, login, phone, pass)
                    db.addUser(user)
                    Toast.makeText(this, "Пользователь $email зарегистрирован", Toast.LENGTH_LONG).show()

                    userPhone.text.clear()
                    userLogin.text.clear()
                    userEmail.text.clear()
                    userPassword.text.clear()
                } else {
                    Toast.makeText(this, "Email уже зарегистрирован, укажите другой", Toast.LENGTH_LONG).show()
                }
            }
        }

        linkToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}
