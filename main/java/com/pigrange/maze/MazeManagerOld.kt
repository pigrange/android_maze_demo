package com.pigrange.maze

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.pigrange.maze.MazeActivity.OperationState

class MazeManagerOld(private val holder: MazeActivity.ViewHolder, private val context: Context) {

    var mCurrentState: MazeActivity.OperationState = MazeActivity.OperationState.WAITING

    private var mazeWidth: Int = 100
    private var mazeHeight: Int = 100
    private var containerWidth: Int = 0

    private var containerHeight: Int = 0
    private var gridSize: Int = 0
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
    private var access = ArrayList<Grid>()

    private val activity = context as BaseActivity

    fun init() {
        mazeMap = Array(mazeHeight) { arrayOfNulls<Grid>(mazeWidth) }

        val windowManager: WindowManager = activity.baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.defaultDisplay.width
        val height = windowManager.defaultDisplay.height

        containerWidth = holder.container.measuredWidth
        containerHeight = holder.container.measuredHeight

        gridSize = containerWidth / mazeWidth

        marginTop = (containerHeight - gridSize * mazeHeight) / 2
        marginLeft = (containerWidth - gridSize * mazeWidth) / 2

        refresh()
    }

    private fun refresh() {

        //清空所用控件
        holder.container.removeAllViewsInLayout()
        //初始化数据
        for (y in 0 until mazeHeight) {
            for (x in 0 until mazeWidth) {
                val grid = Grid(context,gridSize)
                holder.container.addView(grid)

                grid.init(
                        marginLeft + gridSize * x,
                        marginTop + gridSize * y,
//                        0 + gridSize * y,
                        gridSize,
                        Grid.GridState.FREE)

                grid.setOnClickListener(GridOnClickListener(x, y))

                mazeMap[y][x] = grid
            }
        }
        //生成空白区域
        clean()
        //生成迷宫
        generateBlocks()
    }

    private fun generateBlocks() {
        for (i in 0 until mazeHeight) {
            for (j in 0 until mazeWidth) {
                mazeMap[i][j]!!.setColor(Enums.random(Grid.GridState::class.java))
            }
        }
    }

    private fun clean() {
        access.clear()
        path.clear()
        for (i in 0 until mazeHeight) {
            for (j in 0 until mazeWidth) {
                mazeMap[i][j]!!.setColor(Grid.GridState.FREE)
            }
        }
    }

    private fun searchShortestPath() {
        count = 0
        val len = dfs(formX, formY, 0)
        if (len == Int.MAX_VALUE) {
            activity.runOnUiThread { activity.showToast("没有通路") }
        } else {
            activity.runOnUiThread {
                drawAccess()
                activity.showToast("最短路径长度为$len\n ${count}次DFS")
            }
        }
        mCurrentState = OperationState.FINISH
        activity.runOnUiThread {
            holder.currentMode.text = "完成"
        }
    }

    private fun drawAccess() {
        for (grid in access) {
            grid.setColor(Grid.GridState.WAY)
        }
        formX = -1
        formY = -1
        toX = -1
        toY = -1
    }

    private fun dfs(x: Int, y: Int, length: Int): Int {
        count++

        //退出条件
        if (x < 0 || x >= mazeWidth
                || y < 0
                || y >= mazeHeight
                || length > minLength
                || mazeMap[y][x]!!.state == Grid.GridState.BLOCK
                || mazeMap[y][x]!!.state == Grid.GridState.WAY) {
            return minLength
        }
        //终止条件
        if (x == toX && y == toY) {
            minLength = length
            access.addAll(path)
            return minLength
        }
        //标记走过
        mazeMap[y][x]!!.state = Grid.GridState.WAY
        path.add(mazeMap[y][x]!!)

        //上
        dfs(x, y + 1, length + 1)

        //右上
//        dfs(x + 1, y + 1, length + 1)

        //右
        dfs(x + 1, y, length + 1)

        //右下
//        dfs(x + 1, y - 1, length + 1)

        //下
        dfs(x, y - 1, length + 1)

        //左下
//        dfs(x - 1, y - 1, length + 1)

        //左
        dfs(x - 1, y, length + 1)

        //左上
//        dfs(x - 1, y + 1, length + 1)

        //还原点的状态
        mazeMap[y][x]!!.state = Grid.GridState.FREE
        path.removeAt(path.size - 1)
        return minLength
    }

    private inner class GridOnClickListener(var x: Int, var y: Int) : View.OnClickListener {
        override fun onClick(v: View?) {
            when (mCurrentState) {

                OperationState.REMOVING_BLOCK -> mazeMap[y][x]!!.setColor(Grid.GridState.FREE)

                OperationState.ADDING_BLOCK -> mazeMap[y][x]!!.setColor(Grid.GridState.BLOCK)

                OperationState.CHOOSING_ORIGIN -> {
                    if (mazeMap[y][x]!!.state != Grid.GridState.BLOCK) {
                        formX = x
                        formY = y
                        mazeMap[y][x]!!.setColor(Grid.GridState.WAY)
                        Toast.makeText(context, "请选择终点", Toast.LENGTH_SHORT).show()
                        mCurrentState = OperationState.CHOOSING_TERMINUS
                    }
                }
                OperationState.CHOOSING_TERMINUS -> {
                    if (mazeMap[y][x]!!.state != Grid.GridState.BLOCK) {
                        toX = x
                        toY = y
                        mazeMap[y][x]!!.setColor(Grid.GridState.WAY)
                        mCurrentState = OperationState.FINDING_WAY
                        holder.currentMode.text = "正在寻路"
                        Thread(Runnable {
                            searchShortestPath()
                        }).start()
                    }
                }
                else -> return
            }
        }
    }
}
