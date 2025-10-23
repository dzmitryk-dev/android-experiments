package dzmitryk.codepractice.recyclerview.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    private val _items = MutableLiveData<List<String>>()
    val items: LiveData<List<String>> = _items

    init {
        loadItems()
    }

    private fun loadItems() {
        val sampleItems = (1..20).map { "Item $it" }
        _items.value = sampleItems
    }
}
