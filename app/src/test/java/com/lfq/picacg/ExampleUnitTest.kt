package com.lfq.picacg

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

var size = 0
var lines = 0

fun main() {
    f(File("E:\\Android\\PicACG Android Client\\app\\src\\main\\java\\com\\lfq\\picacg"))
    println(size)
    println(lines)
}

fun f(file: File) {
    if (file.isFile) {
        val list = file.readLines()
        list.forEach { t -> size += t.length }
        lines += list.size
    } else {
        file.listFiles().forEach { t -> f(t) }
    }
}
