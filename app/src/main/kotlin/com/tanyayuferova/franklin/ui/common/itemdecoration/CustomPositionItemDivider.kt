package com.tanyayuferova.franklin.ui.common.itemdecoration

import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.tanyayuferova.franklin.R
import kotlin.math.roundToInt

class CustomPositionItemDivider(
    context: Context,
    orientation: Int = VERTICAL,
    divider: Drawable = ContextCompat.getDrawable(context, R.drawable.divider)!!,
    private val positions: IntArray = IntArray(0)
) : ItemDivider(context, orientation, divider) {

    private val bounds = Rect()

    override fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left = 0
        val right = parent.width

        positions.forEach { i ->
            val child = parent.findViewHolderForAdapterPosition(i)
            if (child != null) {
                val childView = child.itemView
                parent.getDecoratedBoundsWithMargins(childView, bounds)
                val bottom = bounds.bottom + childView.translationY.roundToInt()
                val top = bottom - divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top = 0
        val bottom = parent.height

        positions.forEach { i ->
            val child = parent.findViewHolderForAdapterPosition(i)
            if (child != null) {
                val childView = child.itemView
                parent.layoutManager!!.getDecoratedBoundsWithMargins(childView, bounds)
                val right = bounds.right + childView.translationX.roundToInt()
                val left = right - divider.intrinsicWidth
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
//        if (parent.indexOfChild(view) !in positions) {
//            outRect.set(0, 0, 0, 0)
//            return
//        }
//todo refactor
        positions.forEach { i ->
            if (parent.findViewHolderForAdapterPosition(i)?.itemView !== view) {
                outRect.set(0, 0, 0, 0)
                return
            }
        }

        super.getItemOffsets(outRect, view, parent, state)
    }
}
