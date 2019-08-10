package com.lfq.picacg.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lfq.picacg.util.MyProgressDialog
import com.lfq.picacg.util.getStorageCard
import com.lfq.picacg.util.toast
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream


/**
 * 图片浏览
 */
class ImageBrowse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.getStringExtra("url")
        val view = View.inflate(this, com.lfq.picacg.R.layout.activity_imagebrowse, null)
        setContentView(view)
        hideBottomUIMenu()
        val image = findViewById<ImageView>(com.lfq.picacg.R.id.image)
        if (url.isNotEmpty()) {
            Glide.with(this)
                .load(url)
                .into(image)
        }
        view.setOnClickListener {
            finish()
        }
        view.setOnLongClickListener {
            AlertDialog.Builder(this)
                .setMessage("是否保存此图片")
                .setNegativeButton("取消", null)
                .setPositiveButton("是") { _, _ ->
                    if (getStorageCard(this)) {
                        saveFile()
                    } else {
                        toast("需要存储卡读写权限")
                    }
                }.show()
            true
        }
    }

    var url = ""

    private fun saveFile() {
        if (url.isNotEmpty()) {
            Thread(SaveNetresToFileRunnable()).start()
        }
    }

    private fun getSaveFile(): File {
        val file = File("/storage/emulated/0", "picacg")
        file.mkdirs()
        return File(file, System.currentTimeMillis().toString() + ".jpg")
    }

    private inner class SaveNetresToFileRunnable : Runnable {
        val dialog = MyProgressDialog(this@ImageBrowse, "下载中...")
        override fun run() {
            val request = Request.Builder()
                .url(url)
                .build()
            val response = OkHttpClient().newCall(request).execute()
            val bytes = response.body?.bytes()
            val file = getSaveFile()
            val out = FileOutputStream(file)
            out.write(bytes)
            out.flush()
            out.close()
            runOnUiThread {
                dialog.dismiss()
                toast("存储路径: " + file.path)
                finish()
            }
        }
    }
}

/**
 * 隐藏虚拟按键，并且全屏
 */
@SuppressLint("ObsoleteSdkInt")
fun Activity.hideBottomUIMenu() {
    //隐藏虚拟按键，并且全屏
    if (Build.VERSION.SDK_INT in 12..18) { // lower api
        val v = window.decorView;
        v.systemUiVisibility = View.GONE;
    } else if (Build.VERSION.SDK_INT >= 19) {
        //for new api versions.
        val v = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            .or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            .or(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) // hide nav bar
            .or(View.SYSTEM_UI_FLAG_FULLSCREEN)// hide status bar
            .or(View.SYSTEM_UI_FLAG_IMMERSIVE)
        v.systemUiVisibility = uiOptions
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
}