package com.dshevarev.kursovaya.register

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.models.Item

class EditItemActivity : AppCompatActivity() {
    private lateinit var imageNameEditText: EditText
    private lateinit var brandEditText: EditText
    private lateinit var modelEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var textEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var magpriceEditText: EditText
    private lateinit var statusEditText: EditText
    private lateinit var updateItemButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        imageNameEditText = findViewById(R.id.edit_item_image_name)
        brandEditText = findViewById(R.id.edit_item_brand)
        modelEditText = findViewById(R.id.edit_item_model)
        descriptionEditText = findViewById(R.id.edit_item_description)
        textEditText = findViewById(R.id.edit_item_text)
        priceEditText = findViewById(R.id.edit_item_price)
        magpriceEditText = findViewById(R.id.edit_item_magprice)
        statusEditText = findViewById(R.id.edit_item_status)
        updateItemButton = findViewById(R.id.edit_item_update_button)

        val itemId = intent.getIntExtra("itemId", -1)
        if (itemId == -1) {
            Toast.makeText(this, "Телефон не был найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = DBHelper(this, null)
        val item = dbHelper.getItemById(itemId)

        if (item != null) {
            imageNameEditText.setText(item.image)
            brandEditText.setText(item.brand)
            modelEditText.setText(item.model)
            descriptionEditText.setText(item.description)
            textEditText.setText(item.text)
            priceEditText.setText(item.price.toString())
            magpriceEditText.setText(item.magprice.toString())
            statusEditText.setText(if (item.status) "В продаже" else "Продан")
        }

        updateItemButton.setOnClickListener {
            updateItem(itemId)
        }
    }

    private fun updateItem(itemId: Int) {
        val imageName = imageNameEditText.text.toString()
        val brand = brandEditText.text.toString()
        val model = modelEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val text = textEditText.text.toString()
        val price = priceEditText.text.toString().toIntOrNull()
        val magprice = magpriceEditText.text.toString().toIntOrNull()
        val status = statusEditText.text.toString() == "В продаже"

        if (imageName.isBlank() || brand.isBlank() || model.isBlank() || description.isBlank() || text.isBlank() || price == null || magprice == null) {
            Toast.makeText(this, "Пожалуйста заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val image = if (getDrawableResourceId(imageName) != 0) {
            imageName
        } else {
            Toast.makeText(this, "Ваше фото не было найдено. Применено стандартное фото", Toast.LENGTH_SHORT).show()
            "iphone"
        }

        val dbHelper = DBHelper(this, null)
        val item = Item(
            id = itemId,
            image = image,
            brand = brand,
            model = model,
            description = description,
            text = text,
            price = price,
            magprice = magprice,
            status = status
        )

        dbHelper.updateItem(item)
        Toast.makeText(this, "Телефон изменен!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getDrawableResourceId(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }
}