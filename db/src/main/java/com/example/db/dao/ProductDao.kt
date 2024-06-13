package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.model.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM product")
    fun getAllFlow(): Flow<List<Product>>

    @Insert
    fun insertAll(vararg products: Product)
}