package com.android.example.livedatabuilder

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*

class BaseFoundationActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this))
    }

    override fun onResume() {
        super.onResume()
//        coroutinesBase1()
//        coroutinesBase2()
//        coroutinesBase3_1()
//        coroutinesBase3_2()
//        coroutinesBase3_3_m()
//        coroutinesBase5_m_1()
//        coroutinesBase5_m_2()

    }

    /**
     * 1.创建一个最简单的协程
     */
    private fun coroutinesBase1() {
        //
        LogUtils.i("coroutinesBase1: main 1 ${Thread.currentThread().name}")

        GlobalScope.launch {
            for (i in 1..10) {
                LogUtils.i("coroutinesBase1: GlobalScope launch $i ${Thread.currentThread().name}")
            }
        }

        LogUtils.i("coroutinesBase1: main 2 ${Thread.currentThread().name}")

        lifecycleScope.launch() {
            for (i in 1..10) {
                LogUtils.i("coroutinesBase1: lifecycleScope launch $i ${Thread.currentThread().name}")
            }
        }
        LogUtils.i("coroutinesBase1: main 3 ${Thread.currentThread().name}")

        lifecycleScope.async {
            for (i in 1..10) {
                LogUtils.i("coroutinesBase1: lifecycleScope async $i ${Thread.currentThread().name}")

            }
        }

        LogUtils.i("coroutinesBase1: main end ${Thread.currentThread().name}")
    }

    /**
     * 2.怎样为协程指定线程
     */
    private fun coroutinesBase2() {

        LogUtils.i("coroutinesBase2: main 1 ${Thread.currentThread().name}")

        lifecycleScope.launch(Dispatchers.IO) {
            for (i in 1..10) {
                LogUtils.i("coroutinesBase2: launch io $i ${Thread.currentThread().name}")
            }
        }

        LogUtils.i("coroutinesBase2: main end ${Thread.currentThread().name}")
    }

    /**
     * 3.怎样切换线程 3_1
     */
    private fun coroutinesBase3_1() {
        //不要这回调地狱
        lifecycleScope.launch(Dispatchers.Main) {
            LogUtils.i("coroutinesBase3_1: 1 ${Thread.currentThread().name}")
            launch(Dispatchers.IO) {
                LogUtils.i("coroutinesBase3_1: 2 ${Thread.currentThread().name}")
                launch(Dispatchers.Main) {
                    LogUtils.i("coroutinesBase3_1: 3 ${Thread.currentThread().name}")
                    launch(Dispatchers.IO) {
                        LogUtils.i("coroutinesBase3_1: 4 ${Thread.currentThread().name}")

                    }
                }
            }
        }
    }

    /**
     * 3.怎样切换线程 3_2
     */
    private fun coroutinesBase3_2() {
        lifecycleScope.launch {
            LogUtils.i("coroutinesBase3_2: 1 ${Thread.currentThread().name}")
            withContext(Dispatchers.IO) {
                LogUtils.i("coroutinesBase3_2: 2 ${Thread.currentThread().name}")
            }
            LogUtils.i("coroutinesBase3_2: 3 ${Thread.currentThread().name}")
            withContext(Dispatchers.IO) {
                LogUtils.i("coroutinesBase3_2: 4 ${Thread.currentThread().name}")
            }
        }
    }

    /**
     * 3.怎样切换线程 3_3_m
     */
    private fun coroutinesBase3_3_m() {
        lifecycleScope.launch {
            for (i in 1..10) {
                if (i % 2 == 1) {
                    LogUtils.i("coroutinesBase3_3_m: $i ${Thread.currentThread().name}")
                    coroutinesBase3_3_sub(i + 1)
                }
            }
        }
    }


    /**
     * 3.怎样切换线程 3_3_sub
     */
    private suspend fun coroutinesBase3_3_sub(i: Int) {
        withContext(Dispatchers.IO) {
            LogUtils.i("coroutinesBase3_3_sub: $i ${Thread.currentThread().name}")
        }
    }


    /**
     * 4.为什么要 suspend 修饰
     */
    private suspend fun coroutinesBase4() {
        LogUtils.i("coroutinesBase4: ${Thread.currentThread().name}")
    }

    /**
     * 5.挂起函数怎样返回数据 m_1
     */
    private fun coroutinesBase5_m_1() {
        lifecycleScope.launch {
            val deferred = async { coroutinesBase5_sub(1) }
            runCatching { coroutinesBase5_sub(2) }
            LogUtils.i("coroutinesBase5_m: ${Thread.currentThread().name} ${deferred.await()}")
        }
    }

    /**
     * 5.挂起函数怎样返回数据 sub
     */
    private suspend fun coroutinesBase5_sub(i: Int): String {
        return withContext(Dispatchers.IO) {
            LogUtils.i("coroutinesBase5_sub: ${Thread.currentThread().name} $i")
//            while (true){
//                delay(1000)
//                LogUtils.i("coroutinesBase5_sub: ${Thread.currentThread().name} $i 我在运行")
//            }
            return@withContext "我是${i}号"
        }
    }

    /**
     * 5.挂起函数怎样返回数据 m_2
     */
    private fun coroutinesBase5_m_2() {
        lifecycleScope.launch {

            val deferred1 = async { coroutinesBase5_sub(1) }
            val deferred2 = async { coroutinesBase5_sub(2) }
            val deferred3 = async { coroutinesBase5_sub(3) }
            val deferred4 = async { coroutinesBase5_sub(4) }
            LogUtils.i("coroutinesBase5_m_2: ${deferred1.await()},${deferred2.await()},${deferred3.await()},${deferred4.await()}")
        }
    }

    private fun coroutinesBase6() {
        val job = Job()
        val supervisorJob = SupervisorJob()
        job.cancel()//JobImpl
        supervisorJob.cancelChildren()
        lifecycleScope.launch {
            delay(1000)
            yield()// ensureActive()

        }
    }


}