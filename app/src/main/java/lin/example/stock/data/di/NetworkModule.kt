package lin.example.stock.data.di

import com.google.gson.GsonBuilder
import lin.example.stock.data.remote.api.TaifexApiService
import lin.example.stock.data.repository.TaifexRepositoryImpl
import lin.example.stock.domain.repository.TaifexRepository
import lin.example.stock.domain.usecase.FetchAllStockInfoUseCase
import lin.example.stock.presentation.ui.viewmodel.TaifexViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val gson = GsonBuilder()
    .setLenient()
    .create()

val networkModule = module {
    single {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://openapi.twse.com.tw/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TaifexApiService::class.java)
    }
    single<TaifexRepository> { TaifexRepositoryImpl(get()) }
    single { FetchAllStockInfoUseCase(get()) }
    viewModel { TaifexViewModel(get()) }
}