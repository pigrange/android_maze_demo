package com.pigrange.maze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : BaseActivity() {
    private var isBackExist: Boolean = true
    private var exitTime = 0L
    lateinit var imm: InputMethodManager

    companion object {
        const val SIZE = "size"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        edit_Size_wrapper.setOnClickListener {
            edit_cellSize.isFocusable = true
            edit_cellSize.isFocusableInTouchMode = true
            edit_cellSize.requestFocus()
            imm.showSoftInput(edit_cellSize, 0)
        }

        launch_generate_maze.setOnClickListener {
            val size = edit_cellSize.text.toString()

            if (size == "") {
                showToast("请输入正确的尺寸")
                return@setOnClickListener
            }
            if (size.toInt() > 80) {
                showToast("性能有限，不建议创建尺寸大于80的迷宫")
                return@setOnClickListener
            }
            if (size.toInt() < 10) {
                showToast("这样做是不方便创建迷宫的")
                return@setOnClickListener
            }

            val intent = Intent(this, MazeActivity::class.java)
            intent.putExtra(SIZE, size)
            startActivity(intent)
            edit_cellSize.setText("")
        }
    }

    override fun onBackPressed() {
        if (isBackExist) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
        }
    }
}
