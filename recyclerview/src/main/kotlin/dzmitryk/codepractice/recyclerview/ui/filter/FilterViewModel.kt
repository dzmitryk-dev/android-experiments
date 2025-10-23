package dzmitryk.codepractice.recyclerview.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile
import kotlin.random.Random

class FilterViewModel : ViewModel() {

    @Volatile
    private var data: List<FilterItem> = emptyList()
    private val _items = MutableLiveData<List<FilterItem>>()
    val items: LiveData<List<FilterItem>> = _items

    init {
        generateItems()
    }

    private fun generateItems() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val random = Random(System.currentTimeMillis())
                val size = random.nextInt(10, 100)
                data = buildList(size) {
                    repeat(size) {
                        add(
                            FilterItem(
                                text = "Item $it",
                                type = FilterItemType.entries.toTypedArray().random(random)
                            )
                        )
                    }
                }
            }
            _items.value = data
        }
    }
}
