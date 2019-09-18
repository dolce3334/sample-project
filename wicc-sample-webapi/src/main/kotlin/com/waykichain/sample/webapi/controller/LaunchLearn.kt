package com.waykichain.sample.webapi.controller

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

/**
 * @ClassName: LaunchLearn
 * @Created by ch
 * @Description: 协程练习测试
 * @Date: 2019/8/24 17:23
 */
fun test1() {
    println("Start")

    // 启动一个协程
    GlobalScope.launch {
        delay(1000L)
        println("Hello")
    }

    Thread.sleep(2000) // 等待 2 秒钟
    println("Stop")
}

fun test2() {
    val c = AtomicLong()

    for (i in 1..1_000_000L)
        thread(start = true) {
            c.addAndGet(i)
        }

    println(c.get())
}

fun test3() {
    val c = AtomicLong()

    for (i in 1..1_000_000L)
        GlobalScope.launch {
            c.addAndGet(i)
        }

    println(c.get())
}

fun test4() {
    val deferred = (1..1_000_000).map { n ->
        GlobalScope.async {
            delay(1000)
            n
        }
    }

    runBlocking {
        val sum = deferred.sumBy { it.await() }
        println("Sum: $sum")
    }
}

suspend fun workload(n: Int): Int {
    delay(1000)
    return n
}

fun test5() {
    val deferred = (1..1_000_000).map { n ->
        GlobalScope.async {
            workload(n)
        }
    }
}



fun main(args: Array<String>) {
    test5()
}