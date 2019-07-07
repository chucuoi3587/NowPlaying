package nhannatc.nowplaying.android.app.di.component

import dagger.Component
import nhannatc.nowplaying.android.app.MainActivity
import nhannatc.nowplaying.android.app.di.module.MainActivityModule
import javax.inject.Singleton

@Singleton
@Component(modules = [MainActivityModule::class])
interface ApplicationComponent {
    fun inject(target : MainActivity)
}