package com.lfq.picacg.view.submission

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SubmissionRequest
import com.lfq.picacg.service.imp.upload
import com.lfq.picacg.util.*
import com.lfq.picacg.view.login.LoginActivity
import java.io.File


class SubmissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission)
        isSignin()
        findViewById<Toolbar>(R.id.toolbar)
            .setNavigationOnClickListener {
                finish()
            }
        val images = findViewById<RecyclerView>(R.id.images)
        val title = findViewById<EditText>(R.id.title)
        val keyword = findViewById<EditText>(R.id.keyword)
        val introduction = findViewById<EditText>(R.id.introduction)
        findViewById<Button>(R.id.post)
            .setOnClickListener {
                if (isSignin()) {
                    post(title.text.toString(), keyword.text.toString(), introduction.text.toString(), adapter.list)
                }
            }
        images.adapter = adapter
        images.layoutManager = GridLayoutManager(this, 3)
    }

    private val adapter = ImagesAdapter(View.OnClickListener {
        if (isStorageCard(this)) {
            val intent = Intent(Intent.ACTION_PICK, null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, 0x1)
        } else {
            AlertDialog.Builder(this)
                .setMessage("投稿功能需要访问你的照片，是否开启权限")
                .setNegativeButton("我不投了") { _, _ -> finish() }
                .setPositiveButton("去开启") { _, _ -> getStorageCard(this) }
                .show()
        }
    })

    /**
     * 提交投稿请求
     */
    private fun post(title: String, keyword: String, introduction: String, files: List<File>) {
        if (files.isEmpty()) {
            toast("至少要添加一张图片")
            return
        }
        val dialog = MyProgressDialog(this, "上传中...")
        upload(title, keyword, introduction, files, object : MyNetCall<SubmissionRequest> {
            override fun onRequest(t: SubmissionRequest?) {
                if (t != null) {
                    if (t.code == 0) {
                        finish()
                    }
                    toast(t.message)
                }
                dialog.dismiss()
            }
        })
    }

    /**
     * 检查用户是否已登录
     */
    private fun isSignin(): Boolean {
        if (UserTools.key == 0L) {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("投稿功能要登录才能使用哦")
                .setNegativeButton("取消", null)
                .setPositiveButton("去登录") { _, _ ->
                    startActivity(Intent(this, LoginActivity::class.java))
                }.show()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            val file = UriToFile(this, data?.data)
            if (file != null) {
                adapter.list.remove(file)
                adapter.list.add(file)
                adapter.notifyDataSetChanged()
            }
        }
    }
}

