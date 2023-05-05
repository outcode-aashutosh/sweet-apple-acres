package app.sweetappleacres.domain.model

data class ProductDomain(
    val description: String,
    val id: String,
    val image: String,
    val isAvailable: Boolean,
    val name: String,
    val price: Double,
    val rating: Double,
)