package technical.test.yprsty

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import technical.test.yprsty.di.dataModule
import technical.test.yprsty.di.presentationModule

class ToDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ToDoApplication)
            loadKoinModules(listOf(dataModule, presentationModule))
        }
    }
}