package com.android.app.ui.activity.jetpack.model

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.app.R
import com.android.helper.base.AppBaseFragment
import kotlinx.android.synthetic.main.fragment_model2.*

class Model2Fragment : AppBaseFragment() {

    override fun getBaseLayout(): Int {
        return R.layout.fragment_model2
    }

    override fun initView(view: View?) {
    }

    override fun initData(savedInstanceState: Bundle?) {

        btn_f2_change.setOnClickListener {
            val get = ViewModelProvider(this).get(TestViewModel::class.java)
            tv_content_fr_2.text = get.name
        }
    }

    companion object {

        private val fragment by lazy {
            return@lazy Model2Fragment()
        }

        fun getInstance(): Model2Fragment {
            return fragment;
        }
    }

}