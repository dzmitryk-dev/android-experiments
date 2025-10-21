package dzmitryk.codepractice.recyclerview.utils

import androidx.recyclerview.widget.RecyclerView

internal abstract class ArrayAdapter<T, VH : RecyclerView.ViewHolder>(
    private val items: Array<T>
) : RecyclerView.Adapter<VH>() {

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): T = items[position]
}