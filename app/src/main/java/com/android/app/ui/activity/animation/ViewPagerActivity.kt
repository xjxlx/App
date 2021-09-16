package com.android.app.ui.activity.animation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.app.R
import com.android.app.databinding.ActivityViewPagerBinding
import com.android.helper.base.BaseBindingActivity
import com.android.helper.utils.ToastUtil
import com.android.helper.widget.banner.BannerItemClickListener
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

        val listView = arrayListOf<View>()
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner2, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner3, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner2, null))
        listView.add(LayoutInflater.from(mContext).inflate(R.layout.fragment_vp_banner3, null))


        mBinding.bannerView
            .setImageData(listView)
            .setImageData(list)
            .setBannerLoadListener(object : BannerLoadListener<Int> {
                override fun onLoadView(view: ImageView?, position: Int, t: Int?) {
                    view?.setImageResource(t!!)
                }
            })
            .setItemClickListener(object : BannerItemClickListener<Int> {
                override fun onItemClick(view: View?, position: Int, t: Int?) {
                    ToastUtil.show("Position:" + position)
                }
            })
            .show(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityViewPagerBinding {
        return ActivityViewPagerBinding.inflate(inflater, container, false)
    }

}