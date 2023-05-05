package app.sweetappleacres.domain.source

import app.sweetappleacres.data.Prefs
import app.sweetappleacres.data.database.AppDao
import app.sweetappleacres.data.database.entities.ProductListEntity
import app.sweetappleacres.data.source.products.ProductsLocalDataSource

class ProductLocalDataSourceImpl(
    private val prefs: Prefs,
    private val dao: AppDao,
) : ProductsLocalDataSource {
    override suspend fun getAllProducts(): List<ProductListEntity> {
        return dao.getAllProducts()
    }

    override suspend fun getProductsById(productId: String): ProductListEntity {
        return dao.getProductById(queryId = productId)
    }
}