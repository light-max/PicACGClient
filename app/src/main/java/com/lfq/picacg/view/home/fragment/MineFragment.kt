package com.lfq.picacg.view.home.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lfq.picacg.MyApplication
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SignInRequest
import com.lfq.picacg.service.imp.sign_in
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.head_small_url
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.info.AuthorInfoActivity
import com.lfq.picacg.view.info.UserInfoActivity
import com.lfq.picacg.view.login.LoginActivity
import de.hdodenhof.circleimageview.CircleImageView

const val UpdateNameAndNickName = "UpdateNameAndNickName"
const val UpdateHeadImage = "UpdateHeadImage"

fun updateMineUi() {
    LocalBroadcastManager
        .getInstance(MyApplication.context)
        .sendBroadcast(Intent(UpdateNameAndNickName))
}

fun updateMineHeadImage() {
    LocalBroadcastManager
        .getInstance(MyApplication.context)
        .sendBroadcast(Intent(UpdateHeadImage))
}

class MineFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        head = view.findViewById(R.id.head)
        nickname = view.findViewById(R.id.nickname)
        username = view.findViewById(R.id.username)
        look_submission = view.findViewById(R.id.look_submission)
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            sign_in(UserTools.name, UserTools.password, object : MyNetCall<SignInRequest> {
                override fun onRequest(t: SignInRequest?) {
                    if (t?.code == 0) {
                        UserTools.key = t.key
                        setUserInfo()
                        setUesrHeadImage()
                    } else {
                        toast("刷新失败")
                    }
                    swipe.isRefreshing = false
                }
            })
        }
        setUserInfo()
        setUesrHeadImage()
        registeredBroadcasts()
    }

    lateinit var head: CircleImageView
    lateinit var nickname: TextView
    lateinit var username: TextView
    lateinit var look_submission: TextView

    private fun setUserInfo() {
        if (UserTools.key != 0L) {
            nickname.text = UserTools.userinfo?.nickname
            username.text = UserTools.name
            look_submission.visibility = View.VISIBLE
            head.setOnClickListener {
                startActivity(Intent(activity, UserInfoActivity::class.java))
            }
            look_submission.setOnClickListener {
                val intent = Intent(context, AuthorInfoActivity::class.java)
                intent.putExtra(AuthorInfoActivity.AUTHOR_ID, UserTools.userinfo?.id)
                context?.startActivity(intent)
            }
        } else {
            nickname.text = "点击头像登录"
            username.text = ""
            look_submission.visibility = View.GONE
            head.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }

    private fun setUesrHeadImage() {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_account_circle_black_24dp)
        Glide.with(context!!)
            .load(head_small_url + UserTools.userinfo?.id)
            .apply(options)
            .into(head)
    }

    inner class UpdateUi : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                UpdateNameAndNickName -> setUserInfo()
                UpdateHeadImage -> setUesrHeadImage()
            }
        }
    }

    private fun registeredBroadcasts() {
        LocalBroadcastManager.getInstance(activity!!)
            .registerReceiver(UpdateUi(), IntentFilter(UpdateNameAndNickName))
        LocalBroadcastManager.getInstance(activity!!)
            .registerReceiver(UpdateUi(), IntentFilter(UpdateHeadImage))
    }
}