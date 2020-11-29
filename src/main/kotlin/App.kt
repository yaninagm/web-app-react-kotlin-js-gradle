import kotlinx.browser.window
import kotlinx.coroutines.async
import react.*
import react.dom.*
import kotlinx.coroutines.*

suspend fun fetchVideo(id: Int): Video =
    //window.fetch("https://my-json-server.typicode.com/kotlin-hands-on/kotlinconf-json/videos/$id")

    window.fetch("https://cors-anywhere.herokuapp.com/" + "https://real-customer.herokuapp.com/qrByProduct/$id")
        .await()
        .json()
        .await()
        .unsafeCast<Video>()

suspend fun fetchVideos(): List<Video> = coroutineScope {
    (1..5).map { id ->
        async {
            fetchVideo(id)
        }
    }.awaitAll()
}

external interface AppState : RState {
    var currentVideo: Video?
    var unwatchedVideos: List<Video>
    var watchedVideos: List<Video>
}

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        unwatchedVideos = listOf()
        watchedVideos = listOf()

        val mainScope = MainScope()
        mainScope.launch {
            val videos = fetchVideos()
            setState {
                unwatchedVideos = videos
            }
        }
    }

    override fun RBuilder.render() {
        h1 {
            +"KotlinConf Explorer"
        }
        div {
            h3 {
                +"Videos to watch"


            }

            img {
                attrs {
                    src = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" +
                        "https://www.remax.com.ar/Terreno-Venta-MAIPU-Maipu-Mendoza-_420921063-16"
                }
            }
            videoList {
                videos = state.unwatchedVideos
                selectedVideo = state.currentVideo
                onSelectVideo = { video ->
                    setState {
                        currentVideo = video
                    }
                }
            }
        }
        state.currentVideo?.let { currentVideo ->
            videoPlayer {
                video = currentVideo
                unwatchedVideo = currentVideo in state.unwatchedVideos
                onWatchedButtonPressed = {
                    if (video in state.unwatchedVideos) {
                        setState {
                            unwatchedVideos -= video
                            watchedVideos += video
                        }
                    } else {
                        setState {
                            watchedVideos -= video
                            unwatchedVideos += video
                        }
                    }
                }
            }
        }
    }
}