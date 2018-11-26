package com.pigrange.maze

import android.content.Context
import android.graphics.Rect
import android.view.View

class MazeManager(private val holder: MazeActivity.ViewHolder, private val context: Context) : View.OnClickListener {
    private var containerWidth: Int = 0
    private var containerHeight: Int = 0

    private var cellCount = 0

    private var cellSize: Int = 30
    private lateinit var mCellMap: Array<Array<MazeView.Cell?>>
    private val activity = context as BaseActivity

    private lateinit var mazeGenerator: MazeGenerator
    private lateinit var mazeArray: Array<IntArray>
    private lateinit var pathFinder: PathFinder

    init {
        holder.findWayOut.setOnClickListener(this)
        holder.refresh.setOnClickListener(this)
    }

    fun changeContainerSize() {
        val windowWidth = activity.windowManager.defaultDisplay.width
        val windowHeight = activity.windowManager.defaultDisplay.height
        holder.container.layoutParams.width = windowWidth
        holder.container.layoutParams.height = windowWidth

        holder.footer.layoutParams.height = windowHeight - windowWidth
        holder.container.requestLayout()
        holder.footer.requestLayout()
    }

    fun initMaze(cellCount: Int) {
        this.cellCount = cellCount
        mazeGenerator = MazeGenerator(cellCount)
        mazeArray = mazeGenerator.mazeArray
        pathFinder = PathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)

        holder.container.post {
            containerHeight = holder.container.height
            containerWidth = holder.container.width

            val wid = if (cellSize * cellCount > containerWidth) {
                Math.min(cellSize, containerWidth / cellCount)
            } else {
                Math.max(cellSize, containerWidth / cellCount)
            }

            val het = if (cellSize * cellCount > containerHeight) {
                Math.min(cellSize, containerHeight / cellCount)
            } else {
                Math.max(cellSize, containerHeight / cellCount)
            }

            cellSize = Math.min(het, wid)

            holder.mFill.layoutParams.width = cellSize * cellCount
            holder.mFill.layoutParams.height = cellSize * cellCount

            initCellMap()
        }
    }

    private fun initCellMap() {
        mCellMap = Array(cellCount) { arrayOfNulls<MazeView.Cell>(cellCount) }
        for (y in 0 until cellCount) {
            for (x in 0 until cellCount) {
                val rect = Rect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize)
                val cell = MazeView.Cell(mazeArray[y][x], rect)
                mCellMap[y][x] = cell
            }
        }
        holder.mFill.setCells(mCellMap)
        holder.currentMode.setText(R.string.star_current_state_ready)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.maze_find_way_out -> {
                var endNode = pathFinder.findPath()
                while (endNode != null) {
                    mCellMap[endNode.y][endNode.x]!!.state = MazeView.PATH
                    endNode = endNode.fatherNode
                }
                holder.mFill.requestLayout()
                holder.currentMode.setText(R.string.star_current_state_done)
            }

            R.id.maze_refresh -> {
                mazeGenerator.refresh()
                mazeArray = mazeGenerator.mazeArray
                pathFinder = PathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)
                initCellMap()
            }
        }
    }
}
