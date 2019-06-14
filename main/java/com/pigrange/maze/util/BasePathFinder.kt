package com.pigrange.maze.util

import com.pigrange.maze.data.PathNode

interface BasePathFinder {
    fun findPath(callBack: PathFindCallBack)
}

interface PathFindCallBack {
    fun notify(pathNode: PathNode?, style: Int)
}

