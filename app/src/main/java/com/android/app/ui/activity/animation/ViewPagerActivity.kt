package com.android.app.ui.activity.animation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityViewPagerBinding
import com.android.helper.base.BaseBindingActivity
import com.android.helper.widget.banner.BannerLoadListener

/**
 * 自定义viewpager的类
 */
class ViewPagerActivity : BaseBindingActivity<ActivityViewPagerBinding>() {

    override fun initData() {
        setTitleContent("自定义ViewPager的类")

        val list: ArrayList<Int> = ArrayList()
        list.add(R.mipmap.icon_banner_1)
        list.add(R.mipmap.icon_banner_2)
        list.add(R.mipmap.icon_banner_3)
        list.add(R.mipmap.icon_banner_4)

        mBinding.bannerView
            .setImageData(list)
            .setBannerLoadListener(BannerLoadListener<Int> { view, t ->
                view?.setImageResource(t)
            })
            .addIndicator(mBinding.biBanner)
            .show(mContext)

//        val fragmentList = ArrayList<Fragment>()
//        fragmentList.add(VpBanner1Fragment.newInstance())
//        fragmentList.add(VpBanner2Fragment.newInstance())
//        fragmentList.add(VpBanner3Fragment.newInstance())
//        fragmentList.add(VpBanner4Fragment.newInstance())
//        fragmentList.add(VpBanner5Fragment.newInstance())
//
//        mBinding.bannerFragment
//            .setFragmentData(fragmentList)
//            .addIndicator(mBinding.biBanner2)
//            .show(mContext)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityViewPagerBinding {
        return ActivityViewPagerBinding.inflate(inflater, container, false)
    }

}