package com.lfq.picacg.util

import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.FragmentActivity

class MyProgressDialog : ProgressDialog {
    constructor(context: FragmentActivity?) : super(context)
    constructor(context: Context) : super(context)
    constructor(context: FragmentActivity?, title: String) : super(context) {
        super.setTitle(title)
        super.setCancelable(false)
        super.show()
    }
}