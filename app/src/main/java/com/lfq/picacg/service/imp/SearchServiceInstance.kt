package com.lfq.picacg.service.imp

import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SearchRequest
import com.lfq.picacg.service.SearchService
import com.lfq.picacg.util.MyCallback
import com.lfq.picacg.util.NetTools

class SearchServiceInstance {
    companion object {
        private var service = NetTools.create(SearchService::class.java)

        fun getInstance(): SearchService? {
            return if (NetTools.isNetWorkAvailableShowMessage()) {
                service
            } else {
                null
            }
        }
    }
}

fun search(value: String, netCall: MyNetCall<SearchRequest>) {
    SearchServiceInstance.getInstance()?.search(value)?.enqueue(MyCallback(netCall))
}