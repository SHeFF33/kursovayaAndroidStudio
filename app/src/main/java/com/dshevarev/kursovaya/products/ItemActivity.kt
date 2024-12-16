package com.dshevarev.kursovaya.products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper

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

        val imageName = intent.getStringExtra("itemImage")
        if (imageName != null) {
            val imageId = resources.getIdentifier(imageName, "drawable", packageName)
            image.setImageResource(imageId)
        }
        brand.text = intent.getStringExtra("itemBrand")
        model.text = intent.getStringExtra("itemModel")
        text.text = intent.getStringExtra("itemText")
        magprice.text = intent.getStringExtra("itemMagprice")

        back.setOnClickListener {
            finish()
        }

        buy.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Подтверждение покупки")
            builder.setMessage("Вы действительно хотите купить этот товар?")
            builder.setPositiveButton("Да") { dialog, which ->
                val itemId = intent.getIntExtra("itemId", -1)
                if (itemId != -1) {
                    val dbHelper = DBHelper(this, null)
                    dbHelper.updateItemStatus(itemId, false)
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
