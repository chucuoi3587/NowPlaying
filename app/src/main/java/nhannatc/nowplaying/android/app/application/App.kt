package nhannatc.nowplaying.android.app.application

import android.app.Application
import com.raywenderlich.android.droidwiki.dagger.AppModule
import nhannatc.nowplaying.android.app.di.component.ApplicationComponent
//import nhannatc.nowplaying.android.app.di.component.DaggerApplicationComponent

class App: Application() {

    private lateinit var applicationComponent : ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initApplicationComponent()
    }

    private fun initApplicationComponent() {
//        applicationComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
//        applicationComponent.inject(this)
    }

    fun getApplicationComponent() : ApplicationComponent {
        return applicationComponent
    }
}