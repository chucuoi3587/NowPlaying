package nhannatc.nowplaying.android.app.network

import android.content.Context
import android.os.Environment
import nhannatc.nowplaying.android.app.Constant
import nhannatc.nowplaying.android.app.util.CommonUtil
import okhttp3.*
import org.apache.http.NameValuePair
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

class JsonClient(context: Context, source: URL, requestMethod: String,
                 requestProperties: List<NameValuePair>, bodyContent: String, readTimeout: Int, connectTimeout: Int) {
    private var mSource: URL? = null
    private lateinit var mContext: Context
    var mJson: JSONObject? = null
        get() = field
    private lateinit var mRequestMethod: String
    private lateinit var mBodyContent: String
    var responseCode: Int = 0
        get() = field
    private var mListOfRequestProperty: List<NameValuePair>? = null
    private var mReadTimeout: Int = 0
    private var mConnectTimeout: Int = 0
    var mIsUploadFile:Boolean = false
        set(value) {field = value}
        get() = field
    interface DataResponse {
        fun ResultData(requestIndex: Int)
    }

    private var mRequestIndex: Int = 0
    internal val JSON = MediaType.parse("base64; charset=utf-8")
    fun setDataResponseCallback(requestIndex: Int, callback: DataResponse) {
        this.mRequestIndex = requestIndex
        this.mDataResponse = callback
    }

    private var mDataResponse: DataResponse? = null
    init {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            CommonUtil.cacheDir = File(Environment.getExternalStorageDirectory(), context.packageName)
        else
            CommonUtil.cacheDir = context.cacheDir
        if (CommonUtil.cacheDir!!.exists().not())
            CommonUtil.cacheDir!!.mkdirs()
        mContext = context
        mSource = source
        mRequestMethod = requestMethod
        mBodyContent = bodyContent
        mListOfRequestProperty = requestProperties
        mReadTimeout = readTimeout
        mConnectTimeout = connectTimeout
    }

    fun connect() {
        mJson = null
        responseCode = -1
        val client = OkHttpClient()
            .newBuilder().retryOnConnectionFailure(false)
            .connectTimeout(mConnectTimeout.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(mReadTimeout.toLong(), TimeUnit.MILLISECONDS)
            .build()
//        CommonUtil.trustEveryone()

        val request = Request.Builder()
        request.url(mSource)
        if (mListOfRequestProperty != null) {
            var item: NameValuePair
            for (i in mListOfRequestProperty!!.indices) {
                item = mListOfRequestProperty!!.get(i)
                request.addHeader(item.name, item.value)
            }
        }
        if (isGotOutPut(mRequestMethod)) {
            if (mIsUploadFile) {
                val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                if (mBodyContent != "") {
                    try {
                        val json = JSONObject(mBodyContent)
                        val keys = json.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            val value = json.opt(key)
                            if (key.contains("upload_file")) {
                                val f = File(value as String)
                                builder.addFormDataPart(
                                    key,
                                    f.name,
                                    RequestBody.create(MediaType.parse("image/jpeg"), f)
                                )
                            } else {
                                builder.addFormDataPart(key, value as? String ?: value.toString())
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                val body = builder.build()
                request.post(body)
                request.method(mRequestMethod, body)
            } else {
                val body = RequestBody.create(JSON, mBodyContent)
                request.method(mRequestMethod, body)
                request.post(body)
            }
        }

        client.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                printToJson(responseCode, e.message)
                mDataResponse?.ResultData(mRequestIndex)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                responseCode = response.code()
                try {
                    val respStr = response.body()!!.string()
                    mJson = JSONObject(respStr)
                    mDataResponse?.ResultData(mRequestIndex)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun isGotOutPut(method: String)= if (method.equals(Constant.GET_REQUEST)) false else true

    private fun printToJson(code: Int, message: String?) {
        try {
            val res = mContext.resources
            mJson = JSONObject()
            mJson!!.put("resultCode", code)
            mJson!!.put("resultMessage", message)
            // Log.e("JsonClient", message);
        } catch (e: JSONException) {
            // Log.e("JsonClient.connect", message);
        }

    }

}