package app.sweetappleacres.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


const val PRODUCT_LIST_TABLE = "PRODUCT_LIST_TABLE"

@Entity(tableName = PRODUCT_LIST_TABLE)
data class ProductListEntity(
    @ColumnInfo(name = "description")
    val description: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "isAvailable")
    val isAvailable: Boolean,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "rating")
    val rating: Double,
)