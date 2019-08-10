package com.lfq.picacg.service.imp

import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SubmissionRequest
import com.lfq.picacg.service.SubmissionService
import com.lfq.picacg.service.imp.SubmissionServiceImp.Companion.getInstance
import com.lfq.picacg.util.MyCallback
import com.lfq.picacg.util.NetTools
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.getFileRequest
import java.io.File


class SubmissionServiceImp {
    companion object {
        private var service = NetTools.create(SubmissionService::class.java)

        fun getInstance(): SubmissionService? {
            return if (NetTools.isNetWorkAvailableShowMessage()) {
                service
            } else {
                null
            }
        }
    }
}

fun upload(
    title: String,
    keyword: String,
    introduction: String,
    files: List<File>,
    netCall: MyNetCall<SubmissionRequest>
) {
    getInstance()?.upload(
        UserTools.name, UserTools.key,
        title, keyword, introduction,
        getFileRequest(files)
    )?.enqueue(MyCallback(netCall))
}

fun detele_submission(submission_id: Int, netCall: MyNetCall<Int>) {
    getInstance()?.delete(UserTools.name, UserTools.key, submission_id)?.enqueue(MyCallback(netCall))
}

fun update(id: Int, title: String, keyword: String, introduction: String, netCall: MyNetCall<SubmissionRequest>) {
    getInstance()?.update(UserTools.name, UserTools.key, id, title, keyword, introduction)?.enqueue(MyCallback(netCall))
}