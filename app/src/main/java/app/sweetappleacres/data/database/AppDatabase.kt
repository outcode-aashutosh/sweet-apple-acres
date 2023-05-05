package app.sweetappleacres.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import app.sweetappleacres.data.database.entities.ProductListEntity

@Database(
    entities = [
        ProductListEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}