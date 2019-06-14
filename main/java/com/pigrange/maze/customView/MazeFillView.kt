package com.pigrange.maze.customView

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView

class MazeFillView(ctx: Context, attr: AttributeSet) : ImageView(ctx, attr) {
    companion object {
        const val WALL = 0
        const val ROAD = 1
        const val PATH = -1
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//
//        val pm = MeasureSpec.getSize(widthMeasureSpec)
//        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
//            setMeasuredDimension(pm, pm)
//        }
//    }

    class MazeDrawable(cells: Array<Array<Cell?>>) : Drawable() {
        private val mWallPaint = Paint()
        private val mRoadPaint = Paint()
        private val mPathPaint = Paint()
        private var mCells: Array<Array<Cell?>>? = null

        init {
            mWallPaint.color = Color.rgb(0xd3, 97, 97)
            mRoadPaint.color = Color.argb(90, 255, 255, 255)
            mPathPaint.color = Color.rgb(0x9f, 0xbc, 0x7b)
            mCells = cells
        }

        override fun draw(canvas: Canvas?) {
            if (mCells != null) {
                for (y in 0 until mCells!!.size) {
                    for (x in 0 until mCells!![0].size) {
                        val cell = mCells!![y][x]
                        val rect = cell!!.mRect
                        when (cell.state) {
                            WALL -> canvas!!.drawRect(rect, mWallPaint)
                            ROAD -> canvas!!.drawRect(rect, mRoadPaint)
                            PATH -> canvas!!.drawRect(rect, mPathPaint)
                        }
                    }
                }
            }
        }

        override fun setAlpha(alpha: Int) {
            mWallPaint.alpha = alpha
        }

        override fun getOpacity(): Int {
            return PixelFormat.OPAQUE
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            mWallPaint.colorFilter = colorFilter
        }
    }

    class Cell(var state: Int, val mRect: RectF)
}
