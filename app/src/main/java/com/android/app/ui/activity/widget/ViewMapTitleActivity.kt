package com.android.app.ui.activity.widget

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityViewMapBinding
import com.android.app.test.CustomSlingScrollMainActivity
import com.android.app.test.SlidingMenuActivity
import com.android.app.ui.activity.animation.ViewPagerActivity
import com.android.app.ui.activity.hmview.HmCustomViewActivity
import com.android.helper.base.title.AppBaseBindingTitleActivity

class ViewMapTitleActivity : AppBaseBindingTitleActivity<ActivityViewMapBinding>() {

    override fun setTitleContent(): String {
        return "自定义View集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityViewMapBinding {
        return ActivityViewMapBinding.inflate(inflater, container, true)
    }

    override fun initListener() {
        super.initListener()
        setonClickListener(R.id.tv_back, R.id.tv_progress, R.id.tv_multiple_list_view, R.id.tv_custom_text, R.id.tv_custom_round,
            R.id.tv_custom_random, R.id.tv_custom_left_and_right, R.id.tv_custom_progress, R.id.tv_custom_touch,
            R.id.tv_custom_input_password, R.id.tv_scroll_view, R.id.tv_page_view, R.id.tv_test_hm, R.id.tv_custom_menu,
            R.id.tv_custom_name_list, R.id.tv_custom_viewpager, R.id.tv_custom_round_2, R.id.tv_custom_page_list, R.id.tv_custom_charging,
            R.id.tv_custom_number_scroll, R.id.tv_custom_breathing, R.id.tv_custom_viewpager2, R.id.tv_custom_yh, R.id.tv_custom_menu2,
            R.id.tv_custom_canvas_savere_storeview)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.tv_back -> finish()
            R.id.tv_progress -> startActivity(ProgressTitleActivity::class.java)
            R.id.tv_multiple_list_view -> startActivity(MultipleListViewActivity::class.java)
            R.id.tv_custom_text -> startActivity(CustomTestActivity::class.java)
            R.id.tv_custom_round -> startActivity(CustomRoundImageActivity::class.java)
            R.id.tv_custom_random -> startActivity(RandomActivity::class.java)
            R.id.tv_custom_left_and_right -> startActivity(JointActivity::class.java)
            R.id.tv_custom_progress -> startActivity(ProgressActivity::class.java)
            R.id.tv_custom_touch -> startActivity(TouchUnlockActivity::class.java)
            R.id.tv_custom_input_password -> startActivity(InputPassWordActivity::class.java)
            R.id.tv_scroll_view -> startActivity(NestSlidingViewActivity::class.java)
            R.id.tv_page_view -> startActivity(ScrollPageViewActivity::class.java)
            R.id.tv_test_hm -> startActivity(HmCustomViewActivity::class.java)
            R.id.tv_custom_menu -> startActivity(SlidingMenuActivity::class.java)
            R.id.tv_custom_menu2 -> startActivity(CustomSlingScrollMainActivity::class.java)
            R.id.tv_custom_name_list -> startActivity(NameListActivity::class.java)
            R.id.tv_custom_viewpager -> startActivity(ViewPagerActivity::class.java)
            R.id.tv_custom_round_2 -> startActivity(CustomRound2Activity::class.java)
            R.id.tv_custom_page_list -> startActivity(PageListActivity::class.java)
            R.id.tv_custom_charging -> startActivity(CustomChargingActivity::class.java)
            R.id.tv_custom_number_scroll -> startActivity(CustomNumberScrollActivity::class.java)
            R.id.tv_custom_breathing -> startActivity(CustomBreathingViewActivity::class.java)
            R.id.tv_custom_viewpager2 -> startActivity(ViewPager2Activity::class.java)
            R.id.tv_custom_yh -> startActivity(CustomYhActivity::class.java)
            R.id.tv_custom_canvas_savere_storeview -> startActivity(CanvasSaveRestoreActivity::class.java)
        }
    }
}