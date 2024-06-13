package com.example.db.repository

import androidx.annotation.WorkerThread
import com.example.db.dao.ProductDao
import com.example.db.model.entity.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAllFlow()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(product: Product) {
        productDao.insertAll(product)
    }
}