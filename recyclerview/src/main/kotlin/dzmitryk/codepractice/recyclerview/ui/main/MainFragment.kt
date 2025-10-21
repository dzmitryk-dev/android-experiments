package dzmitryk.codepractice.recyclerview.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dzmitryk.codepractice.recyclerview.R

import dzmitryk.codepractice.recyclerview.databinding.FragmentMainBinding
import dzmitryk.codepractice.recyclerview.databinding.ItemStringBinding
import dzmitryk.codepractice.recyclerview.utils.ArrayAdapter

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        val hardcodedStrings = requireContext().resources.getStringArray(R.array.items)
        val adapter = ItemAdapter(hardcodedStrings)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setAdapter(adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class ItemAdapter(items: Array<String>) :
        ArrayAdapter<String, ItemAdapter.StringViewHolder>(items) {
        class StringViewHolder(
            private val binding: ItemStringBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: String) {
                binding.itemText.text = item
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
            val binding =
                ItemStringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StringViewHolder(binding)
        }

        override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

}
