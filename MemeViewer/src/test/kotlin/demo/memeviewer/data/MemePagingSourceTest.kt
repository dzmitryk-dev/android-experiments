
package demo.memeviewer.data

import androidx.paging.PagingSource
import demo.memeviewer.model.MemeData
import demo.memeviewer.model.PageData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MemePagingSourceTest {

    private val dataSource: DataSource = mock()
    private val parser: PageParser = mock()
    private val testDispatcher = StandardTestDispatcher()

    private val pagingSource by lazy {
        MemePagingSource(dataSource, parser, testDispatcher)
    }

    @Test
    fun `load returns page when successful`() = runTest(testDispatcher) {
        val mockedDocument = mock<Document>()
        val fakePageData = PageData(
            memes = listOf(
                MemeData.Image(
                    title = "test",
                    creator = "creator",
                    link = "link",
                    viewCount = "100",
                    imageUrl = "imageUrl"
                )
            ),
            nextPageUrl = "nextPageUrl"
        )
        val expectedLoadResult = PagingSource.LoadResult.Page<String, MemeData>(
            data = listOf(
                MemeData.Image(
                    title = "test",
                    creator = "creator",
                    link = "link",
                    viewCount = "100",
                    imageUrl = "imageUrl"
                )
            ),
            prevKey = null,
            nextKey = "nextPageUrl"
        )

        whenever(dataSource.get("https://imgflip.com/m/fun?sort=latest")).thenReturn(mockedDocument)
        whenever(parser.parse(mockedDocument)).thenReturn(fakePageData)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 2, placeholdersEnabled = false)
        )

        assertThat(result).isEqualTo(expectedLoadResult)
    }

    @Test
    fun `load returns error when data source throws exception`() = runTest(testDispatcher) {
        val exception = IOException()
        whenever(dataSource.get("https://imgflip.com/m/fun?sort=latest")).thenThrow(exception)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 2, placeholdersEnabled = false)
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
    }

    @Test
    fun `load returns error when parser throws exception`() = runTest(testDispatcher) {
        val mockedDocument = mock<Document>()
        val exception = RuntimeException()
        whenever(dataSource.get("https://imgflip.com/m/fun?sort=latest")).thenReturn(mockedDocument)
        whenever(parser.parse(mockedDocument)).thenThrow(exception)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 2, placeholdersEnabled = false)
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
    }
}
