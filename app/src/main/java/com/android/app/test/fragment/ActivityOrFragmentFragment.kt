package com.android.app.test.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.databinding.FragmentActivityOrFragmentBinding
import com.android.helper.base.BaseBindingFragment
import com.android.helper.utils.LogUtil

class ActivityOrFragmentFragment : BaseBindingFragment<FragmentActivityOrFragmentBinding>() {

    private val classTAG = "------> Fragment "
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        LogUtil.e(classTAG + "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e(classTAG + "onCreate")
    }

    override fun initView(view: View?) {
    }

     override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentActivityOrFragmentBinding {
        LogUtil.e(classTAG + "onCreateView")
        return FragmentActivityOrFragmentBinding.inflate(inflater, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LogUtil.e(classTAG + "onActivityCreated")

        mHandler.sendEmptyMessage(1)
    }

    override fun onStart() {
        super.onStart()
        LogUtil.e(classTAG + "onStart")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e(classTAG + "onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.e(classTAG + "onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.e(classTAG + "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.e(classTAG + "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(classTAG + "onDestroy")
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    override fun onDetach() {
        super.onDetach()
        LogUtil.e(classTAG + "onDetach")
    }

    @SuppressLint("HandlerLeak")
    private var mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            LogUtil.e(classTAG + "mHandler")

            sendEmptyMessageDelayed(1, 2000)
        }
    };
}