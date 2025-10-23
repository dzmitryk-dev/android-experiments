package dzmitryk.codepractice.recyclerview.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dzmitryk.codepractice.recyclerview.R
import dzmitryk.codepractice.recyclerview.databinding.FragmentMainBinding
import dzmitryk.codepractice.recyclerview.databinding.ItemStringBinding
import dzmitryk.codepractice.recyclerview.utils.ArrayAdapter
import dzmitryk.codepractice.recyclerview.utils.SpacingItemDecorator

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()

        enum class Screens(@StringRes val titleResId: Int) {
            FILTER(R.string.filter_fragment_name)
        }
    }

    private var _binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = _binding ?: return

        val adapter = ItemAdapter(Screens.entries.toTypedArray(), ::handleScreenNavigation)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setAdapter(adapter)
            addItemDecoration(SpacingItemDecorator(16))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleScreenNavigation(screen: Screens) {
        if (screen == Screens.FILTER) {
            findNavController().navigate(R.id.action_mainFragment_to_filterFragment)
        }
    }

    private class ItemAdapter(
        items: Array<Screens>,
        private val onItemClick: (Screens) -> Unit
    ) : ArrayAdapter<Screens, ItemAdapter.StringViewHolder>(items) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
            val binding =
                ItemStringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StringViewHolder(binding, onItemClick)
        }

        override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        class StringViewHolder(
            private val binding: ItemStringBinding,
            private val onItemClick: (Screens) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(screen: Screens) {
                binding.itemText.text = binding.root.context.getString(screen.titleResId)
                binding.root.setOnClickListener { onItemClick(screen) }
            }
        }
    }
}
