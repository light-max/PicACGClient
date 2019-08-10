package com.lfq.picacg.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.data.SubmissionRequest
import com.lfq.picacg.service.imp.update
import com.lfq.picacg.util.toast

class UpdateSubmission : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bean = intent.getParcelableExtra(DATA)
        if (bean.id == 0) {
            finish()
            return
        }
        setContentView(R.layout.activity_submission)
        findViewById<RecyclerView>(R.id.images).visibility = View.GONE
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "修改信息"
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val title = findViewById<EditText>(R.id.title)
        val keyword = findViewById<EditText>(R.id.keyword)
        val introduction = findViewById<EditText>(R.id.introduction)
        findViewById<Button>(R.id.post).setOnClickListener {
            post(title.text.toString(), keyword.text.toString(), introduction.text.toString())
        }
        title.setText(bean.title)
        keyword.setText(bean.keyword)
        introduction.setText(bean.introduction)
    }

    lateinit var bean: ContentRequest.ContentBean

    companion object {
        const val DATA = "SUBMISSION_DATA"
    }

    fun post(title: String, keyword: String, introduction: String) {
        update(bean.id, title, keyword, introduction, object : MyNetCall<SubmissionRequest> {
            override fun onRequest(t: SubmissionRequest?) {
                if (t != null) {
                    if (t.code == 0) {
                        val result = Intent()
                        result.putExtra("title", title)
                        result.putExtra("keyword", keyword)
                        result.putExtra("introduction", introduction)
                        result.putExtra("position", intent.getIntExtra("position", 0))
                        setResult(Activity.RESULT_OK, result)
                        finish()
                    }
                    toast(t.message)
                }
            }
        })
    }
}