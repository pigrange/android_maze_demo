package com.pigrange.maze.data

class PathNode constructor(internal var y: Int, internal var x: Int) {

    var count: Int = 0
    var F = Integer.MAX_VALUE
    var parent: PathNode? = null
    var timeCost: Long = 0
}
