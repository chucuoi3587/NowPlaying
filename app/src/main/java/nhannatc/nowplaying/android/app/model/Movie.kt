package nhannatc.nowplaying.android.app.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import nhannatc.nowplaying.android.app.BR
import nhannatc.nowplaying.android.app.Constant
import org.json.JSONObject
import java.util.*

data class Movie(val json : JSONObject) : BaseObservable() {
    var vote_average: Double = 0.0
    set(value) {
        field = value
        exptAverage = value.toString()
    }
    var poster_path: String = ""
    set(value) {
        field = value
        exptPath = String.format(Constant.IMAGE_URL, Constant.IMAGE_SIZE_THUMBNAIL) + value
    }
    var title: String = ""

    var exptPath : String = ""
        @Bindable get() = field
        set(value) {field = value
        notifyPropertyChanged(BR.exptPath)}
    var exptAverage : String = ""
        @Bindable get() = field
        set(value) {field = value
        notifyPropertyChanged(BR.exptAverage)}
    init {
        vote_average = json.optDouble("vote_average", 0.0)
        poster_path = json.optString("poster_path", "")
        title = json.optString("title", "")
    }


    companion object {
        @BindingAdapter("posterImage")
        @JvmStatic fun loadImage(view : ImageView, imageUrl : String) {
            Glide.with(view.context).load(imageUrl).into(view)
        }
    }

}