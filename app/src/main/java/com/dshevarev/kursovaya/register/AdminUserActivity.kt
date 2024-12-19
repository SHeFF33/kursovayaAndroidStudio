package com.dshevarev.kursovaya.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.products.AdminUserAdapter

class AdminUserActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: AdminUserAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user)

        recyclerView = findViewById(R.id.recyclerViewUsers)
        dbHelper = DBHelper(this, null)

        val users = dbHelper.getAllUsers()
        userAdapter = AdminUserAdapter(users, dbHelper)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}