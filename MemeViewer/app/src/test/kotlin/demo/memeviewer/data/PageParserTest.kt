package demo.memeviewer.data

import demo.memeviewer.model.PageData
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.Test

class PageParserTest {

    private val testParser = PageParser()

    private val expectedPageData = PageData(
        nextPageUrl = "/m/fun?sort=latest&after=a8kswa",
        memes = emptyList()
    )

    @Test
    fun testParse() {
        val html = javaClass.getResource("/example-page.html")!!.readText()
        val testPage = Jsoup.parse(html)

        val parsedData = testParser.parse(testPage)

        assertThat(parsedData).isEqualTo(expectedPageData)
    }
}