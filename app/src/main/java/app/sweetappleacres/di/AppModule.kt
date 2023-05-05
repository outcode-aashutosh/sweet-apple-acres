package app.sweetappleacres.di

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import app.sweetappleacres.data.source.products.ProductsLocalDataSource
import app.sweetappleacres.data.source.products.ProductsRemoteDataSource
import app.sweetappleacres.domain.repository.ProductsRepository
import app.sweetappleacres.domain.repository.ProductsRepositoryImpl
import app.sweetappleacres.domain.source.ProductLocalDataSourceImpl
import app.sweetappleacres.domain.source.ProductRemoteDataSourceImpl
import app.sweetappleacres.ui.features.products.ProductsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { CoroutineScope(Dispatchers.Main + Job()) }
    single { Dispatchers.Default }
    single { provideResources(get()) }
    single { provideAssetManager(get()) }
    single { provideContentResolver(get()) }
}

val dataSourceModule = module {
    single<ProductsRemoteDataSource> { ProductRemoteDataSourceImpl(get(), get(), get()) }
    single<ProductsLocalDataSource> { ProductLocalDataSourceImpl(get(), get()) }
}

val repositoryModule = module {
    single<ProductsRepository> { ProductsRepositoryImpl(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { ProductsViewModel(get()) }
}

fun provideResources(context: Context): Resources = context.resources

fun provideAssetManager(resources: Resources): AssetManager = resources.assets

fun provideContentResolver(context: Context): ContentResolver = context.contentResolver