package com.android.app.ui.activity.animation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.app.R
import com.android.app.databinding.ActivityViewPagerBinding
import com.android.app.test.banner.VpBanner1Fragment
import com.android.app.test.banner.VpBanner2Fragment
import com.android.app.test.banner.VpBanner3Fragment
import com.android.app.test.banner.VpBanner4Fragment
import com.android.app.test.banner.VpBanner5Fragment
import com.android.common.base.BaseBindingActivity
import com.android.helper.base.BaseViewPager2FragmentAdapter
import com.android.helper.interfaces.listener.OnSelectorListener
import com.android.helper.widget.banner.ViewPager2Util

/** 自定义viewpager的类 */
class ViewPagerActivity : BaseBindingActivity<ActivityViewPagerBinding>() {
    /** Activity初始化view */
    override fun initView() {}

    override fun initData(savedInstanceState: Bundle?) {
        val list: ArrayList<Int> = ArrayList()
        list.add(R.mipmap.icon_banner_1)
        list.add(R.mipmap.icon_banner_2)
        list.add(R.mipmap.icon_banner_3)
        list.add(R.mipmap.icon_banner_4)
        val arrayListOf = arrayListOf<Fragment>()

        arrayListOf.add(VpBanner5Fragment.newInstance())

        arrayListOf.add(VpBanner1Fragment.newInstance())
        arrayListOf.add(VpBanner2Fragment.newInstance())
        arrayListOf.add(VpBanner3Fragment.newInstance())
        arrayListOf.add(VpBanner4Fragment.newInstance())
        arrayListOf.add(VpBanner5Fragment.newInstance())

        arrayListOf.add(VpBanner1Fragment.newInstance())
        val adapter = BaseViewPager2FragmentAdapter(this, arrayListOf)
        mBinding.bannerView.adapter = adapter
        adapter.setSelectorListener(object : OnSelectorListener<Fragment> {
            override fun onSelector(view: View?, position: Int, t: Fragment?) {
                val arg = Bundle()
                arg.putInt("position", position)
                t?.arguments = arg
            }
        })
        val show = ViewPager2Util.Builder().setViewPager2(mBinding.bannerView).setIndicator(mBinding.biBanner).Build().show(this)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityViewPagerBinding {
        return ActivityViewPagerBinding.inflate(inflater, container, false)
    }
}
