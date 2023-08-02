package com.android.app.ui.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.app.KeepAidlInterface
import com.android.app.OnChangeListenerAidlInterface
import com.android.common.utils.LogUtil

class KeepService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return mIBinder
    }

    companion object {

        private var mListener: OnChangeListenerAidlInterface? = null

        private val mIBinder: KeepAidlInterface.Stub = object : KeepAidlInterface.Stub() {

            override fun setOnChangeListener(listener: OnChangeListenerAidlInterface?) {
                LogUtil.e("setOnChangeListener ---> :$listener")
                if (listener != null) {
                    mListener = listener
                }
            }
        }

        @JvmStatic
        fun start() {
            LogUtil.e("service - start")
            mListener?.onChange(1)
        }

        @JvmStatic
        fun keep() {
            LogUtil.e("service - keep")
            mListener?.onChange(2)
        }

        @JvmStatic
        fun stop() {
            LogUtil.e("service - stop")
            mListener?.onChange(3)
        }
    }
}
