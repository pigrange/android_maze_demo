package com.pigrange.maze.util

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.pigrange.maze.data.PathNode
import com.pigrange.maze.util.MazeManager.Companion.STYLE_BFS
import java.util.*

class BFSPathFinder(private val map: Array<IntArray>, private val startY: Int, private val startX: Int, private val endY: Int, private val endX: Int) : BasePathFinder {
    private val mHandlerThread = HandlerThread("BFS").apply { start() }
    private val mHandler = Handler(mHandlerThread.looper)

    override fun findPath(callBack: PathFindCallBack) {
        mHandler.post {
            Log.e("TAG", "BFS start!!!")
            val startTime = System.currentTimeMillis()
            val endNode = BFS()

            val timeCost = System.currentTimeMillis() - startTime
            if (endNode != null) endNode.timeCost = timeCost

            callBack.notify(endNode, STYLE_BFS)
        }
    }

    private fun BFS(): PathNode? {
        //右下左上
        val dx = arrayOf(1, 0, -1, 0)
        val dy = arrayOf(0, 1, 0, -1)
        val routeMap = Array(endY + 2) { Array(endX + 2) { true } }

        val queue = LinkedList<PathNode>()
        val startNode = PathNode(startY, startX)
        queue.add(startNode)

        while (queue.isNotEmpty()) {
            val node = queue.poll()

            if (node.x == endX && node.y == endY) {
                return node
            }

            for (i in 0..3) {

                //避免来回走
                if (i == checkNode(node)) {
                    continue
                }

                val x = node.x + dx[i]
                val y = node.y + dy[i]

                if (isPath(x, y) && routeMap[y][x]) {
                    routeMap[y][x] = false
                    val child = PathNode(y, x)
                    child.parent = node
                    child.count = node.count + 1
                    queue.add(child)
                }
            }
        }
        return null
    }

    private fun checkNode(node: PathNode): Int {
        val parent = node.parent ?: return -1
        val dx = node.x - parent.x
        val dy = node.y - parent.y
        when {
            dx == 1 && dy == 0 -> return 2
            dx == 0 && dy == 1 -> return 3
            dx == -1 && dy == 0 -> return 0
            dx == 0 && dy == -1 -> return 1
        }
        return -1
    }

    private fun isPath(y: Int, x: Int): Boolean {
        val rowCount = map.size
        val colCount = map[0].size

        return if (y >= 1 && y < rowCount - 1 && x >= 1 && x < colCount - 1) {
            map[y][x] != 0
        } else {
            false
        }
    }
}
