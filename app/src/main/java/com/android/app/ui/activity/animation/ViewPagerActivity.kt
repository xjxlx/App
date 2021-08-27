package com.android.app.ui.activity.animation

import com.android.app.R
import com.android.helper.base.BaseTitleActivity
import com.android.helper.widget.banner.BannerView
import kotlinx.android.synthetic.main.activity_view_pager.*
import java.util.*

/**
 * 自定义viewpager的类
 */
class ViewPagerActivity : BaseTitleActivity() {

    override fun getTitleLayout(): Int {
        return R.layout.activity_view_pager
    }

    override fun initData() {
        super.initData()
        setTitleContent("自定义ViewPager的类")

        val list: ArrayList<Any> = ArrayList()
        list.add(R.mipmap.icon_banner_1)
        list.add(R.mipmap.icon_banner_2)
        list.add(R.mipmap.icon_banner_3)
        list.add(R.mipmap.icon_banner_4)

        val imageData = BannerView.Builder()
            .autoLoop(true)
            .setImageData(list)
            .setBannerLoadListener { imageView, `object` ->
                val i = `object` as Int
                imageView.setImageResource(i)
            }.addIndicator(bi_banner)

        banner_view.createBuild(imageData)
        banner_view.start(this)
    }

}