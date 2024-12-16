package com.dshevarev.kursovaya.utils

import android.content.Context
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object CryptoUtils {
    private const val ALGORITHM = "AES"
    private const val KEY_SIZE = 128
    private const val KEY_PREF = "crypto_key"

    fun generateKey(context: Context): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
        keyGenerator.init(KEY_SIZE)
        val key = keyGenerator.generateKey()
        saveKey(context, key)
        return key
    }

    fun getKey(context: Context): SecretKey {
        val sharedPreferences = context.getSharedPreferences("crypto_prefs", Context.MODE_PRIVATE)
        val encodedKey = sharedPreferences.getString(KEY_PREF, null)
        return if (encodedKey != null) {
            val decodedKey = Base64.decode(encodedKey, Base64.DEFAULT)
            SecretKeySpec(decodedKey, 0, decodedKey.size, ALGORITHM)
        } else {
            generateKey(context)
        }
    }

    private fun saveKey(context: Context, key: SecretKey) {
        val encodedKey = Base64.encodeToString(key.encoded, Base64.DEFAULT)
        val sharedPreferences = context.getSharedPreferences("crypto_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PREF, encodedKey)
        editor.apply()
    }

    fun encrypt(text: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(text.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decodedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }
}
