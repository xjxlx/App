package com.android.app.ui.activity.jetpack.model

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.app.R
import com.android.app.databinding.ActivityViewModelBinding
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.FragmentUtil
import com.android.helper.utils.jetpack.AndroidLifecycle

/**
 * 说明：viewModel 的作用大致的有两方面
 *      1：在页面进行横竖屏操作的时候，保证数据的安全，因为数据是以来map进行存储的，和页面没有什么关系，所以不会消失。
 *      2：在同一个界面中，如果有多个fragment的话，因为使用了viewModel,所以数据存储在了map集合中，和其他的fragment没有
 *      任何的关系，而且取值的时候还是拿的类名去作为key的，所以只要拿到对应的可以获取这个界面的viewModel，然后在集合
 *      中获取自己需要的数据了，更新也是更新到了这个集合中，所以获取的数据也是最新的。
 * 逻辑：
 *      1：首先横竖屏操作，查看数据是否会丢失
 *      2：使用viewModel，然后横竖屏查看数据是否会丢失
 *      3：多个fragment之间进行数据通讯
 */
class ViewModelActivity : AppBaseBindingTitleActivity<ActivityViewModelBinding>() {

    private val viewModel by lazy {
        return@lazy ViewModelProvider(this).get(TestViewModel::class.java)
    }

    override fun initListener() {
        super.initListener()

        mBinding.btnHp.setOnClickListener {
            viewModel.name = "张淑兰"
        }

        mBinding.btnSp.setOnClickListener {
            viewModel.name = "李芳华"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData(savedInstanceState: Bundle?) {
        val stringExtra = intent.getStringExtra("key")
        LogUtil.e("key:$stringExtra")

        lifecycle.addObserver(object : AndroidLifecycle(this) {})
        // 自定横竖屏
        // lifecycle.addObserver(object : OrientationListener(mContext, this) {})
        viewModel.name = "张三"

        mBinding.tvViewModelContent.text =
            "viewModel 的作用大致的有两方面\n" + "1：在页面进行横竖屏操作的时候，保证数据的安全，因为数据是以来map进行存储的，和页面没有什么关系，" + "所以不会消失。\n" + "2：在同一个界面中，如果有多个fragment的话，因为使用了viewModel,所以数据存储在了map集合中，和其他" + "的fragment没有任何的关系，而且取值的时候还是拿的类名去作为key的，所以只要拿到对应的可以获取这个界" + "面的viewModel，然后在集合中获取自己需要的数据了，更新也是更新到了这个集合中，所以获取的数据也是最" + "新的"

        mBinding.tvTest.text = stringExtra
        // 添加两个fragment
        val util = FragmentUtil(mActivity)
        util.add(R.id.fl_content1, Model1Fragment.newInstance(), "") { successful, tag, t ->
            LogUtil.e("successful:$successful")
        }

        util.autoHide(true)
            .add(R.id.fl_content2, Model2Fragment.getInstance(), "") { success, tag, t ->
                LogUtil.e("success:$success")
            }
    }

    override fun setTitleContent(): String {
        return "ViewModel"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityViewModelBinding {
        return ActivityViewModelBinding.inflate(layoutInflater, container, true)
    }
}