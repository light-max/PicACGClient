package com.lfq.picacg.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

const val REQUEST_WRITE_EXTERNAL_STORAGE = 1

/**
 * 获取存储卡的读写权限
 */
fun getStorageCard(activity: AppCompatActivity): Boolean {
    //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
    return if (ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_EXTERNAL_STORAGE
        )
        false
    } else {
        true
    }
}

fun isStorageCard(activity: AppCompatActivity) =
    ActivityCompat.checkSelfPermission(
        activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED