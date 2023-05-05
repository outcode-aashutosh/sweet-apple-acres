package app.sweetappleacres.domain.repository

import app.sweetappleacres.data.api.Resource
import app.sweetappleacres.data.database.entities.ProductListEntity
import app.sweetappleacres.data.handler.doTryCatch
import app.sweetappleacres.data.response.ProductDetailResponse
import app.sweetappleacres.data.response.ProductsListResponse
import app.sweetappleacres.data.source.products.ProductsLocalDataSource
import app.sweetappleacres.data.source.products.ProductsRemoteDataSource
import app.sweetappleacres.domain.model.ProductDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface ProductsRepository {
    suspend fun persistProducts(): Flow<Resource<ProductsListResponse>>
    suspend fun browseProducts(search: String): Flow<Resource<ProductsListResponse>>
    suspend fun getAllProducts(): Flow<Resource<List<ProductDomain>>>
    suspend fun getProductsByIdLocal(productId: String): Flow<Resource<ProductDomain>>
    suspend fun getProductsByIdRemote(productId: String): Flow<Resource<ProductDetailResponse>>
}

class ProductsRepositoryImpl(
    private val productsLocalDataSource: ProductsLocalDataSource,
    private val productsRemoteDataSource: ProductsRemoteDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : ProductsRepository {

    override suspend fun persistProducts(): Flow<Resource<ProductsListResponse>> {
        return productsRemoteDataSource.getAllProducts()
    }

    override suspend fun browseProducts(search: String): Flow<Resource<ProductsListResponse>> {
        return productsRemoteDataSource.browseProducts(search)
    }

    override suspend fun getAllProducts(): Flow<Resource<List<ProductDomain>>> {
        return flow {
            emit(doTryCatch {
                Resource.Success(mapLocalDomain(productsLocalDataSource.getAllProducts()))
            })
        }.flowOn(defaultDispatcher)
    }

    private fun mapLocalDomain(allProducts: List<ProductListEntity>): List<ProductDomain> {
        return allProducts.map {
            ProductDomain(
                description = it.description,
                id = it.id,
                image = it.image,
                isAvailable = it.isAvailable,
                name = it.name,
                price = it.price,
                rating = it.rating,
            )
        }
    }

    override suspend fun getProductsByIdLocal(productId: String): Flow<Resource<ProductDomain>> {
        return flow {
            emit(doTryCatch {
                val item = productsLocalDataSource.getProductsById(productId)
                Resource.Success(
                    ProductDomain(
                        description = item.description,
                        id = item.id,
                        image = item.image,
                        isAvailable = item.isAvailable,
                        name = item.name,
                        price = item.price,
                        rating = item.rating
                    )
                )
            })
        }.flowOn(defaultDispatcher)
    }

    override suspend fun getProductsByIdRemote(productId: String): Flow<Resource<ProductDetailResponse>> {
        return productsRemoteDataSource.getProductsById(productId)
    }

}