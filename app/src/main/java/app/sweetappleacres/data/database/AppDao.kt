package app.sweetappleacres.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.sweetappleacres.data.database.entities.ProductListEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(productListEntity: ProductListEntity)

    @Query("SELECT * FROM product_list_table")
    suspend fun getAllProducts(): List<ProductListEntity>

    @Query("SELECT * FROM product_list_table WHERE id = :queryId")
    suspend fun getProductById(queryId: String): ProductListEntity

    @Query("DELETE FROM product_list_table")
    suspend fun deleteAllCachedProducts()
}