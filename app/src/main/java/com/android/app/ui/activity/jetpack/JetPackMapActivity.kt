package com.android.app.ui.activity.jetpack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityJetPackMapBinding
import com.android.app.ui.activity.jetpack.lifecycle.LifecycleActivity
import com.android.app.ui.activity.jetpack.livedata.LiveDataActivity
import com.android.app.ui.activity.jetpack.model.ViewModelActivity
import com.android.app.ui.activity.jetpack.navigation.NavigationActivity
import com.android.app.ui.activity.jetpack.navigation.navigation2.Navigation2Activity
import com.android.app.ui.activity.jetpack.paging.PagingActivity
import com.android.app.ui.activity.jetpack.room.room1.RoomActivity
import com.android.app.ui.activity.jetpack.room.room2.Room2Activity
import com.android.common.base.BaseBindingTitleActivity

/** JetPack的集合 */
class JetPackMapActivity : BaseBindingTitleActivity<ActivityJetPackMapBinding>() {

    override fun initListener() {
        super.initListener()

        setonClickListener(mBinding.tvBack, mBinding.tvLifecycle, mBinding.tvViewModel, mBinding.tvLiveDate, mBinding.tvRoom,
            mBinding.tvRoom2, mBinding.tvNavigation, mBinding.tvNavigation2, mBinding.tvPaging)
    }

    override fun initData(savedInstanceState: Bundle?) {}

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_back -> {
                finish()
            }

            R.id.tv_lifecycle -> {
                startActivity(LifecycleActivity::class.java)
            }

            R.id.tv_view_model -> {
                intent.putExtra("key", "123")
                startActivity(ViewModelActivity::class.java)
            }

            R.id.tv_live_date -> {
                startActivity(LiveDataActivity::class.java)
            }

            R.id.tv_room -> {
                startActivity(RoomActivity::class.java)
            }

            R.id.tv_room2 -> {
                startActivity(Room2Activity::class.java)
            }

            R.id.tv_navigation -> {
                startActivity(NavigationActivity::class.java)
            }

            R.id.tv_navigation2 -> {
                startActivity(Navigation2Activity::class.java)
            }

            R.id.tv_paging -> {
                startActivity(PagingActivity::class.java)
            }
        }
    }

    override fun getTitleContent(): String {
        return "测试事件分发"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityJetPackMapBinding {
        return ActivityJetPackMapBinding.inflate(layoutInflater, container, true)
    }
}
