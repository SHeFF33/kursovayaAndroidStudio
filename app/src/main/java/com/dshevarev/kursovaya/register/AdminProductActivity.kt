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
import com.dshevarev.kursovaya.products.AdminProductAdapter

class AdminProductActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: AdminProductAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_product)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        dbHelper = DBHelper(this, null)

        val items = dbHelper.getAllItems()
        productAdapter = AdminProductAdapter(items, this)
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        val items = dbHelper.getAllItems()
        productAdapter.updateItems(items)
    }
}