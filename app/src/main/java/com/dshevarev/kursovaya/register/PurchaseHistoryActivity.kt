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
import com.dshevarev.kursovaya.products.PurchaseHistoryAdapter

class PurchaseHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var purchaseHistoryAdapter: PurchaseHistoryAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)

        recyclerView = findViewById(R.id.recyclerViewPurchaseHistory)
        dbHelper = DBHelper(this, null)

        val userEmail = intent.getStringExtra("useremail")
        if (userEmail != null) {
            val userId = dbHelper.getUserIdByEmail(userEmail)
            val sales = dbHelper.getSalesByUserId(userId)
            purchaseHistoryAdapter = PurchaseHistoryAdapter(sales)
            recyclerView.adapter = purchaseHistoryAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
}