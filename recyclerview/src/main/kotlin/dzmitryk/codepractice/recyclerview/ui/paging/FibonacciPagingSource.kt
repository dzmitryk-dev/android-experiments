package dzmitryk.codepractice.recyclerview.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.math.BigDecimal

class FibonacciPagingSource : PagingSource<Int, String>() {

    private val fibonacciSequence = sequence {
        var a = BigDecimal.ZERO
        var b = BigDecimal.ONE
        while (true) {
            yield(a)
            val temp = a + b
            a = b
            b = temp
        }
    }

    private var loadedItems = 0

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val startIndex = loadedItems
            val endIndex = startIndex + pageSize

            val data = fibonacciSequence
                .drop(startIndex)
                .take(pageSize)
                .mapIndexed { index, value -> "Fib(${startIndex + index}): $value" }
                .toList()

            loadedItems = endIndex

            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

