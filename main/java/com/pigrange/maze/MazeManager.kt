package com.pigrange.maze

import android.content.Context
import android.view.View

class MazeManager(private val holder: MazeActivity.ViewHolder, private val context: Context) : View.OnClickListener {

    private var mazeWidth: Int = 100
    private var mazeHeight: Int = 100

    private var containerWidth: Int = holder.container.measuredWidth
    private var containerHeight: Int = holder.container.measuredHeight

    private var gridSize: Int = 15
    private var marginTop: Int = 0

    private var marginLeft: Int = 0
    private var formX = -1
    private var formY = -1
    private var toX = -1
    private var toY = -1
    private var count = 0

    private var minLength = Int.MAX_VALUE

    private lateinit var mazeMap: Array<Array<Grid?>>
    private var path = ArrayList<Grid>()
    private var access = ArrayList<Grid?>()

    private val activity = context as BaseActivity

    fun initMaze(height: Int, width: Int) {
        if (height * gridSize > containerHeight) {
            gridSize = Math.min(gridSize, containerHeight / height)
        }
        if (width * gridSize > containerWidth) {
            gridSize = Math.min(gridSize, containerWidth / width)
        }

        holder.fill.layoutParams.width = gridSize * width
        holder.fill.layoutParams.height = gridSize * height
        holder.fill.requestLayout()

        mazeMap = Array(height) { arrayOfNulls<Grid>(width) }
        for (y in 0 until height) {
            for (x in 0 until width) {
                val grid = Grid(context, gridSize)
                grid.init(x * gridSize, y * gridSize, gridSize, Enums.random(Grid.GridState::class.java))
                grid.setOnClickListener(this)
                mazeMap[x][y] = grid
                holder.fill.addView(grid)
            }
        }
    }


    override fun onClick(v: View?) {

    }
}
