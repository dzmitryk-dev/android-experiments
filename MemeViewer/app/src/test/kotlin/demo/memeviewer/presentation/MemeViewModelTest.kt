package demo.memeviewer.presentation

import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import demo.memeviewer.data.MemePagingSource
import demo.memeviewer.model.MemeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MemeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val memePagingSource: MemePagingSource = mock()

    private lateinit var viewModel: MemeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MemeViewModel(memePagingSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `memes should return flow of paging data`() = runTest {
        val memes = listOf(
            MemeData.Image(
                title = "title",
                creator = "creator",
                link = "link",
                viewCount = "viewCount",
                imageUrl = "imageUrl"
            ),
        )
        whenever(memePagingSource.load(any())) doReturn PagingSource.LoadResult.Page(memes, null, null)

        val snapshot = viewModel.memes.asSnapshot()

        assertThat(snapshot).isEqualTo(memes)
    }
}
