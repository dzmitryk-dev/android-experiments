package demo.memeviewer.data

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

internal class NetworkSource @Inject constructor() : DataSource {

    override fun get(url: String): Document {
        return Jsoup.connect(url).get()
    }
}

internal interface DataSource {

    @Throws(IOException::class)
    fun get(url: String): Document
}