package demo.memeviewer.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import demo.memeviewer.di.IoDispatcher
import demo.memeviewer.model.MemeData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val START_URL = "https://imgflip.com/m/fun?sort=latest"

internal class MemePagingSource @Inject constructor(
    private val dataSource: DataSource,
    private val parser: PageParser,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : PagingSource<String, MemeData>() {

    override fun getRefreshKey(state: PagingState<String, MemeData>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, MemeData> {
        val link = params.key ?: START_URL

        return try {
            val (memes, nextPageUrl) = withContext(coroutineDispatcher) {
                val page = dataSource.get(link)
                parser.parse(page).let { result ->
                    result.copy(memes = result.memes.filterNot { it is MemeData.NSFW } )
                }
            }

            LoadResult.Page(
                data = memes,
                prevKey = null,
                nextKey = nextPageUrl,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
