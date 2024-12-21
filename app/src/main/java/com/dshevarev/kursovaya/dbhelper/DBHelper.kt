package com.dshevarev.kursovaya.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dshevarev.kursovaya.models.Item
import com.dshevarev.kursovaya.models.Sale
import com.dshevarev.kursovaya.models.User
import com.dshevarev.kursovaya.utils.CryptoUtils
import java.util.Date
import javax.crypto.SecretKey

class DBHelper(val context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 15) {

    private val secretKey: SecretKey = CryptoUtils.getKey(context)

    override fun onCreate(db: SQLiteDatabase?) {
        val queryUsers = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, login TEXT, phone TEXT, pass TEXT, is_admin INTEGER)"
        val queryItems = "CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, brand TEXT, model TEXT, description TEXT, text TEXT, price INTEGER, magprice INTEGER, status INTEGER, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))"
        val querySales = "CREATE TABLE sales (id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER, brand TEXT, model TEXT, price INTEGER, magprice INTEGER, saleuser TEXT, user TEXT, sale_date TEXT, FOREIGN KEY(product_id) REFERENCES items(id))"
        db!!.execSQL(queryUsers)
        db.execSQL(queryItems)
        db.execSQL(querySales)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS items")
        db.execSQL("DROP TABLE IF EXISTS sales")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("email", user.email)
        values.put("login", user.login)
        values.put("phone", user.phone)
        values.put("pass", CryptoUtils.encrypt(user.pass, secretKey))
        values.put("is_admin", if (user.isAdmin) 1 else 0)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    fun getUser(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val encryptedPass = CryptoUtils.encrypt(pass, secretKey)
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$email' AND pass = '$encryptedPass'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return exists
    }

    fun getUserById(userId: Int): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = $userId", null)
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                pass = CryptoUtils.decrypt(cursor.getString(cursor.getColumnIndexOrThrow("pass")), secretKey),
                isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun getUserIdByEmail(email: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE email = '$email'", null)
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun getUserIdByProductId(productId: Int): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT user_id FROM items WHERE id = $productId", null)
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun isEmailUnique(email: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$email'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return !exists
    }

    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    pass = CryptoUtils.decrypt(cursor.getString(cursor.getColumnIndexOrThrow("pass")), secretKey),
                    isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1
                )
                users.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return users
    }

    fun updateUserRole(userId: Int, isAdmin: Boolean) {
        val values = ContentValues()
        values.put("is_admin", if (isAdmin) 1 else 0)

        val db = this.writableDatabase
        db.update("users", values, "id = ?", arrayOf(userId.toString()))
        db.close()
    }

    fun addItem(item: Item, userId: Int) {
        val values = ContentValues()
        values.put("image", item.image)
        values.put("brand", item.brand)
        values.put("model", item.model)
        values.put("description", item.description)
        values.put("text", item.text)
        values.put("price", item.price)
        values.put("magprice", item.magprice)
        values.put("status", if (item.status) 1 else 0)
        values.put("user_id", userId)

        val db = this.writableDatabase
        db.insert("items", null, values)
        db.close()
    }

    fun getItems(): List<Item> {
        val items = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items WHERE status = 1", null)

        if (cursor.moveToFirst()) {
            do {
                val item = Item(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    brand = cursor.getString(cursor.getColumnIndexOrThrow("brand")),
                    model = cursor.getString(cursor.getColumnIndexOrThrow("model")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    text = cursor.getString(cursor.getColumnIndexOrThrow("text")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    magprice = cursor.getInt(cursor.getColumnIndexOrThrow("magprice")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1
                )
                items.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return items
    }

    fun getItemsByUserId(userId: Int): List<Item> {
        val items = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items WHERE user_id = $userId", null)

        if (cursor.moveToFirst()) {
            do {
                val item = Item(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    brand = cursor.getString(cursor.getColumnIndexOrThrow("brand")),
                    model = cursor.getString(cursor.getColumnIndexOrThrow("model")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    text = cursor.getString(cursor.getColumnIndexOrThrow("text")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    magprice = cursor.getInt(cursor.getColumnIndexOrThrow("magprice")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1
                )
                items.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return items
    }

    fun updateItemStatus(itemId: Int, status: Boolean) {
        val values = ContentValues()
        values.put("status", if (status) 1 else 0)

        val db = this.writableDatabase
        db.update("items", values, "id = ?", arrayOf(itemId.toString()))
        db.close()
    }

    fun addSale(sale: Sale) {
        val values = ContentValues()
        values.put("product_id", sale.productId)
        values.put("brand", sale.brand)
        values.put("model", sale.model)
        values.put("price", sale.price)
        values.put("magprice", sale.magprice)
        values.put("saleuser", sale.saleUser)
        values.put("user", sale.user)
        values.put("sale_date", sale.saleDate.toString())

        val db = this.writableDatabase
        db.insert("sales", null, values)
        db.close()
    }

    fun getSales(): List<Sale> {
        val sales = mutableListOf<Sale>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM sales", null)

        if (cursor.moveToFirst()) {
            do {
                val sale = Sale(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                    brand = cursor.getString(cursor.getColumnIndexOrThrow("brand")),
                    model = cursor.getString(cursor.getColumnIndexOrThrow("model")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    magprice = cursor.getInt(cursor.getColumnIndexOrThrow("magprice")),
                    saleUser = cursor.getString(cursor.getColumnIndexOrThrow("saleuser")),
                    user = cursor.getString(cursor.getColumnIndexOrThrow("user")),
                    saleDate = Date(cursor.getString(cursor.getColumnIndexOrThrow("sale_date")))
                )
                sales.add(sale)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return sales
    }
    fun getSalesByUserId(userId: Int): List<Sale> {
        val sales = mutableListOf<Sale>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM sales WHERE saleuser = (SELECT email FROM users WHERE id = $userId)", null)

        if (cursor.moveToFirst()) {
            do {
                val sale = Sale(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                    brand = cursor.getString(cursor.getColumnIndexOrThrow("brand")),
                    model = cursor.getString(cursor.getColumnIndexOrThrow("model")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    magprice = cursor.getInt(cursor.getColumnIndexOrThrow("magprice")),
                    saleUser = cursor.getString(cursor.getColumnIndexOrThrow("saleuser")),
                    user = cursor.getString(cursor.getColumnIndexOrThrow("user")),
                    saleDate = Date(cursor.getString(cursor.getColumnIndexOrThrow("sale_date")))
                )
                sales.add(sale)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return sales
    }

    fun updateItem(item: Item) {
        val values = ContentValues()
        values.put("image", item.image)
        values.put("brand", item.brand)
        values.put("model", item.model)
        values.put("description", item.description)
        values.put("text", item.text)
        values.put("price", item.price)
        values.put("magprice", item.magprice)
        values.put("status", if (item.status) 1 else 0)

        val db = this.writableDatabase
        db.update("items", values, "id = ?", arrayOf(item.id.toString()))
        db.close()
    }

    fun getItemById(itemId: Int): Item? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items WHERE id = $itemId", null)
        var item: Item? = null
        if (cursor.moveToFirst()) {
            item = Item(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                brand = cursor.getString(cursor.getColumnIndexOrThrow("brand")),
                model = cursor.getString(cursor.getColumnIndexOrThrow("model")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                text = cursor.getString(cursor.getColumnIndexOrThrow("text")),
                price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                magprice = cursor.getInt(cursor.getColumnIndexOrThrow("magprice")),
                status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1
            )
        }
        cursor.close()
        db.close()
        return item
    }
    fun getAllItems(): List<Item> {
        val items = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items", null)

        if (cursor.moveToFirst()) {
            do {
                val item = Item(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    brand = cursor.getString(cursor.getColumnIndexOrThrow("brand")),
                    model = cursor.getString(cursor.getColumnIndexOrThrow("model")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    text = cursor.getString(cursor.getColumnIndexOrThrow("text")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    magprice = cursor.getInt(cursor.getColumnIndexOrThrow("magprice")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1
                )
                items.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return items
    }

    fun deleteItem(itemId: Int) {
        val db = this.writableDatabase
        db.delete("items", "id = ?", arrayOf(itemId.toString()))
        db.close()
    }
}