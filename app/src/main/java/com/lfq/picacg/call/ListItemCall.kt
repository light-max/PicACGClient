package com.lfq.picacg.call;

import android.view.View
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.data.SearchRequest

/**
 * 这些接口的参数都差不多，甚至有一样的
 */
class ListItemCall {
    /**
     * 点击图片的回调
     */
    interface OnImageClickListener {
        fun onImageClick(bean: ContentRequest.ContentBean, position: Int)
    }

    /**
     * 点击头像的回调
     */
    interface OnHeadClickListener {
        fun onHeadClick(bean: ContentRequest.ContentBean)
    }

    /**
     * 点赞的回调
     */
    interface OnStarClickListener {
        fun onStarClick(bean: ContentRequest.ContentBean, position: Int)
    }

    /**
     * 更多按钮回调
     */
    interface OnMoreClickListener {
        fun onMoreClick(parent: View, bean: ContentRequest.ContentBean, position: Int)
    }

    /**
     * 搜索到的资源的回调
     */
    interface OnSearchResClickListener {
        fun onnResClick(bean: SearchRequest.SearchBean)
    }
}
