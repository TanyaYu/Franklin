package com.tanyayuferova.franklin.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.TextView

/**
 * Author: Tanya Yuferova
 * Date: 7/13/19
 */
class CheckedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), Checkable {

    private var _isChecked = false

    override fun isChecked() = _isChecked

    override fun toggle() {
        _isChecked = !_isChecked
        refreshDrawableState()
    }

    override fun setChecked(isChecked: Boolean) {
        _isChecked = isChecked
        refreshDrawableState()
    }

    public override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked)
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        return drawableState
    }

    companion object {
        val CHECKED_STATE_SET = arrayOf(android.R.attr.state_checked).toIntArray()
    }
}
