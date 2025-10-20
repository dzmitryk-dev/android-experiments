package demo.memeviewer.data

import demo.memeviewer.model.MemeData
import demo.memeviewer.model.PageData
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import timber.log.Timber

internal class PageParser {

    fun parse(page: Document): PageData {
        val content = page.selectFirst("div#base-left") ?: run {
            Timber.e("No content found. Actual HTML: ${page.html()}")
            throw IllegalStateException("No content found.")
        }

        val memes = content.select("div.base-unit.clearfix").mapNotNull(this::parseElement)

        val url = content.selectFirst("div.pager a.pager-next.l.but")?.attr("href")

        return PageData(memes = memes, nextPageUrl = url)
    }

    private fun parseElement(element: Element): MemeData? {
        val t = element.selectFirst("h2.base-unit-title a")

        val link = t?.attr("href")?.let { "http://imgflip.com$it" }.orEmpty()
        val title = t?.text().orEmpty()

        val baseInfo = element.selectFirst("div.base-info") ?: return null

        val nsfwFlag = baseInfo.selectFirst("div.base-flag")
        if (nsfwFlag != null) {
            return MemeData.NSFW
        }

        val author = baseInfo.selectFirst("div.base-author")?.text().orEmpty()
        val viewCount = baseInfo.selectFirst("div.base-view-count")?.text().orEmpty()

        val imageWrapper = element.selectFirst("div.base-img-wrap-wrap div.base-img-wrap") ?: return null

        val nsfw = imageWrapper.selectFirst("div.base-nsfw-msg")
        if (nsfw != null) {
            return MemeData.NSFW
        }

        val imageElement = imageWrapper.selectFirst("a.base-img-link")?.child(0) ?: return null

        return if (imageElement.tagName() == "video") {
            val gifUrl = imageElement.attr("data-src")
            val posterUrl = imageElement.attr("poster")

            val videoUrl = imageElement.selectFirst("source")?.attr("src").orEmpty()

            return MemeData.Video(
                title = title,
                creator = author,
                viewCount = viewCount,
                link = link,
                gifUrl = "https:$gifUrl",
                videoUrl = "https:$videoUrl",
                posterUrl = "https:$posterUrl"
            )
        } else if (imageElement.tagName() == "img") {
            val imageUrl = imageElement.attr("src")

            MemeData.Image(
                title = title,
                creator = author,
                viewCount = viewCount,
                link = link,
                imageUrl = "https:$imageUrl"
            )
        } else {
            null
        }
    }
}