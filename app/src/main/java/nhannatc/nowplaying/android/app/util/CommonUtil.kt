package nhannatc.nowplaying.android.app.util

import android.content.Context
import java.io.File
import android.util.TypedValue



class CommonUtil {

    companion object {
        var cacheDir: File? = null
            set(value) {field = value}
            get() = field
        /**
         * Converting dp to pixel
         */
        fun dpToPx(dp: Int, context : Context): Int {
            val r = context.resources
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.getDisplayMetrics()))
        }
    }


}