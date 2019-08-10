package com.lfq.picacg.view.info

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lfq.picacg.MyApplication
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.UserInfoRequest
import com.lfq.picacg.service.imp.set_data
import com.lfq.picacg.service.imp.set_head
import com.lfq.picacg.service.imp.sign_out
import com.lfq.picacg.util.*
import com.lfq.picacg.view.ImageBrowse
import com.lfq.picacg.view.NameSetActivity
import com.lfq.picacg.view.PasswordSetActivity
import com.lfq.picacg.view.home.fragment.updateMineHeadImage
import com.lfq.picacg.view.home.fragment.updateMineUi
import com.soundcloud.android.crop.Crop
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

const val FinishUserInfo = "FinishUserInfo"

/**
 * 销毁UserInfoActivity
 */
fun finishUserInfoActivity() {
    LocalBroadcastManager.getInstance(MyApplication.context)
        .sendBroadcast(Intent(FinishUserInfo))
}

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinfo)
        val nickname = findViewById<EditText>(R.id.nickname)
        val word = findViewById<EditText>(R.id.word)
        val male = findViewById<RadioButton>(R.id.male)
        val female = findViewById<RadioButton>(R.id.female)
        val unknown = findViewById<RadioButton>(R.id.unknown)
        val check = findViewById<CheckBox>(R.id.checkbox)
        val bar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(bar)
        bar.setNavigationOnClickListener {
            finish()
        }
        //更新头像
        updateHeadImage()
        findViewById<Button>(R.id.sign_out).setOnClickListener {
            sign_out(UserTools.name, UserTools.key)
            UserTools.sign_out()
            UserTools.set("", "", false)
            updateMineUi()
            updateMineHeadImage()
            finish()
        }
        findViewById<Button>(R.id.save).setOnClickListener {
            post(
                nickname.text.toString(),
                word.text.toString(),
                when {
                    male.isChecked -> 1
                    female.isChecked -> 2
                    else -> 0
                }
            )
        }
        check.setOnClickListener {
            UserTools.setAutoSignIn(check.isChecked)
        }
        check.isChecked = UserTools.isAutoSignIn()
        val info = UserTools.userinfo
        nickname.setText(info?.nickname)
        word.setText(info?.word)
        when (info?.sex) {
            1 -> male.isChecked = true
            2 -> female.isChecked = true
            else -> unknown.isChecked = true
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(FinishBroadcast(), IntentFilter(FinishUserInfo))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.usermenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_name -> startActivity(Intent(this, NameSetActivity::class.java))
            R.id.set_password -> startActivity(Intent(this, PasswordSetActivity::class.java))
        }
        return true
    }

    /**
     * 更新头像
     */
    private fun updateHeadImage() {
        val header = findViewById<CircleImageView>(R.id.header)
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_account_circle_black_24dp)
        Glide.with(this)
            .load(head_small_url + UserTools.userinfo?.id)
            .apply(options)
            .listener(MyGlideListener())
            .into(header)
        header.setOnClickListener {
            HeadOpreateDialog(this).show()
        }
    }

    /**
     * 保存修改号的数据
     */
    private fun post(nickname: String, word: String, sex: Int) {
        val dialog = MyProgressDialog(this, "提交数据中...")
        val info = UserInfoRequest(sex, nickname, word)
        set_data(info.toJson(), object : MyNetCall<Int> {
            override fun onRequest(t: Int?) {
                if (t != null) {
                    when (t) {
                        0 -> {
                            UserTools.userinfo = info
                            updateMineUi()
                            toast("修改成功")
                            finish()
                        }
                        else -> toast("修改失败")
                    }
                }
                dialog.dismiss()
            }
        })
    }

    private inner class FinishBroadcast : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) = finish()
    }

    var loadok = false

    /**
     * 加载头像的回调，只做一件事情，记录是否加载成功
     */
    private inner class MyGlideListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            loadok = false
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            loadok = true
            return false
        }
    }

    private inner class HeadOpreateDialog(context: Context) :
        Dialog(context, R.style.MyDialog) {
        init {
            setContentView(R.layout.message1)
            val look = findViewById<Button>(R.id.look)
            if (loadok) {
                look.visibility = View.VISIBLE
                look.setOnClickListener {
                    val intent = Intent(this@UserInfoActivity, ImageBrowse::class.java)
                    intent.putExtra("url", head_source_url + UserTools.userinfo?.id)
                    startActivity(intent)
                    dismiss()
                }
            } else {
                look.visibility = View.GONE
            }
            findViewById<Button>(R.id.upload).setOnClickListener {
                if (getStorageCard(this@UserInfoActivity)) {
                    val intent = Intent(Intent.ACTION_PICK, null)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, 0x1)
                } else {
                    toast("获取存储卡读写权限")
                }
                dismiss()
            }
        }
    }

    /**
     * 从相册获取照片
     */
    var outputFile: File? = null

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //启动剪裁
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            outputFile = File("/storage/emulated/0/picacg/crop", UriToFile(this, data?.data)?.name)
            outputFile?.parentFile?.mkdirs()
            val uri = Uri.fromFile(outputFile)
            Crop.of(data?.data, uri).asSquare().start(this)
        }
        //剪裁成功
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            uploadHeadImage(outputFile!!)
        }
    }

    /**
     * 上传头像
     */
    private fun uploadHeadImage(file: File) {
        val dialog = MyProgressDialog(this, "上传中...")
        set_head(file, object : MyNetCall<Int> {
            override fun onRequest(t: Int?) {
                if (t == 0) {
                    updateHeadImage()
                    updateMineHeadImage()
                } else {
                    toast(t.toString())
                }
                dialog.dismiss()
            }
        })
    }
}
