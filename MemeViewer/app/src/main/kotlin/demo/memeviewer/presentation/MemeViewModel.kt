package demo.memeviewer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.memeviewer.data.MemePagingSource
import javax.inject.Inject

@HiltViewModel
internal class MemeViewModel @Inject constructor(
    memePagingSource: MemePagingSource,
) : ViewModel() {

    val memes = Pager(
        config = PagingConfig(pageSize = 14),
        pagingSourceFactory = { memePagingSource }
    ).flow.cachedIn(viewModelScope)
}
