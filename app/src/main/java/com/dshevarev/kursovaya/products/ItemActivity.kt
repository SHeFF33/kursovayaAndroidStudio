package com.dshevarev.kursovaya.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.models.Sale
import com.dshevarev.kursovaya.utils.SharedPreferencesHelper
import java.util.Date

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val brand: TextView = findViewById(R.id.item_list_brand_one)
        val model: TextView = findViewById(R.id.item_list_model_one)
        val magprice: TextView = findViewById(R.id.item_list_magprice_one)
        val text: TextView = findViewById(R.id.item_list_text)
        val image: ImageView = findViewById(R.id.item_list_image_one)
        val buy: Button = findViewById(R.id.button_buy)
        val back: ImageView = findViewById(R.id.item_list_back)

        val userEmail = SharedPreferencesHelper.getUserEmail(this)
        val dbHelper = DBHelper(this, null)
        val userId = dbHelper.getUserIdByEmail(userEmail!!)
        val user = dbHelper.getUserById(userId)

        if (user != null && user.isAdmin) {
            buy.visibility = View.GONE
        }

        val imageName = intent.getStringExtra("itemImage")
        if (imageName != null) {
            val imageId = resources.getIdentifier(imageName, "drawable", packageName)
            image.setImageResource(imageId)
        }
        brand.text = intent.getStringExtra("itemBrand")
        model.text = intent.getStringExtra("itemModel")
        text.text = intent.getStringExtra("itemText")
        magprice.text = intent.getIntExtra("itemMagprice", 0).toString()

        back.setOnClickListener {
            finish()
        }

        buy.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Подтверждение покупки")
            builder.setMessage("Вы действительно хотите купить этот товар?")
            builder.setPositiveButton("Да") { dialog, which ->
                val itemId = intent.getIntExtra("itemId", -1)
                val itemPrice = intent.getIntExtra("itemPrice", 0)
                val itemMagprice = intent.getIntExtra("itemMagprice", 0)

                if (itemId != -1) {
                    dbHelper.updateItemStatus(itemId, false)

                    val saleUserEmail = SharedPreferencesHelper.getUserEmail(this)
                    val saleUser = dbHelper.getUserById(dbHelper.getUserIdByEmail(saleUserEmail!!))

                    val sellerUserId = dbHelper.getUserIdByProductId(itemId)
                    val seller = dbHelper.getUserById(sellerUserId)

                    val sale = Sale(
                        id = 0,
                        productId = itemId,
                        brand = brand.text.toString(),
                        model = model.text.toString(),
                        price = itemPrice,
                        magprice = itemMagprice,
                        saleUser = saleUser!!.email,
                        user = seller!!.email,
                        saleDate = Date()
                    )

                    dbHelper.addSale(sale)

                    val returnIntent = Intent()
                    returnIntent.putExtra("itemId", itemId)
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
            }
            builder.setNegativeButton("Нет") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }
}