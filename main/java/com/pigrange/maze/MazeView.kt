package com.pigrange.maze

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class MazeView(ctx: Context, attributeSet: AttributeSet) : View(ctx, attributeSet) {
    companion object {
        const val WALL = 0
        const val ROAD = 1
        const val PATH = -1
    }

    private val mWallPaint = Paint()
    private val mRoadPaint = Paint()
    private val mPathPaint = Paint()

    var mCells: Array<Array<Cell?>>? = null

    init {
        mWallPaint.color = Color.rgb(0xd3, 97, 97)
        mRoadPaint.color = Color.argb(90, 255, 255, 255)
        mPathPaint.color = Color.rgb(0x9f, 0xbc, 0x7b)
    }

    fun setCells(cells: Array<Array<Cell?>>) {
        mCells = cells
        requestLayout()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mCells != null) {
            for (y in 0 until mCells!!.size) {
                for (x in 0 until mCells!![0].size) {
                    val cell = mCells!![y][x]
                    val rect = cell!!.mRect
                    when {
                        cell.state == WALL -> canvas!!.drawRect(rect, mWallPaint)
                        cell.state == ROAD -> canvas!!.drawRect(rect, mRoadPaint)
                        cell.state == PATH -> canvas!!.drawRect(rect, mPathPaint)
                    }
                }
            }
        }
    }

    class Cell(var state: Int, val mRect: RectF)

}
