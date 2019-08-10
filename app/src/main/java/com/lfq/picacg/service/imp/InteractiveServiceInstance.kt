package com.lfq.picacg.service.imp

import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.service.InteractiveService
import com.lfq.picacg.util.MyCallback
import com.lfq.picacg.util.NetTools
import com.lfq.picacg.util.UserTools

class InteractiveServiceInstance {
    companion object {
        private var service = NetTools.create(InteractiveService::class.java)

        fun getInstance(): InteractiveService? {
            return if (NetTools.isNetWorkAvailableShowMessage()) {
                service
            } else {
                null
            }
        }
    }
}


fun star_manuscript(manuscriptid: Int, netCall: MyNetCall<Int>) {
    InteractiveServiceInstance.getInstance()?.star(manuscriptid, UserTools.name, UserTools.key)
        ?.enqueue(MyCallback(netCall))
}

fun watch_manuscript(manuscriptid: Int, netCall: MyNetCall<Int>) {
    InteractiveServiceInstance.getInstance()?.watch(manuscriptid, UserTools.name, UserTools.key)
        ?.enqueue(MyCallback(netCall))
}