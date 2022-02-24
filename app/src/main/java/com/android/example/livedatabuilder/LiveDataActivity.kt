/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.example.livedatabuilder

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.android.example.livedata.R
import com.android.example.livedata.databinding.ActivityLivedataBinding
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*

class LiveDataActivity : AppCompatActivity() {

    // Obtain ViewModel
    private val viewmodel: LiveDataViewModel by viewModels { LiveDataVMFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain binding object using the Data Binding library
        val binding = DataBindingUtil.setContentView<ActivityLivedataBinding>(
            this, R.layout.activity_livedata
        )

        // Set the LifecycleOwner to be able to observe LiveData objects
        binding.lifecycleOwner = this

        // Bind ViewModel
        binding.viewmodel = viewmodel
    }

    override fun onResume() {
        super.onResume()
        Log.i("test", "onResume: 123")
        coroutinesBase1()
        coroutinesBase2()
        coroutinesBase3_1()
        coroutinesBase3_2()
        coroutinesBase3_3_m()
        coroutinesBase5()


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


    private suspend fun coroutinesBase4() {
        LogUtils.i("coroutinesBase4: ${Thread.currentThread().name}")
    }

    private fun coroutinesBase5() {
    }


    /**
     * 1.创建一个最简单的协程 1_1
     */
    fun coroutinesBase1_1() {
        //
        LogUtils.i("coroutinesBase1: main 1")
        val deferred = lifecycleScope.async {
            for (i in 1..100) {
                LogUtils.i("coroutinesBase1: launch $i")

            }
            return@async 101
        }

        LogUtils.i("coroutinesBase1: main 2")
//        LogUtils.i("coroutinesBase1: main ${deferred.}")

    }


}


