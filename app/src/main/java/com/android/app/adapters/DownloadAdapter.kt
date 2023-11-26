package com.android.app.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.app.R
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH
import com.android.http.download.DownLoadManager
import com.android.http.download.Download
import com.android.http.download.ProgressListener
import okhttp3.Response

class DownloadAdapter() : BaseRecycleViewAdapter<Download, DownloadAdapter.DlHolder>() {

    private lateinit var mActivity: FragmentActivity

    constructor(mContext: FragmentActivity) : this() {
        this.mActivity = mContext
    }

    private val downLoadManager: DownLoadManager by lazy {
        return@lazy DownLoadManager.Builder()
            .setRepeatDownload(false)
            .bindDownload(mActivity)
            .build()
    }

    class DlHolder(itemView: View) : BaseVH(itemView) {
        val tv_download: Button = itemView.findViewById(R.id.tv_download)
        val tv_cancel: Button = itemView.findViewById(R.id.tv_cancel)
        val tv_current_progress: TextView = itemView.findViewById(R.id.tv_current_progress)
        val progress: ProgressBar = itemView.findViewById(R.id.progress)
    }

    override fun bindHolder(holder: DlHolder, position: Int) {
        val bean = mList[position]
        val tempFileLength = bean.tempFileLength
        val contentLength = bean.contentLength
        val idValue = bean.id

        if ((tempFileLength > 0) && (tempFileLength < contentLength)) {
            holder.tv_download.text = "继续下载"
            val fl = tempFileLength.toFloat() / contentLength * 100
            // 进度条
            holder.progress.progress = fl.toInt()
            // 百分比进度
            holder.tv_current_progress.text = fl.toString()
        }

        holder.tv_download.setOnClickListener {
            downLoadManager.download(bean, object : ProgressListener {
                override fun onStart(id: String?, contentLength: Long) {
                    if (TextUtils.equals(id, idValue)) {
                        holder.tv_download.text = "下载中"
                    }
                }

                override fun onProgress(id: String, progress: Long, contentLength: Long, percentage: String) {
                    if (TextUtils.equals(id, idValue)) {
                        bean.contentLength = contentLength
                        bean.tempFileLength = progress

                        holder.progress.progress = progress.toInt()
                        holder.progress.max = contentLength.toInt()
                        holder.tv_current_progress.text = percentage
                    }
                }

                override fun onError(id: String?, throwable: Throwable?) {
                    if (TextUtils.equals(id, idValue)) {
                        holder.tv_download.text = "开始下载"
                    }
                }

                override fun onComplete(id: String?, path: String?, response: Response?) {
                    if (TextUtils.equals(id, idValue)) {
                        holder.tv_download.text = "下载完成"
                    }
                }
            })
            mItemClickListener?.onItemClick(holder.tv_download, position, bean)
        }

        holder.tv_cancel.setOnClickListener {
            // 取消下载
            downLoadManager.cancel(bean.id)
            mItemClickListener?.onItemClick(holder.tv_cancel, position, bean)
        }

        holder.itemView.setOnClickListener {
            val bindingAdapterPosition = holder.bindingAdapterPosition
            mItemClickListener?.onItemClick(holder.itemView, bindingAdapterPosition, mList[bindingAdapterPosition])
        }
    }

    override fun createVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): DlHolder {
        return DlHolder(inflater.inflate(R.layout.item_download, parent, false))
    }
}