package com.example.roomimage.app

import android.app.Application
import com.example.db.datasource.AppDatabase
import com.example.db.repository.ProductRepository

class ProductApplication: Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }
}