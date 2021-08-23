package com.android.app.ui.activity.jetpack.model

import com.android.app.R
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.android.helper.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_model1.*

class Model1Fragment : BaseFragment() {

    override fun getBaseLayout(): Int {
        return R.layout.fragment_model1
    }

    override fun initView(view: View?) {

    }

    override fun initData() {

        btn_f1_change.setOnClickListener {
            val get = ViewModelProviders.of(mContext).get(TestViewModel::class.java)
            tv_content_fr_1.text = get.name
        }
    }

    companion object {

        private val fragment: Model1Fragment by lazy {
            return@lazy Model1Fragment()
        }

        fun newInstance(): Model1Fragment {
            return fragment
        }
    }
}