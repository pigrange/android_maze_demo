package com.pigrange.maze

import android.content.Context
import android.graphics.RectF
import android.view.View

class MazeManager(private val holder: MazeActivity.ViewHolder, private val context: Context) : View.OnClickListener {
    private var containerWidth: Int = 0
    private var containerHeight: Int = 0

    private var cellCount = 0

    private var cellSize: Float = 30f
    private val activity = context as BaseActivity

    private lateinit var mCellMap: Array<Array<MazeView.Cell?>>
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
                Math.min(cellSize, containerWidth.toFloat() / cellCount)
            } else {
                Math.max(cellSize, containerWidth.toFloat() / cellCount)
            }

            val het = if (cellSize * cellCount > containerHeight) {
                Math.min(cellSize, containerHeight.toFloat() / cellCount)
            } else {
                Math.max(cellSize, containerHeight.toFloat() / cellCount)
            }

            cellSize = Math.min(het, wid)

            holder.mFill.layoutParams.width = (cellSize * cellCount).toInt()
            holder.mFill.layoutParams.height = (cellSize * cellCount).toInt()

            initCellMap()
        }
    }

    private fun initCellMap() {
        mCellMap = Array(cellCount) { arrayOfNulls<MazeView.Cell>(cellCount) }
        for (y in 0 until cellCount) {
            for (x in 0 until cellCount) {
                val rect = RectF(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize)
                val cell = MazeView.Cell(mazeArray[y][x], rect)
                mCellMap[y][x] = cell
            }
        }
        holder.mFill.setCells(mCellMap)
        holder.currentMode.setText(R.string.str_current_state_ready)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.maze_find_way_out -> {
                var endNode = pathFinder.findPath()
                if (endNode == null) {
                    holder.currentMode.setText(R.string.stt_current_state_noRoad)
                    return
                }

                val count = endNode.count
                val str = activity.resources.getString(R.string.str_current_state_done)
                val done = String.format(str,count)

                while (endNode != null) {
                    mCellMap[endNode.y][endNode.x]!!.state = MazeView.PATH
                    endNode = endNode.fatherNode
                }

                holder.mFill.requestLayout()
                holder.currentMode.text = done
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
