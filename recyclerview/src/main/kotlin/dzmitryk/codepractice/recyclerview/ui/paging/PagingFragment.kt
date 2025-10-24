package dzmitryk.codepractice.recyclerview.ui.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dzmitryk.codepractice.recyclerview.databinding.FragmentPagingBinding
import dzmitryk.codepractice.recyclerview.databinding.ItemStringBinding
import dzmitryk.codepractice.recyclerview.utils.SpacingItemDecorator
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items with pagination.
 */
class PagingFragment : Fragment() {

    private var _binding: FragmentPagingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PagingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PagingListAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            addItemDecoration(SpacingItemDecorator(16))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private class PagingListAdapter : PagingDataAdapter<String, StringViewHolder>(ITEM_CALLBACK) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
                val binding = ItemStringBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StringViewHolder(binding)
            }

            override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
                holder.bind(getItem(position))
            }
        }

        private val ITEM_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }

        private class StringViewHolder(
            private val binding: ItemStringBinding,
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: String?) {
                if (item != null) {
                    binding.itemText.text = item
                }
            }
        }
    }
}