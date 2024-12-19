package com.dshevarev.kursovaya.register

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper

class ChangeUserRoleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_role)

        val userEmail = intent.getStringExtra("useremail")
        val dbHelper = DBHelper(this, null)
        val userId = dbHelper.getUserIdByEmail(userEmail!!)
        val user = dbHelper.getUserById(userId)

        val emailTextView: TextView = findViewById(R.id.change_user_role_email)
        val roleTextView: TextView = findViewById(R.id.change_user_role_role)
        val changeRoleButton: Button = findViewById(R.id.change_user_role_button)

        emailTextView.text = user?.email
        roleTextView.text = if (user?.isAdmin == true) "Admin" else "User"

        changeRoleButton.setOnClickListener {
            val newRole = !user!!.isAdmin
            dbHelper.updateUserRole(userId, newRole)
            finish()
        }
    }
}