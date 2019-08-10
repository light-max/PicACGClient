package com.lfq.picacg.util;

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil


// 一些乱七八糟的功能函数都写在这里


var toast: Toast? = null

fun toast(message: String) {
    toast?.setText(message)
    toast?.show()
}

fun getFileRequest(files: List<File>): List<MultipartBody.Part> {
    val list = ArrayList<MultipartBody.Part>()
    for (file in files) {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        list.add(part)
    }
    return list
}

fun createBitmap(byteArray: ByteArray?): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
}

fun decodeBitmap(byteArray: ByteArray?, width: Int, height: Int): Bitmap {
    val op = BitmapFactory.Options()
    // inJustDecodeBounds如果设置为true,仅仅返回图片实际的宽和高,宽和高是赋值给opts.outWidth,opts.outHeight;
    op.inJustDecodeBounds = true
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size, op) //获取尺寸信息
    //获取比例大小
    val wRatio = ceil((op.outWidth / width).toDouble()).toInt()
    val hRatio = ceil((op.outHeight / height).toDouble()).toInt()
    //如果超出指定大小，则缩小相应的比例
    if (wRatio > 1 && hRatio > 1) {
        if (wRatio > hRatio) {
            op.inSampleSize = wRatio
        } else {
            op.inSampleSize = hRatio
        }
    }
    op.inJustDecodeBounds = false
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, op)
}

@SuppressLint("SimpleDateFormat")
fun getDateTime(time: Long): String {
    val formatter = SimpleDateFormat("yyyy/M/d HH:mm")
    return formatter.format(Date(time))
}