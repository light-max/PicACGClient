package com.lfq.picacg.service.imp

import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.service.ContentService
import com.lfq.picacg.util.MyCallback
import com.lfq.picacg.util.NetTools
import org.json.JSONArray

class ContentServiceInstance {
    companion object {
        private var service = NetTools.create(ContentService::class.java)

        fun getInstance(): ContentService? {
            return if (NetTools.isNetWorkAvailableShowMessage()) {
                service
            } else {
                null
            }
        }
    }
}

fun rand_content(netCall: MyNetCall<ContentRequest>) {
    ContentServiceInstance.getInstance()?.rand(6)?.enqueue(MyCallback(netCall))
}

fun get_content(id: JSONArray, netCall: MyNetCall<ContentRequest>) {
    ContentServiceInstance.getInstance()?.get(4, id)?.enqueue(MyCallback(netCall))
}

fun get_sort(type: String, lastid: Int, netCall: MyNetCall<ContentRequest>) {
    ContentServiceInstance.getInstance()?.sort(type, 8, lastid)?.enqueue(MyCallback(netCall))
}

fun get_content(id: Int, lastid: Int, netCall: MyNetCall<ContentRequest>) {
    ContentServiceInstance.getInstance()?.get(id, 8, lastid)?.enqueue(MyCallback(netCall))
}

fun get_content(id: Int, netCall: MyNetCall<ContentRequest.ContentBean>) {
    ContentServiceInstance.getInstance()?.get(id)?.enqueue(MyCallback(netCall))
}