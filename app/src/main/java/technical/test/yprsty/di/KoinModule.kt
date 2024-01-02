package technical.test.yprsty.di

import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import technical.test.yprsty.data.ToDoRepository
import technical.test.yprsty.data.source.locale.ToDoDatabase
import technical.test.yprsty.data.source.remote.GeoApiService
import technical.test.yprsty.presentation.detail.DetailViewModel
import technical.test.yprsty.presentation.main.MainViewModel
import java.util.concurrent.TimeUnit

val dataModule = module {
    factory { get<ToDoDatabase>().activityDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            ToDoDatabase::class.java,
            "ToDo.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://get.geojs.io/v1/ip/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(GeoApiService::class.java)
    }

    single {
        ToDoRepository(get(), get())
    }
}

val presentationModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}