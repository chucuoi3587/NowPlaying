package nhannatc.nowplaying.android.app.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import nhannatc.nowplaying.android.app.Constant
import nhannatc.nowplaying.android.app.model.Movie
import nhannatc.nowplaying.android.app.network.DataLoader
import nhannatc.nowplaying.android.app.webservices.GetPopularMovieWs
import org.json.JSONObject
import javax.inject.Inject

class MovieListViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val model : MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    private var mContext : Context
    private var mGetPopularMoviesWs : GetPopularMovieWs
    private var mPage = 1
    private var mTotalPage = 0;
    var movies : ArrayList<Movie>? = ArrayList<Movie>()
    var isLock : MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        mContext = application.baseContext
        mGetPopularMoviesWs = GetPopularMovieWs(mContext, object : DataLoader.DataLoaderInterface {
            override fun loadDataDone(requestIndex: Int, resultCode: Int, result: Any) {
                Log.d("NhanNATC", "Result : " + (result as JSONObject).toString())
                when (requestIndex) {
                    Constant.REQUEST_API_GET_POPULAR_MOVIES -> {
                        if (mTotalPage == 0)
                            mTotalPage = (result as JSONObject).optInt("total_pages", 0)
                        var jarray = (result as JSONObject).optJSONArray("results")
                        if (jarray != null && jarray.length() > 0) {
                            var i = 0
                            for (i in 0 until jarray.length()) {
                                var movie = Movie(jarray.getJSONObject(i))
                                movies!!.add(movie)
                            }
                        }
                        isLock.postValue(false)
                        model.postValue(movies)
                    }
                }
            }

            override fun loadDataFail(requestIndex: Int, resultCode: Int, result: Any) {
                Log.d("NhanNATC", "result  failed")
            }
        })
        model.value = movies
        isLock.value = false
    }

    fun getMoviesLiveData() : MutableLiveData<ArrayList<Movie>> {
        return model
    }

    fun getData() : ArrayList<Movie>? {
        return model.value
    }

    fun getMovies() {
        mGetPopularMoviesWs.getMovies(mPage++)
        isLock.postValue(true)
    }

    fun loadMore() {
        if (mPage >= mTotalPage)
            return
        mGetPopularMoviesWs.getMovies(mPage++)
        isLock.postValue(true)
    }
}