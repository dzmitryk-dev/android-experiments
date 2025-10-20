package demo.memeviewer.data

import demo.memeviewer.model.MemeData
import demo.memeviewer.model.PageData
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.Test

class PageParserTest {

    private val testParser = PageParser()

    private val expectedPageData = PageData(
        nextPageUrl = "/m/fun?sort=latest&after=a8kswa",
        memes = listOf(
            MemeData.Image(
                title = "sarcasticat",
                creator = "by kayrobin",
                link = "http://imgflip.com/i/a8kxw1",
                viewCount = "8 views" ,
                imageUrl = "https://i.imgflip.com/a8kxw1.jpg"
            ),
            MemeData.Image(
                title = "Why Do you always wear that mask?",
                creator = "by misteriosoguayff",
                link = "http://imgflip.com/i/a8l17f",
                viewCount = "8 views",
                imageUrl = "https://i.imgflip.com/a8l17f.jpg"
            ),
            MemeData.Image(
                title = "Tuesday time!!",
                creator = "by CoolguyMcSwaggy",
                link = "http://imgflip.com/i/a8kznx",
                viewCount = "19 views",
                imageUrl = "https://i.imgflip.com/a8kznx.jpg"
            ),
            MemeData.Image(
                title = "One big threat!",
                creator = "by CoolguyMcSwaggy",
                link = "http://imgflip.com/i/a8kz4e",
                viewCount = "24 views",
                imageUrl = "https://i.imgflip.com/a8kz4e.jpg"
            ),
            MemeData.Image(
                title = "good vs evil",
                creator = "by Mememaster10230303",
                link = "http://imgflip.com/i/a8kz1h",
                viewCount = "12 views",
                imageUrl = "https://i.imgflip.com/a8kz1h.jpg"
            ),
            MemeData.NSFW,
            MemeData.NSFW
        )
    )

    @Test
    fun testParse() {
        val html = javaClass.getResource("/example-page.html")!!.readText()
        val testPage = Jsoup.parse(html)

        val parsedData = testParser.parse(testPage)

        assertThat(parsedData).isEqualTo(expectedPageData)
    }
}