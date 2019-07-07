package nhannatc.nowplaying.android.app.webservices

import android.content.Context
import nhannatc.nowplaying.android.app.Constant
import nhannatc.nowplaying.android.app.network.DataLoader
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONObject
import java.net.URL
import java.util.ArrayList

class GetPopularMovieWs(context : Context) : DataLoader(context) {

    var mHandler : DataLoaderInterface? = null
    var page: Int = 1

    constructor(context: Context, handler : DataLoaderInterface) : this(context) {
        mHandler = handler
    }

    fun getMovies(page : Int) {
        this.page = page
        checkSessionTokenAndBuildRequest()
    }

    override fun startExecuteAfterAuthenticate() {
        val properties = ArrayList<NameValuePair>()
        properties.add(BasicNameValuePair("api_key", Constant.API_KEY))
        properties.add(BasicNameValuePair("language", "en"))
        properties.add(BasicNameValuePair("page", page.toString()))
        val query = StringBuilder(api + Constant.WS_API_GET_POPULAR_MOVIES)

        query.append("?")
        query.append(convertListParamsToURLString(properties))
        properties.add(BasicNameValuePair("Content-Type", "application/json"))

        val url = URL(query.toString())
        execute(Constant.POST_REQUEST, Constant.REQUEST_API_GET_POPULAR_MOVIES, properties, "", url)
    }

    override fun processResultsDone(requestIndex: Int, resultCode: Int, jsonObject: JSONObject) {
        if (mHandler != null) {
            if (jsonObject.optInt("status_code", -1) != -1) {
                mHandler!!.loadDataFail(requestIndex, resultCode, jsonObject)
            } else {
                mHandler!!.loadDataDone(requestIndex, resultCode, jsonObject)
            }
        } else if (DataLoaderInterface::class.java.isAssignableFrom(mContext.javaClass)) {
            if (jsonObject.optInt("status_code", -1) != -1) {
                (mContext as DataLoaderInterface).loadDataFail(requestIndex, resultCode, jsonObject)
            } else {
                (mContext as DataLoaderInterface).loadDataDone(requestIndex, resultCode, jsonObject)
            }
        }
    }

    override fun processResultsFail(requestIndex: Int, failCode: Int, errorObject: JSONObject) {
        if (mHandler != null) {
            mHandler!!.loadDataFail(requestIndex, failCode, errorObject)
        } else if (DataLoaderInterface::class.java.isAssignableFrom(mContext.javaClass)) {
            (mContext as DataLoaderInterface).loadDataFail(requestIndex, failCode, errorObject
            )
        }
    }

}