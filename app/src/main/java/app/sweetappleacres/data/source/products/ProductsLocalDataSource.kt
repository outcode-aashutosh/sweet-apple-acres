package app.sweetappleacres.data.source.products

import app.sweetappleacres.data.database.entities.ProductListEntity

interface ProductsLocalDataSource {
    suspend fun getAllProducts(): List<ProductListEntity>
    suspend fun getProductsById(productId: String): ProductListEntity
}