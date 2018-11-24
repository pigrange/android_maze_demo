package com.pigrange.maze

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView

class Grid(ctx: Context, val size: Int) : TextView(ctx) {
    constructor(context: Context, attrs: AttributeSet, size: Int) : this(context, size)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, size: Int) : this(context, size)

    var state: GridState = GridState.FREE

    fun init(x: Int, y: Int, gridSize: Int, state: GridState) {
        this.x = x.toFloat()
        this.y = y.toFloat()
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(size, size)
        params.width = gridSize
        params.height = gridSize
        this.layoutParams = params
        setColor(state)
    }

    fun setColor(state: GridState) {
        when (state) {
            GridState.FREE -> {
                this.state = state
                this.setBackgroundColor(resources.getColor(R.color.colorFree))
            }
            GridState.BLOCK -> {
                this.state = state
                this.setBackgroundColor(resources.getColor(R.color.colorBlock))
            }
            GridState.WAY -> {
                this.setBackgroundColor(resources.getColor(R.color.colorGreen))
            }
        }
    }

    enum class GridState {
        FREE, BLOCK, WAY
    }
}
