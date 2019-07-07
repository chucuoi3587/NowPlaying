package nhannatc.nowplaying.android.app.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import nhannatc.nowplaying.android.app.BR
import nhannatc.nowplaying.android.app.Constant
import nhannatc.nowplaying.android.app.R
import nhannatc.nowplaying.android.app.databinding.MovieCardItemBinding
import nhannatc.nowplaying.android.app.model.Movie
//import nhannatc.nowplaying.android.app.util.GlideApp

class MovieListRecylerAdapter(movies : ArrayList<Movie>) : RecyclerView.Adapter<MovieListRecylerAdapter.ViewHolder>() {
    var movies : ArrayList<Movie>
    var mContext : Context? = null
    init {
        this.movies = movies

    }

    fun replaceData(movielist : ArrayList<Movie>) {
//        this.movies.clear()
        this.movies = movielist
//        this.movies.addAll(movielist)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        if (mContext == null)
            mContext = parent.context
//        val v = LayoutInflater.from(mContext).inflate(R.layout.movie_card_item, parent, false)
        val inflater = LayoutInflater.from(mContext)
//        val viewbinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.movie_card_item, parent, false)
        val viewbinding = nhannatc.nowplaying.android.app.databinding.MovieCardItemBinding.inflate(inflater)
        val viewHolder = ViewHolder(viewbinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        val movie = movies.get(position)
        viewholder.onBind(movie/*, mContext!!*/)
    }

//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////        private var thumbnail : ImageView
////        private var averageVote : TextView
////        var container : LinearLayout
////
////        init {
////            thumbnail = itemView.findViewById(R.id.thumbnail)
////            averageVote = itemView.findViewById(R.id.voteAverageTv)
////            container = itemView.findViewById(R.id.container)
////        }
////        fun onBind(movie : Movie, context : Context) {
////            averageVote.text = movie.vote_average.toString()
////            Glide.with(context).load(String.format(Constant.Companion.IMAGE_URL, Constant.IMAGE_SIZE_THUMBNAIL) + movie.poster_path).into(thumbnail)
//////            GlideApp.with(context).load(String.format(Constant.Companion.IMAGE_URL, Constant.IMAGE_SIZE_THUMBNAIL) + movie.poster_path).into(thumbnail)
////        }
////    }
    inner class ViewHolder(val binding : MovieCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(movie : Movie) {
//            binding.setVariable(BR.movie, movie)
//            binding.setVariable(BR.imageUrl, movie.exptPath)
            binding.movie = movie
            binding.imageUrl = movie.exptPath
            binding.executePendingBindings()
        }

}
}