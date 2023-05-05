package app.sweetappleacres.data.response


import com.google.gson.annotations.SerializedName

class ProductsListResponse : ArrayList<ProductsListResponseItem>()

data class ProductsListResponseItem(
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String? = "",
    @SerializedName("isAvailable")
    val isAvailable: Boolean,
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("price")
    val price: Double,
    @SerializedName("rating")
    val rating: Double,
)