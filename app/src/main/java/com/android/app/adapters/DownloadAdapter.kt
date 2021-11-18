package com.android.app.adapters

import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.app.R
import com.android.helper.base.BaseVH
import com.android.helper.base.recycleview.BaseRecycleAdapter
import com.android.helper.interfaces.listener.ProgressListener
import com.android.helper.utils.download.DownLoadManager
import com.android.helper.utils.download.Download
import okhttp3.Response
import java.util.*

class DownloadAdapter(mContext: FragmentActivity, mList: ArrayList<Download>) :
    BaseRecycleAdapter<Download, DownloadAdapter.DlHolder>(mContext, mList) {
    
    private val downLoadManager: DownLoadManager by lazy {
        return@lazy DownLoadManager
            .Builder()
            .setRepeatDownload(false)
            .bindDownload(mContext)
            .build()
    }
    
    class DlHolder(itemView: View) : BaseVH(itemView) {
        
        val tv_download: Button = itemView.findViewById(R.id.tv_download)
        val tv_cancel: Button = itemView.findViewById(R.id.tv_cancel)
        val tv_current_progress: TextView = itemView.findViewById(R.id.tv_current_progress)
        val progress: ProgressBar = itemView.findViewById(R.id.progress)
    }
    
    override fun onBindHolder(holder: DlHolder, position: Int) {
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
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(holder.tv_download, position, bean)
            }
        }
        
        holder.tv_cancel.setOnClickListener {
            // 取消下载
            downLoadManager.cancel(bean.id)
            
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(holder.tv_cancel, position, bean)
            }
        }
        
        holder.itemView.setOnClickListener {
            if (mItemClickListener != null) {
                val bindingAdapterPosition = holder.bindingAdapterPosition
                mItemClickListener.onItemClick(
                    holder.itemView,
                    bindingAdapterPosition,
                    mList[bindingAdapterPosition]
                )
            }
        }
    }
    
    /**
     * @return 返回一个RecycleView的布局
     */
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_download
    }
    
    override fun createViewHolder(inflate: View?, viewType: Int): DlHolder {
        return DlHolder(inflate!!)
    }
}