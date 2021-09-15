package com.android.app.ui.activity.animation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

//        val list: ArrayList<Int> = ArrayList()
//        list.add(R.mipmap.icon_banner_1)
//        list.add(R.mipmap.icon_banner_2)
//        list.add(R.mipmap.icon_banner_3)
//        list.add(R.mipmap.icon_banner_4)

        val listView = arrayListOf<View>()
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner2, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner3, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner4, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner5, null))

        mBinding.bannerView.setImageData(listView)
            .setBannerLoadListener(BannerLoadListener<Int> { view, _, t ->
                if (view is ImageView) {
                    view.setImageResource(t!!)
                }
            })
            .addIndicator(mBinding.biBanner)
            .show(this)

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