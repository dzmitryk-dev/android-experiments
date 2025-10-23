package dzmitryk.codepractice.recyclerview.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors

class SpacingItemDecorator(private val spacing: Int) : RecyclerView.ItemDecoration() {
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

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val color = MaterialColors.getColor(parent, android.R.attr.colorPrimary)
        val paint = android.graphics.Paint().apply {
            setColor(color)
            strokeWidth = 2f
        }

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val bottom = child.bottom + spacing / 2
            c.drawLine(
                child.left.toFloat() + spacing,
                bottom.toFloat(),
                child.right.toFloat() - spacing,
                bottom.toFloat(),
                paint
            )
        }
    }
}
