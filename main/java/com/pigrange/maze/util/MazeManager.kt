package com.pigrange.maze.util

import android.content.Context
import android.graphics.RectF
import android.os.Handler
import android.view.View
import com.pigrange.maze.R
import com.pigrange.maze.customView.MazeView
import com.pigrange.maze.data.PathNode
import com.pigrange.maze.ui.BaseActivity
import com.pigrange.maze.ui.MazeActivity

class MazeManager(private val holder: MazeActivity.ViewHolder, context: Context) : View.OnClickListener, PathFindCallBack {

    companion object {
        const val STYLE_A_STAR = 1
        const val STYLE_BFS = 2
        const val STYLE_DFS = 3
    }


    private var containerWidth: Int = 0
    private var containerHeight: Int = 0

    private var cellCount = 0

    private var cellSize: Float = 30f
    private val activity = context as BaseActivity

    private val mHandler = Handler(activity.mainLooper)

    private lateinit var mCellMap: Array<Array<MazeView.Cell?>>
    private lateinit var mazeGenerator: MazeGenerator
    private lateinit var mazeArray: Array<IntArray>

    private lateinit var mAStarPathFinder: BasePathFinder
    private lateinit var mDFSPathFinder: BasePathFinder
    private lateinit var mBFSPathFinder: BasePathFinder

    init {
        holder.aStarFindWayOut.setOnClickListener(this)
        holder.dFSFindWayOut.setOnClickListener(this)
        holder.bfsFindWayOut.setOnClickListener(this)
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

        mAStarPathFinder = AStarPathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)
        mDFSPathFinder = DFSPathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)
        mBFSPathFinder = BFSPathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)

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
            R.id.A_STAR_find_way_out -> {
                mAStarPathFinder.findPath(this)
                return
            }

            R.id.DFS_find_way_out -> {
                mDFSPathFinder.findPath(this)
                return
            }

            R.id.BFS_find_way_out -> {
                mBFSPathFinder.findPath(this)
                return
            }

            R.id.maze_refresh -> {
                mazeGenerator.refresh()
                mazeArray = mazeGenerator.mazeArray
                mAStarPathFinder = AStarPathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)
                mDFSPathFinder = DFSPathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)
                mBFSPathFinder = BFSPathFinder(mazeArray, 1, 1, cellCount - 2, cellCount - 2)
                initCellMap()
                return
            }
        }
    }

    override fun notify(pathNode: PathNode?, style: Int) {
        mHandler.post { showResult(pathNode, style) }
    }

    private fun showResult(endNode: PathNode?, style: Int) {

        if (endNode == null) {
            holder.currentMode.setText(R.string.stt_current_state_noRoad)
            return
        }

        var node = endNode
        val timeCost = node.timeCost

        val str = activity.resources.getString(R.string.str_current_state_done)
        val done = String.format(str, timeCost)

        val mapState: Int = when (style) {
            STYLE_A_STAR -> {
                MazeView.ASTAR_PATH
            }
            STYLE_DFS -> {
                MazeView.DFS_PATH
            }
            STYLE_BFS -> {
                MazeView.BFS_PATH
            }
            else -> {
                MazeView.ASTAR_PATH
            }
        }

        var stepCount = 0
        while (node != null) {
            mCellMap[node.y][node.x]!!.state = mapState
            node = node.parent
            stepCount++
        }
        val stepStr = activity.resources.getString(R.string.find_way_result_steps)
        val steps = String.format(stepStr, stepCount)

        holder.mFill.requestLayout()
        holder.currentMode.text = done
        holder.steps.text = steps
    }
}
