package com.dshevarev.kursovaya.models

import java.util.Date

data class Sale(
    val id: Int,
    val productId: Int,
    val brand: String,
    val model: String,
    val price: Int,
    val magprice: Int,
    val saleUser: String,
    val user: String,
    val saleDate: Date
)