package com.m2f.sliidetest.SliideTest.helpers

import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

fun <T : Any> T.readFile(fileName: String): String {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("service-response/$fileName")
        val source = inputStream.source()
        val buffer = source.buffer()
        return buffer.readString(StandardCharsets.UTF_8)
    }