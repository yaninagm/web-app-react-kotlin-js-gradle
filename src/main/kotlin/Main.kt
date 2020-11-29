import kotlinx.browser.document
import react.dom.render


data class Video(
    val id: Int,
    val name: String,
    val urlQr: String,
    val dinamicUrl: String,
    val dateCreated: String,
    val descripction: String,
    val videoUrl: String,
    val title: String,
    val speaker: String
)

fun main() {
    render(document.getElementById("root")) {
        child(App::class) {}
    }
}