package com.dshevarev.kursovaya.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dshevarev.kursovaya.models.Item
import com.dshevarev.kursovaya.models.User
import com.dshevarev.kursovaya.utils.CryptoUtils
import javax.crypto.SecretKey

class DBHelper(val context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 6) {

    private val secretKey: SecretKey = CryptoUtils.getKey(context)

    override fun onCreate(db: SQLiteDatabase?) {
        val queryUsers = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, login TEXT, phone TEXT, pass TEXT)"
        val queryItems = "CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, brand TEXT, model TEXT, description TEXT, text TEXT, price INTEGER, magprice INTEGER, status INTEGER, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))"
        db!!.execSQL(queryUsers)
        db.execSQL(queryItems)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS items")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("email", user.email)
        values.put("login", user.login)
        values.put("phone", user.phone)
        values.put("pass", CryptoUtils.encrypt(user.pass, secretKey))

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
                pass = CryptoUtils.decrypt(cursor.getString(cursor.getColumnIndexOrThrow("pass")), secretKey)
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

    fun isEmailUnique(email: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$email'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return !exists
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
}
