package nhannatc.nowplaying.android.app

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import nhannatc.nowplaying.android.app.adapter.MovieListRecylerAdapter
import nhannatc.nowplaying.android.app.application.App
import nhannatc.nowplaying.android.app.databinding.ActivityMainBinding
import nhannatc.nowplaying.android.app.model.Movie
import nhannatc.nowplaying.android.app.network.DataLoader
import nhannatc.nowplaying.android.app.util.CommonUtil
import nhannatc.nowplaying.android.app.viewmodel.MovieListViewModel
import org.json.JSONObject
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    var adapter : MovieListRecylerAdapter? = null
    var isLock = false;
    @Inject lateinit var viewmodel : MovieListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null || (savedInstanceState!= null && !savedInstanceState.getBoolean("is_init", true))) {
            initViews()
            val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            viewmodel = ViewModelProviders.of(MainActivity@this).get(MovieListViewModel::class.java)
//            (application as App).getApplicationComponent().inject(this)
            binding.vm = viewmodel
            binding.lifecycleOwner = this
            binding.setVariable(BR.vm, viewmodel)
            binding.executePendingBindings()

            adapter = viewmodel.getData()?.let { MovieListRecylerAdapter(it) }
            recyclerView.adapter = adapter
            viewmodel.model.observe(this, Observer { it -> adapter!!.replaceData(it!!) })
            viewmodel.isLock.observe(this, Observer { it ->
//                showOrHideLoading(it!!)
                Log.d("NhanNATC", "==== isLock : " + it)
            })

            viewmodel.getMovies()
        }
    }

    private fun showOrHideLoading(flag : Boolean) {
        if (flag)
            findViewById<LinearLayout>(R.id.progressLayout).visibility = View.VISIBLE
        else
            findViewById<LinearLayout>(R.id.progressLayout).visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("is_init", true)
    }
    private fun initViews() {
//        mGetPopularMoviesWs = GetPopularMovieWs(this)
//        mGetPopularMoviesWs.getMovies(mPage)
        isLock = true;
        recyclerView = findViewById(R.id.recycler_view)
        val mLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, CommonUtil.dpToPx(1, MainActivity@this), true))
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!viewmodel?.isLock.value!! && !recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE)
                    viewmodel.loadMore()
            }
        })
    }

    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

}
