package com.pigrange.maze.customView

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
        const val ASTAR_PATH = -1
        const val DFS_PATH = -2
        const val BFS_PATH = -3
    }

    private val mWallPaint = Paint()
    private val mRoadPaint = Paint()
    private val mASTARPathPaint = Paint()
    private val mDFSPaint = Paint()
    private val mBFSPaint = Paint()

    var mCells: Array<Array<Cell?>>? = null

    init {
        mWallPaint.color = Color.rgb(0xd3, 97, 97)
        mRoadPaint.color = Color.argb(90, 255, 255, 255)
        mASTARPathPaint.color = Color.rgb(0x9f, 0xbc, 0x7b)
        mDFSPaint.color = Color.YELLOW
        mBFSPaint.color = Color.BLUE
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
                    when (cell.state) {
                        WALL -> canvas!!.drawRect(rect, mWallPaint)
                        ROAD -> canvas!!.drawRect(rect, mRoadPaint)
                        ASTAR_PATH -> canvas!!.drawRect(rect, mASTARPathPaint)
                        DFS_PATH -> canvas!!.drawRect(rect, mDFSPaint)
                        BFS_PATH -> canvas!!.drawRect(rect, mBFSPaint)
                    }
                }
            }
        }
    }

    class Cell(var state: Int, val mRect: RectF)

}
