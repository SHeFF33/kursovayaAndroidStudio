package com.dshevarev.kursovaya.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.utils.SharedPreferencesHelper
import com.dshevarev.kursovaya.products.ItemsActivity

class ProfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prof)
        val dbHelper = DBHelper(this, null)

        val emailTextView: TextView = findViewById(R.id.profile_email)
        val loginTextView: TextView = findViewById(R.id.profile_login)
        val phoneTextView: TextView = findViewById(R.id.profile_phone)
        val itemsList: RecyclerView = findViewById(R.id.profile_items_list)
        val noItemsTextView: TextView = findViewById(R.id.profile_no_items)
        val back: ImageView = findViewById(R.id.profile_item_list_back)
        val onLogin: ImageView = findViewById(R.id.profile_onLogin)
        val sale: Button = findViewById(R.id.profile_item_list_sale)
        val adminpanel: Button = findViewById(R.id.profile_admin)

        val userEmail = intent.getStringExtra("useremail") ?: SharedPreferencesHelper.getUserEmail(this)
        if (userEmail == null) {
            Toast.makeText(this, "Ошибка: email не найден", Toast.LENGTH_LONG).show()
            return
        }

        val userId = dbHelper.getUserIdByEmail(userEmail)
        val user = dbHelper.getUserById(userId)

        if (user != null) {
            emailTextView.text = user.email
            loginTextView.text = user.login
            phoneTextView.text = user.phone

            if (user.isAdmin) {
                itemsList.visibility = View.GONE
                noItemsTextView.visibility = View.GONE
                sale.visibility = View.GONE
            } else {
                adminpanel.visibility = View.GONE
                val items = dbHelper.getItemsByUserId(userId)
                if (items.isEmpty()) {
                    noItemsTextView.visibility = View.VISIBLE
                    itemsList.visibility = View.GONE
                } else {
                    noItemsTextView.visibility = View.GONE
                    itemsList.visibility = View.VISIBLE
                    itemsList.layoutManager = LinearLayoutManager(this)
                    itemsList.adapter = ProfileItemsAdapter(items, this)
                }
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
        }

        onLogin.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        sale.setOnClickListener {
            val intent = Intent(this, SaleActivity::class.java)
            intent.putExtra("useremail", userEmail)
            startActivity(intent)
        }

        adminpanel.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            intent.putExtra("useremail", userEmail)
            startActivity(intent)
        }
    }
}