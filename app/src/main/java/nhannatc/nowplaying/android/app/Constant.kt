package nhannatc.nowplaying.android.app

class Constant {
    companion object {
        val HTTP_CONNECTION_TIMEOUT:Int = 30000
        val HTTP_READ_TIMEOUT = 20000
        val POST_REQUEST = "POST"
        val GET_REQUEST = "GET"
        val PER_PAGE = 10
        val APP_API = "https://api.themoviedb.org/3"
        val IMAGE_URL = "https://image.tmdb.org/t/p/%s"
        val IMAGE_SIZE_ORIGINAL = "original"
        val IMAGE_SIZE_THUMBNAIL = "w400"

        val WS_API_GET_POPULAR_MOVIES = "/movie/popular"
        val REQUEST_API_GET_POPULAR_MOVIES = 1001

        val HTTP_STATUS_OK = 200
        val REQUEST_NETWORK_FAILED = 500
        val REQUEST_BAD_GATEWAY = 502
        val REQUEST_NOT_FOUND = 404
        val REQUEST_SERVER_DIE = 505
        val REQUEST_NETWORK_TIMEOUT = -1
        val API_KEY = "84ff27f65e63b715081f9896148a0187"

    }

}