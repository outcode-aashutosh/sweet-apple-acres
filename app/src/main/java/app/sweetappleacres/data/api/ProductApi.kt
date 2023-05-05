package app.sweetappleacres.data.api

import app.common.extension.BASE_PATH
import app.sweetappleacres.data.response.ProductDetailResponse
import app.sweetappleacres.data.response.ProductsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    companion object {
        private const val VIEW_PRODUCTS = "$BASE_PATH/products"
        private const val VIEW_PRODUCT_DETAIL = "$BASE_PATH/products/{id}"
    }

    @GET(VIEW_PRODUCTS)
    suspend fun viewProducts(): Response<ProductsListResponse>

    @GET(VIEW_PRODUCTS)
    suspend fun browseProducts(
        @Query("search") search: String,
    ): Response<ProductsListResponse>

    @GET(VIEW_PRODUCT_DETAIL)
    suspend fun viewProductDetail(
        @Path("id") id: String
    ): Response<ProductDetailResponse>

}