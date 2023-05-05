package app.sweetappleacres.domain.source

import app.sweetappleacres.data.Prefs
import app.sweetappleacres.data.api.ProductApi
import app.sweetappleacres.data.api.Resource
import app.sweetappleacres.data.database.AppDao
import app.sweetappleacres.data.database.entities.ProductListEntity
import app.sweetappleacres.data.handler.doTryCatch
import app.sweetappleacres.data.handler.handleResponse
import app.sweetappleacres.data.response.ProductDetailResponse
import app.sweetappleacres.data.response.ProductsListResponse
import app.sweetappleacres.data.source.products.ProductsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRemoteDataSourceImpl(
    val api: ProductApi,
    private val dao: AppDao,
    private val defaultDispatcher: CoroutineDispatcher,
) : ProductsRemoteDataSource {
    override suspend fun getAllProducts(): Flow<Resource<ProductsListResponse>> {
        return flow {
            emit(doTryCatch {
                api.viewProducts().handleResponse(doActionOnSuccess = { response ->
                    val productList = response.map {
                        ProductListEntity(
                            description = it.description.orEmpty(),
                            id = it.id,
                            image = it.image.orEmpty(),
                            isAvailable = it.isAvailable,
                            name = it.name.orEmpty(),
                            price = it.price,
                            rating = it.rating
                        )
                    }
                    productList.forEach { mappedEntity ->
                        dao.save(mappedEntity)
                    }
                })
            })
        }.flowOn(defaultDispatcher)
    }

    override suspend fun browseProducts(search: String): Flow<Resource<ProductsListResponse>> {
        return flow<Resource<ProductsListResponse>> {
            emit(doTryCatch {
                api.browseProducts(search).handleResponse()
            })
        }.flowOn(defaultDispatcher)
    }

    override suspend fun getProductsById(productId: String): Flow<Resource<ProductDetailResponse>> {
        return flow {
            emit(doTryCatch {
                api.viewProductDetail(productId).handleResponse()
            })
        }
    }
}