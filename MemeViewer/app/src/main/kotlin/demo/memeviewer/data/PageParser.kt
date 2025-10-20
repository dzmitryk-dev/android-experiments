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
       val imageWrapper = element.selectFirst("div.base-img-wrap-wrap div.base-img-wrap") ?: return null

        val nsfw = imageWrapper.selectFirst("div.base-nsfw-msg")
        if (nsfw != null) {
            return MemeData.NSFW
        }

        val imageLink = imageWrapper.selectFirst("a.base-img-link") ?: return null


    }
}