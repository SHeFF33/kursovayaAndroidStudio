package com.dshevarev.kursovaya.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.products.AdminSaleAdapter

class AdminSaleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var saleAdapter: AdminSaleAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sale)

        recyclerView = findViewById(R.id.recyclerViewSales)
        dbHelper = DBHelper(this, null)

        val sales = dbHelper.getSales()
        saleAdapter = AdminSaleAdapter(sales)
        recyclerView.adapter = saleAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}