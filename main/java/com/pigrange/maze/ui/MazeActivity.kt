package com.pigrange.maze.ui

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.pigrange.maze.R
import com.pigrange.maze.customView.MazeView
import com.pigrange.maze.util.MazeManager
import kotlinx.android.synthetic.main.activity_maze.*

class MazeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)
        initView()
    }

    private fun initView() {
        var cellCount = intent.getStringExtra(LaunchActivity.SIZE).toInt()
        if (cellCount % 2 == 0) cellCount += 1

        val holder = ViewHolder()
        holder.container = maze_container
        holder.footer = maze_footer
        holder.mFill = maze_fill
        holder.currentMode = maze_status_text
        holder.aStarFindWayOut = A_STAR_find_way_out
        holder.dFSFindWayOut = DFS_find_way_out
        holder.refresh = maze_refresh
        holder.bfsFindWayOut = BFS_find_way_out
        holder.steps = maze_steps

        val manager = MazeManager(holder, this)
        manager.changeContainerSize()
        manager.initMaze(cellCount)
    }

    class ViewHolder {
        lateinit var container: FrameLayout
        lateinit var mFill: MazeView
        lateinit var footer: ConstraintLayout
        lateinit var refresh: Button
        lateinit var aStarFindWayOut: Button
        lateinit var dFSFindWayOut: Button
        lateinit var currentMode: TextView
        lateinit var bfsFindWayOut: Button
        lateinit var steps: TextView
    }
}
