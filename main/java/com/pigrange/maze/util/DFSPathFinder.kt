package com.pigrange.maze.util

import android.os.Handler
import android.os.HandlerThread
import com.pigrange.maze.data.PathNode
import com.pigrange.maze.util.MazeManager.Companion.STYLE_DFS

class DFSPathFinder(private val map: Array<IntArray>, private val startY: Int, private val startX: Int, private val endY: Int, private val endX: Int) : BasePathFinder {
    private val mHandlerThread = HandlerThread("DFS").apply { start() }
    private val mHandler = Handler(mHandlerThread.looper)

    companion object {
        const val RIGHT = 1
        const val DOWN = 2
        const val LEFT = 3
        const val UP = 4
    }

    override fun findPath(callBack: PathFindCallBack) {
        mHandler.post {
            val startNode = PathNode(startY, startX)
            var result: PathNode? = null

            val startTime = System.currentTimeMillis()
            val size = endX * endY
            val pathList = Array<PathNode?>(size) { null }

            result = DFS(startNode, startY, startX + 1, RIGHT, pathList, 0, result)
            result = DFS(startNode, startY + 1, startX, DOWN, pathList, 0, result)

            val timeCost = System.currentTimeMillis() - startTime
            if (result != null) result.timeCost = timeCost
            callBack.notify(result, STYLE_DFS)
        }
    }

    private fun DFS(father: PathNode, y: Int, x: Int, previousAction: Int, pathList: Array<PathNode?>
                    , pos: Int, currentResult: PathNode?): PathNode? {

        if (currentResult != null && currentResult.count <= father.count) {
            return currentResult
        }

        var result: PathNode? = currentResult
        val currentNode: PathNode
        if (isPath(y, x) && !hasNodeLoop(x, y, pathList, pos)) {
            currentNode = PathNode(y, x)
            pathList[pos] = currentNode

            currentNode.parent = father
            currentNode.count = father.count + 1

            if (currentNode.y == endY && currentNode.x == endX) {
                return currentNode
            }

            val lastPos = pos + 1

            result = if (previousAction == LEFT) result
            else DFS(currentNode, y, x + 1, RIGHT, pathList, lastPos, result)

            result = if (previousAction == UP) result
            else DFS(currentNode, y + 1, x, DOWN, pathList, lastPos, result)

            result = if (previousAction == DOWN) result
            else DFS(currentNode, y - 1, x, UP, pathList, lastPos, result)

            result = if (previousAction == RIGHT) result
            else DFS(currentNode, y, x - 1, LEFT, pathList, lastPos, result)

        }
        return result
    }

    private fun hasNodeLoop(x: Int, y: Int, nodeList: Array<PathNode?>, pos: Int): Boolean {
        for (i in 0 until pos) {
            val node = nodeList[i]
            if (y == node!!.y && x == node.x) return true
        }
        return false
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
