package nhannatc.nowplaying.android.app.network

import android.app.Activity
import android.content.Context
import nhannatc.nowplaying.android.app.Constant
import nhannatc.nowplaying.android.app.util.NetworkUtil
import org.apache.http.NameValuePair
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLEncoder

abstract class DataLoader(context : Context) {

    protected lateinit var mContext: Context
    protected lateinit var api: String
    var READ_TIMEOUT = 0
    var CONNECT_TIMEOUT: Int = 0

    val TEN_MINUTES = 1000 * 60 * 10
    internal var strMess = ""
    var isUploadFile = false

    open interface DataLoaderInterface {
        fun loadDataDone(requestIndex: Int, resultCode: Int, result: Any)

        fun loadDataFail(requestIndex: Int, resultCode: Int, result: Any)
    }

    private var mClient: JsonClient? = null

    init {
        mContext = context
        api = Constant.APP_API
        READ_TIMEOUT = Constant.HTTP_READ_TIMEOUT
        CONNECT_TIMEOUT = Constant.HTTP_CONNECTION_TIMEOUT
    }

    protected fun checkSessionTokenAndBuildRequest() {
        if (NetworkUtil.isNetworkAvailable(mContext).not()) {
            //			((BaseActivity) mContext).hideLoading();
            //			((BaseActivity) mContext).displayErrorNetworkDialog();
            processResultsFail(500, -1, JSONObject())
        } else {
            startExecuteAfterAuthenticate()
        }
    }

    /*
	 * Subclass must implement this method to build url with its params
	 */
    protected abstract fun startExecuteAfterAuthenticate()

    /*
	 * Subclass must implement this method to receive results
	 */
    protected abstract fun processResultsDone(
        requestIndex: Int,
        resultCode: Int, jsonObject: JSONObject
    )

    /*
	 * Subclass must implement this method to receive fail results
	 */
    protected abstract fun processResultsFail(
        requestIndex: Int, failCode: Int,
        errorObject: JSONObject
    )

    fun execute(
        method: String, requestIndex: Int,
        requestProperty: List<NameValuePair>, content: String, url: URL) {

        mClient = JsonClient(
            mContext, url, method,
            requestProperty, content, READ_TIMEOUT, CONNECT_TIMEOUT)
        mClient!!.setDataResponseCallback(requestIndex, object : JsonClient.DataResponse {
            override fun ResultData(requestIndex: Int) {
                if (mContext is Activity) {
                    (mContext as Activity).runOnUiThread { setObjectDueToRequest(requestIndex, mClient) }
                } else {
                    Thread(Runnable {
                        setObjectDueToRequest(requestIndex, mClient)
                    }).start()
                }
            }

        })
        mClient!!.mIsUploadFile = isUploadFile
        mClient!!.connect()
    }

    fun setObjectDueToRequest(requestIndex : Int, jsonClient : JsonClient?) {
        if (jsonClient != null && jsonClient.responseCode === Constant.HTTP_STATUS_OK) {
            jsonClient.mJson?.let { processResultsDone(requestIndex, jsonClient.responseCode, it) }
        } else if (jsonClient != null) {
            if (jsonClient.responseCode == Constant.REQUEST_BAD_GATEWAY) {
                jsonClient.mJson?.let { processResultsFail(Constant.REQUEST_SERVER_DIE, jsonClient.responseCode, it) }
            }
        } else {
            processResultsFail(requestIndex, Constant.REQUEST_NETWORK_TIMEOUT, JSONObject())
        }
    }

    protected fun convertListParamsToURLString(params: List<NameValuePair>): String {
        // Prepare body
        val body = StringBuilder()
        var item: NameValuePair?
        if (params.size > 0) {
            try {
                var i = 0
                i = 0
                while (i < params.size) {
                    item = params[i]
                    body.append(
                        item.name + "=" + URLEncoder.encode(if (item.value == null) ""
                            else item.value, "UTF-8") + "&")
                    item = null
                    i++
                }
                item = null
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        return body.toString()

    }
}