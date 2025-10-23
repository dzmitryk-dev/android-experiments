package dzmitryk.codepractice.recyclerview.ui.filter

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dzmitryk.codepractice.recyclerview.databinding.FragmentFilterBinding
import dzmitryk.codepractice.recyclerview.databinding.ItemStringBinding

/**
 * A fragment representing a list of Items.
 */
class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FilterListAdapter()
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            addItemDecoration(SpacingItemDecorator(16))
        }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        binding.fabAdd.setOnClickListener {
            showFilterDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showFilterDialog() {
        val availableFilters = viewModel.getAvailableFilters()
        val filterNames = availableFilters.map { "${it.name} ${it.emoji}" }.toTypedArray()
        val currentSelectedSet = viewModel.selectedFilters.value ?: emptySet()
        val checkedItems = availableFilters.map { it in currentSelectedSet }.toBooleanArray()
        val selectedSet = currentSelectedSet.toMutableSet()

        AlertDialog.Builder(requireContext())
            .setTitle("Select Filter Types")
            .setMultiChoiceItems(filterNames, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedSet.add(availableFilters[which])
                } else {
                    selectedSet.remove(availableFilters[which])
                }
            }
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                viewModel.updateFilters(selectedSet)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    companion object {

        private class SpacingItemDecorator(private val spacing: Int) :
            RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = spacing
                outRect.bottom = spacing
            }
        }

        private class FilterListAdapter() : ListAdapter<FilterItem, ItemViewHolder>(ITEM_CALLBACK) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val binding = ItemStringBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemViewHolder(binding)
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                holder.bind(getItem(position))
            }
        }

        private val ITEM_CALLBACK = object : DiffUtil.ItemCallback<FilterItem>() {
            override fun areItemsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
                return oldItem == newItem
            }
        }

        private class ItemViewHolder(
            private val binding: ItemStringBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: FilterItem) {
                binding.itemText.text = "${item.type.emoji} ${item.text}"
            }
        }

    }
}