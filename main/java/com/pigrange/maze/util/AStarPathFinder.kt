package com.pigrange.maze.util

import android.os.Handler
import android.os.HandlerThread
import com.pigrange.maze.data.PathNode
import com.pigrange.maze.util.MazeManager.Companion.STYLE_A_STAR
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 */
class AStarPathFinder(private val map: Array<IntArray>, private val startY: Int, private val startX: Int, private val endY: Int, private val endX: Int) : BasePathFinder {
    private val mHandlerThread = HandlerThread("ASTAR").apply { start() }
    private val mHandler = Handler(mHandlerThread.looper)

    private val closeRouteMap = Array(endY + 2) { Array(endX + 2) { false } }
    private val openRouteMap = Array(endY + 2) { Array(endX + 2) { false } }

    override fun findPath(callBack: PathFindCallBack) {
        mHandler.post {
            var endNode: PathNode? = null
            val startNode = PathNode(startY, startX)
            val openList = ArrayList<PathNode>()
//            val openList = PriorityQueue<PathNode>(Comparator<PathNode> { o1, o2 -> o2.F - o1.F })
            val closeList = ArrayList<PathNode>()
            openList.add(startNode)

            var tryCount = 0

            val startTime = System.currentTimeMillis()

            while (openList.isNotEmpty()) {
                tryCount++
                val curMinFNode = getMinFPathNode(openList)
                openList.remove(curMinFNode)
                closeList.add(curMinFNode)
                if (curMinFNode.y == endY && curMinFNode.x == endX) {
                    endNode = curMinFNode
                    endNode.count = tryCount
                    endNode.timeCost = System.currentTimeMillis() - startTime
                    break
                } else {
                    handleChildNode(curMinFNode, curMinFNode.y, curMinFNode.x + 1, openList)
                    handleChildNode(curMinFNode, curMinFNode.y + 1, curMinFNode.x, openList)
                    handleChildNode(curMinFNode, curMinFNode.y, curMinFNode.x - 1, openList)
                    handleChildNode(curMinFNode, curMinFNode.y - 1, curMinFNode.x, openList)
                }
            }
            callBack.notify(endNode, STYLE_A_STAR)
        }
    }

    private fun handleChildNode(curMinFNode: PathNode, y: Int, x: Int, openList: ArrayList<PathNode>) {
        val child: PathNode
        if (isPath(y, x)) {
            child = PathNode(y, x)
            if (!isExistList(child, closeRouteMap) && !isExistList(child, openRouteMap)) {
                child.F = getF(y, x)
                child.parent = curMinFNode
                openList.add(child)
                openRouteMap[y][x] = true
            }
        }
    }

    private fun handleChildNode(curMinFNode: PathNode, y: Int, x: Int, openList: PriorityQueue<PathNode>) {
        val child: PathNode
        if (isPath(y, x)) {
            child = PathNode(y, x)
            if (!isExistList(child, closeRouteMap) && !isExistList(child, openRouteMap)) {
                child.F = getF(y, x)
                child.parent = curMinFNode
                openList.add(child)
                openRouteMap[y][x] = true
            }
        }
    }

    private fun getF(y: Int, x: Int): Int {
        val g = Math.abs(startX - y) + Math.abs(startX - x)
        val h = Math.abs(endX - y) + Math.abs(endY - x)
        return g + h
    }

    private fun isExistList(child: PathNode, routeMap: Array<Array<Boolean>>): Boolean {
        return routeMap[child.y][child.x]
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

    private fun getMinFPathNode(openList: ArrayList<PathNode>): PathNode {
        var pos = 0
        var minF = openList[pos].F
        for (i in openList.indices) {
            if (openList[i].F < minF) {
                minF = openList[i].F
                pos = i
            }
        }
        return openList[pos]
    }

    private fun getMinFPathNode(openList: PriorityQueue<PathNode>): PathNode {
        return openList.poll()
    }
}
