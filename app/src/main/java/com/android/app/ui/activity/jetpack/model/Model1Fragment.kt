package com.android.app.ui.activity.jetpack.model

import android.os.Bundle
import com.android.app.R
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.helper.base.AppBaseFragment
import kotlinx.android.synthetic.main.fragment_model1.*

class Model1Fragment : AppBaseFragment() {

    override fun getBaseLayout(): Int {
        return R.layout.fragment_model1
    }

    override fun initView(view: View?) {

    }

    override fun initData(savedInstanceState: Bundle?) {

        btn_f1_change.setOnClickListener {
            val get = ViewModelProvider(this).get(TestViewModel::class.java)
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