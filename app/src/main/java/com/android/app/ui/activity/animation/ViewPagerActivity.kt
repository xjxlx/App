package com.android.app.ui.activity.animation

import androidx.fragment.app.Fragment
import com.android.app.R
import com.android.app.test.banner.*
import com.android.helper.base.BaseTitleActivity
import com.android.helper.utils.LogUtil
import com.android.helper.widget.banner.BannerView
import kotlinx.android.synthetic.main.activity_view_pager.*

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
        list.add(R.mipmap.bg)
        list.add(R.mipmap.icon_banner_3)
        list.add(R.mipmap.icon_banner_4)

        val builder1 = BannerView.Builder()
            .autoLoop(true)
            .setImageData(list)
            .setBannerLoadListener { imageView, `object` ->
                val i = `object` as Int
                imageView.setImageResource(i)
            }
            .setItemClickListener { fragment, view, position, `object` ->
                LogUtil.e("当前的position:" + position + "  当前的数据：" + `object` as Int)
            }
            .addIndicator(bi_banner)


        banner_view
            .createBuild(builder1)
            .start(this)

        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(VpBanner1Fragment.newInstance())
        fragmentList.add(VpBanner2Fragment.newInstance())
        fragmentList.add(VpBanner3Fragment.newInstance())
        fragmentList.add(VpBanner4Fragment.newInstance())
        fragmentList.add(VpBanner5Fragment.newInstance())
        val builder = BannerView.Builder()
            .autoLoop(true)
            .setFragmentData(fragmentList)
            .setItemClickListener { fragment, view, position, `object` ->
                LogUtil.e("当前的position:" + position + "  当前的数据：")
            }
            .addIndicator(bi_banner2)

        banner_fragment
            .createBuild(builder)
            .start(this)
    }

}