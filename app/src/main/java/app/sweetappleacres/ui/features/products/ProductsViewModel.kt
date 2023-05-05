package app.sweetappleacres.ui.features.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.sweetappleacres.data.api.Resource
import app.sweetappleacres.data.response.ProductsListResponse
import app.sweetappleacres.domain.model.ProductDomain
import app.sweetappleacres.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.onStart

class ProductsViewModel(private val repository: ProductsRepository) : ViewModel() {

    fun loadAllProducts(): LiveData<Resource<ProductsListResponse>> = liveData {
        repository.persistProducts().onStart { emit(Resource.Loading()) }.collect {
            emit(it)
        }
    }

    fun getCachedProducts(): LiveData<Resource<List<ProductDomain>>> = liveData {
        repository.getAllProducts().collect {
            emit(it)
        }
    }

    fun getCachedProductDetail(productId: String): LiveData<Resource<ProductDomain>> = liveData {
        repository.getProductsByIdLocal(productId).collect {
            emit(it)
        }
    }

    fun browseProduct(search: String): LiveData<Resource<ProductsListResponse>> = liveData {
        repository.browseProducts(search).collect {
            emit(it)
        }
    }
}