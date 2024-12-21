package com.dshevarev.kursovaya.products

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.models.Item
import com.dshevarev.kursovaya.utils.SharedPreferencesHelper
import com.dshevarev.kursovaya.register.ProfActivity

class ItemsActivity : AppCompatActivity() {
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var itemsList: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        itemsList = findViewById(R.id.itemsList)
        val profile: ImageView = findViewById(R.id.item_list_profile)
        searchView = findViewById(R.id.searchView)

        val userEmail = intent.getStringExtra("useremail") ?: SharedPreferencesHelper.getUserEmail(this)
        if (userEmail != null) {
            SharedPreferencesHelper.saveUserEmail(this, userEmail)
        }

        val dbHelper = DBHelper(this, null)


        if (dbHelper.getItems().isEmpty()) {
            dbHelper.addItem(Item(1, "iphone6s", "Apple","iPhone 6s", "64gb", "Fantastish dastich good!", 3000,5000, true), 1)
            dbHelper.addItem(Item(2, "iphonex", "Apple","iPhone X","128gb", "Fantastish dastich good!",10000,12000, true), 1)
            dbHelper.addItem(Item(3, "iphone11", "Apple","iPhone 11" ,"256gb", "Fantastish dastich good!",6000,10000, true), 1)
            dbHelper.addItem(Item(4, "samsungs20", "Samsung","galaxy s20", "256gb", "Fantastish dastich good!",14000,19000, true), 1)
            dbHelper.addItem(Item(5, "samsungs21", "Samsung","galaxy s21", "256gb", "Fantastish dastich good!",18000,23000, true), 1)
            dbHelper.addItem(Item(6, "redmi12", "Redmi","12", "128gb", "Fantastish dastich good!",9000,14900, true), 1)
        }
        val items = dbHelper.getItems()
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsAdapter = ItemsAdapter(items, this)
        itemsList.adapter = itemsAdapter

        profile.setOnClickListener {
            val intent = Intent(this, ProfActivity::class.java)
            intent.putExtra("useremail", userEmail)
            startActivity(intent)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterItems(newText)
                return true
            }
        })
    }

    private fun filterItems(query: String?) {
        val dbHelper = DBHelper(this, null)
        val filteredItems = if (query.isNullOrEmpty()) {
            dbHelper.getItems()
        } else {
            dbHelper.getItems().filter { item ->
                item.brand.contains(query, ignoreCase = true) ||
                        item.model.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true)
            }
        }
        itemsAdapter.updateItems(filteredItems)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val dbHelper = DBHelper(this, null)
            val items = dbHelper.getItems()
            itemsAdapter.updateItems(items)
        }
    }
}
