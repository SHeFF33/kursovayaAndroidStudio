package com.dshevarev.kursovaya.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dshevarev.kursovaya.R

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_admin)
        val salepanel: Button = findViewById(R.id.admin_sales)
        val productpanel: Button = findViewById(R.id.admin_product)
        val userpanel: Button = findViewById(R.id.admin_users)


        salepanel.setOnClickListener {
            val intent = Intent(this, AdminSaleActivity::class.java)
            startActivity(intent)
        }
        productpanel.setOnClickListener {
            val intent = Intent(this, AdminProductActivity::class.java)
            startActivity(intent)
        }
        userpanel.setOnClickListener {
            val intent = Intent(this, AdminUserActivity::class.java)
            startActivity(intent)
        }
        }
    }