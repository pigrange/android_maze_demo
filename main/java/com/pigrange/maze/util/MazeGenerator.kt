package com.pigrange.maze.util

import java.util.*

class MazeGenerator(private val size: Int) {

    companion object {
        const val ROAD = 1
    }

    private var maze: Array<IntArray>? = null    //二维数组的迷宫
    private val mazeRef = ArrayList<Node>()    //迷宫的映射列表

    val mazeArray: Array<IntArray>
        get() {
            if (maze == null) {
                maze = Array(size) { IntArray(size) }
                initMazeRef()
                generateMaze()
            }
            return maze as Array<IntArray>
        }

    //上下左右移动
    private val move = arrayOf(Node(-1, 0), Node(0, 1), Node(1, 0), Node(0, -1))

    //选择列表中的一个随机的点
    private val randNode: Node
        get() {
            val temp = rand(mazeRef.size)
            return mazeRef[temp]
        }

    fun refresh() {
        mazeRef.clear()
        maze = null
    }

    //随机得到一个0到t-1的整数
    private fun rand(num: Int): Int {
        return (Math.random() * num).toInt()
    }

    //初始化迷宫的映射列表
    private fun initMazeRef() {
        for (y in 0 until size / 2) {
            for (x in 0 until size / 2) {
                mazeRef.add(Node(y, x))
            }
        }
    }

    //寻找可以移动的方向
    private fun findWay(node: Node): Int {
        val temp = ArrayList<Int>()
        for (i in 0..3) {

            val toX = node.x + move[i].x
            val toY = node.y + move[i].y

            if (toX < 0 || toX >= size / 2 || toY < 0 || toY >= size / 2) {
                continue
            }
            if (maze!![toY * 2 + 1][toX * 2 + 1] == ROAD) {
                continue
            }
            temp.add(i)
        }

        //如果没有可以移动的方向，则判定为死点，将其四周全部打通
        if (temp.isEmpty()) {
            for (i in 0..3) {
                val tempX = node.x * 2 + 1 + move[i].x
                val tempY = node.y * 2 + 1 + move[i].y

                if (tempX > 0 && tempX < size - 1 && tempY > 0 && tempY < size - 1) {
                    maze!![tempY][tempX] = ROAD
                }
            }

            for (i in 0..1) {
                for (j in 0..1) {
                    val tempY = node.x * 2 + 1 + i
                    val tempX = node.y * 2 + 1 + j

                    if (tempY > 0 && tempY < size - 1 && tempX > 0 && tempX < size - 1) {
                        maze!![tempY][tempX] = ROAD
                    }
                }
            }
            return -1
        } else {
            return temp[rand(temp.size)] //返回可走的方向中的随机一个
        }
    }

    //去掉某一个节点
    private fun removeNode(node: Node) {
        var left = -1
        var right = mazeRef.size
        while (right - left > 1) {
            val temp = (left + right) / 2

            //先用二分法找到node的行，再用二分发找到node存在的列
            if (mazeRef[temp].y > node.y || mazeRef[temp].y == node.y && mazeRef[temp].x > node.x) {
                right = temp
            } else {
                left = temp
            }
        }
        mazeRef.removeAt(left)
    }

    //生成迷宫
    private fun generateMaze() {
        while (mazeRef.isNotEmpty()) {

            val t = randNode
            val queue = PriorityQueue<Node>()
            queue.add(t)

            while (!queue.isEmpty()) {
                val tempNode = queue.remove()
                removeNode(tempNode)
                maze!![tempNode.y * 2 + 1][tempNode.x * 2 + 1] = ROAD

                val vocation = findWay(tempNode)
                if (vocation == -1) {
                    break
                }

                val direct = move[vocation]
                val x = tempNode.x * 2 + direct.x + 1
                val y = tempNode.y * 2 + direct.y + 1

                maze!![y][x] = ROAD
                queue.add(Node(tempNode.y + direct.y, tempNode.x + direct.x))
            }
        }
    }


    private class Node internal constructor(internal var y: Int, internal var x: Int)
}
