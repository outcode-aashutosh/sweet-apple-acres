package app.sweetappleacres.data.source.products

import app.sweetappleacres.data.api.Resource
import app.sweetappleacres.data.response.ProductDetailResponse
import app.sweetappleacres.data.response.ProductsListResponse
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {
    suspend fun getAllProducts(): Flow<Resource<ProductsListResponse>>
    suspend fun browseProducts(search: String): Flow<Resource<ProductsListResponse>>
    suspend fun getProductsById(productId: String): Flow<Resource<ProductDetailResponse>>
}