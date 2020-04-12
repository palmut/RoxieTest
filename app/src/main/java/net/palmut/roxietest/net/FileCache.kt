package net.palmut.roxietest.net

import android.content.Context
import java.io.File
import java.io.InputStream

class FileCache(private val context: Context) : CacheInterface {

    override fun put(key: String, stream: InputStream) {
        File(context.cacheDir, key).outputStream().use {
            do {
                val nextByte = stream.read()
                it.write(nextByte)
            } while (nextByte != -1)
        }
    }

    override fun get(key: String): InputStream? {
        val file = File(context.cacheDir, key)
        if (file.exists()) {
            if (System.currentTimeMillis() - file.lastModified() < TTL_millis) {
                return file.inputStream()
            } else {
                file.delete()
            }
        }
        return null
    }

    companion object {
        private const val TTL_millis = 10 * 60 * 1000L // 10 minutes
    }
}

interface CacheInterface {
    fun put(key: String, stream: InputStream)
    fun get(key: String): InputStream?
}