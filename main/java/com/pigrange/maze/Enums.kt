package com.pigrange.maze

import com.pigrange.maze.Grid.GridState
import java.util.Random

/**
 * Created by CDFAE1CC on 2016.12.16.
 * 工具类，用于从枚举类中随机选取枚举值
 */
object Enums {

    /**
     * 初始化随机数种子
     */
    private val rand = Random(System.currentTimeMillis())

    fun <T : Enum<T>> random(ec: Class<T>): T {
        return random(ec.enumConstants)
    }

    private fun <T> random(values: Array<T>): T {
        val value: T = values[rand.nextInt(values.size)]
        return if (value == GridState.WAY) random(values) else value
    }
}
