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

    companion object {
        const val HEIGHT = "height"
        const val WIDTH = "width"
    }

    lateinit var imm: InputMethodManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        edit_height_wrapper.setOnClickListener {
            edit_height.isFocusable = true
            edit_height.isFocusableInTouchMode = true
            edit_height.requestFocus()
            imm.showSoftInput(edit_height, 0)
        }
        edit_width_wrapper.setOnClickListener {
            edit_width.isFocusable = true
            edit_width.isFocusableInTouchMode = true
            edit_width.requestFocus()
            imm.showSoftInput(edit_width, 0)
        }

        luanch_generate_maze.setOnClickListener {
            val height = edit_height.text.toString()
            val width = edit_width.text.toString()

            if (height == "" || width == "") {
                showToast("请输入正确的尺寸")
                return@setOnClickListener
            }

            val intent = Intent(this, MazeActivity::class.java)
            intent.putExtra(HEIGHT, height)
            intent.putExtra(WIDTH, width)
            startActivity(intent)
        }
    }


    override fun onBackPressed() {
        if (isBackExist) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
                System.exit(0)
            }
        }
    }
}
