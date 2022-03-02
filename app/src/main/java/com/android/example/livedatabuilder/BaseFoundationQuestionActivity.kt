package com.android.example.livedatabuilder

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*

class BaseFoundationQuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this))
    }

    override fun onResume() {
        super.onResume()
        coroutinesQuestion1_1()
        coroutinesQuestion1_2()


    }

    /**
     * 以下两个函数都是为了证明 协程与协程之间都是并行的，挂起函数与挂起函数之间是串行的。
     */
    private fun coroutinesQuestion1_1() {
        lifecycleScope.launch {
            launch {
                while (true) {
                    delay(5000)
                    LogUtils.i("coroutinesQuestion1_1")
                }

            }
            launch {
                while (true) {
                    delay(5000)
                    LogUtils.i("coroutinesQuestion1_2")
                }
            }
        }
    }

    private fun coroutinesQuestion1_2() {
        lifecycleScope.launch {
            val job = async {
                coroutinesQuestion1_2_mockNetData(1)
                coroutinesQuestion1_2_mockNetData(2)
                coroutinesQuestion1_2_mockNetData(3)
                coroutinesQuestion1_2_mockNetData(4)
            }
            LogUtils.i("coroutinesQuestion1_2 ${job.await()}")

        }
    }


    private suspend fun coroutinesQuestion1_2_mockNetData(id: Int): String {
        return withContext(Dispatchers.Default) {
            LogUtils.i("coroutinesQuestion1_2 mockNetData source $id")
            return@withContext "mockNetData return $id"
        }
    }



}