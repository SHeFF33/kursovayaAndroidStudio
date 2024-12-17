package com.dshevarev.kursovaya.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.models.Item
import com.dshevarev.kursovaya.products.ItemsActivity
import com.dshevarev.kursovaya.utils.SharedPreferencesHelper

class SaleActivity : AppCompatActivity() {

    private lateinit var imageNameEditText: EditText
    private lateinit var brandEditText: EditText
    private lateinit var modelEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var textEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addItemButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        imageNameEditText = findViewById(R.id.sale_item_image_name)
        brandEditText = findViewById(R.id.sale_item_brand)
        modelEditText = findViewById(R.id.sale_item_model)
        descriptionEditText = findViewById(R.id.sale_item_description)
        textEditText = findViewById(R.id.sale_item_text)
        priceEditText = findViewById(R.id.sale_item_price)
        addItemButton = findViewById(R.id.sale_item_add_button)
         val back : ImageView = findViewById(R.id.sale_item_list_back)
        addItemButton.setOnClickListener {
            addItem()
        }

        back.setOnClickListener{
            val intent = Intent(this, ProfActivity::class.java)
            startActivity(intent)
        }
    }


    private fun addItem() {
        val imageName = imageNameEditText.text.toString()
        val brand = brandEditText.text.toString()
        val model = modelEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val text = textEditText.text.toString()
        val price = priceEditText.text.toString().toIntOrNull()

        if (imageName.isBlank() || brand.isBlank() || model.isBlank() || description.isBlank() || text.isBlank() || price == null) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val userEmail = SharedPreferencesHelper.getUserEmail(this)
        if (userEmail == null) {
            Toast.makeText(this, "Непредвиденная ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DBHelper(this, null)
        val userId = dbHelper.getUserIdByEmail(userEmail)
        if (userId == -1) {
            Toast.makeText(this, "Непредвиденная ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
            return
        }

        val magprice = Math.round(price * 1.3f).toInt()
        val status = true

        val image = if (getDrawableResourceId(imageName) != 0) {
            imageName
        } else {
            Toast.makeText(this, "Ваше фото не было найдено. Применено стандартное фото", Toast.LENGTH_SHORT).show()
            "iphone"
        }

        val item = Item(
            id = 0,
            image = image,
            brand = brand,
            model = model,
            description = description,
            text = text,
            price = price,
            magprice = magprice,
            status = status
        )

        dbHelper.addItem(item, userId)
        Toast.makeText(this, "Телефон продан!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getDrawableResourceId(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

}