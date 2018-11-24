package com.pigrange.maze

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_maze.*

class MazeActivity : BaseActivity() {

    private lateinit var holder: ViewHolder
    private lateinit var manager: MazeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)
        initView()
        doBusiness()
    }

    private fun initView() {
        holder = ViewHolder()
        holder.container = findViewById(R.id.activity_main_container)
        holder.fill = findViewById(R.id.activity_main_fill)
        holder.currentMode = maze_status_text

        holder.findWayOut = maze_find_way_out
        holder.removeRoadblock = maze_remove_barrier

        val width = intent.getStringExtra(LaunchActivity.WIDTH).toInt()
        val height = intent.getStringExtra(LaunchActivity.HEIGHT).toInt()
        manager = MazeManager(holder, this)
        manager.initMaze(height, width)
    }

    private fun doBusiness() {

    }

    private fun checkState() {
    }

    private fun hideUI() {
        findViewById<View>(R.id.activity_main_main).systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


    class ViewHolder {
        lateinit var container: FrameLayout
        lateinit var fill: FrameLayout

        lateinit var removeRoadblock: Button

        lateinit var findWayOut: Button
        lateinit var currentMode: TextView
    }

    enum class OperationState {
        WAITING,
        FINISH,
        FINDING_WAY,
        CHOOSING_ORIGIN,
        CHOOSING_TERMINUS,
        REMOVING_BLOCK,
        ADDING_BLOCK
    }
}
