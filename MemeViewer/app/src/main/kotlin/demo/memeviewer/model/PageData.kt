package demo.memeviewer.model

data class PageData(
    val memes: List<MemeData>,
    val nextPageUrl: String?
)

sealed class MemeData {

    data class Image(
        val title: String,
        val creator: String,
        val imageUrl: String,
    ) : MemeData()

    data class Video(
        val title: String,
        val creator: String,
        val videoUrl: String,
        val posterUrl: String,
    ) : MemeData()

    data object NSFW : MemeData()
}
