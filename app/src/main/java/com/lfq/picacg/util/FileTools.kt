package com.lfq.picacg.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File

fun UriToFile(context: Context, uri: Uri?): File? {
    return try {
        val cursor = context.contentResolver.query(uri!!, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        val i = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        File(cursor?.getString(i!!))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
