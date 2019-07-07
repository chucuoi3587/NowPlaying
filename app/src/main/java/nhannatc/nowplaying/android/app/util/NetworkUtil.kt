package nhannatc.nowplaying.android.app.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtil {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            // Get the connectivity manager from service
            val connMgr = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // Get the network info
            val info = connMgr.activeNetworkInfo
            return info != null && info.isConnected
        }
    }
}