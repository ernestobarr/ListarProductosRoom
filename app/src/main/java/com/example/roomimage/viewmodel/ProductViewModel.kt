package com.example.roomimage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.db.model.entity.Product
import com.example.db.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val allProducts: Flow<List<Product>> = repository.allProducts

    fun insert(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(product)
    }

    class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object{
        private const val TAG="ProductViewModel"
    }
}