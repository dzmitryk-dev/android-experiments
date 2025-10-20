package demo.memeviewer.data

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

internal class NetworkSource @Inject constructor() : DataSource {

    override fun get(url: String): Document {
        return Jsoup.connect(url).get()
    }
}

internal interface DataSource {
    fun get(url: String): Document
}