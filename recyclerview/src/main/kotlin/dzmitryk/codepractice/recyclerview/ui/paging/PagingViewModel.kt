package dzmitryk.codepractice.recyclerview.ui.paging

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class PagingViewModel : ViewModel() {
    val items: Flow<PagingData<String>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { FibonacciPagingSource() }
    ).flow
}