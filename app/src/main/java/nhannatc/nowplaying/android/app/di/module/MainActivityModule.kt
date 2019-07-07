package nhannatc.nowplaying.android.app.di.module

import android.arch.lifecycle.AndroidViewModel
import dagger.Module
import dagger.Provides
import nhannatc.nowplaying.android.app.MainActivity
import nhannatc.nowplaying.android.app.application.App
import nhannatc.nowplaying.android.app.viewmodel.MovieListViewModel
import javax.inject.Scope
import javax.inject.Singleton

@Module
class MainActivityModule() {

    @Provides
    @Singleton
    fun provideMovieListViewModel(mainActivity : MainActivity) : MovieListViewModel {
        val app = mainActivity.application as App
        return MovieListViewModel(app)
    }
}