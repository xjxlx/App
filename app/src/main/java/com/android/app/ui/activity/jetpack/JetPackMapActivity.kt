package com.android.app.ui.activity.jetpack

import com.android.app.R
import com.android.app.ui.activity.jetpack.lifecycle.LifecycleActivity
import com.android.app.ui.activity.jetpack.livedata.LiveDataActivity
import com.android.app.ui.activity.jetpack.model.ViewModelActivity
import com.android.app.ui.activity.jetpack.navigation.NavigationActivity
import com.android.app.ui.activity.jetpack.navigation.navigation2.Navigation2Activity
import com.android.app.ui.activity.jetpack.paging.PagingActivity
import com.android.app.ui.activity.jetpack.room.room1.RoomActivity
import com.android.app.ui.activity.jetpack.room.room2.Room2Activity
import android.view.View
import com.android.helper.base.BaseTitleActivity
import kotlinx.android.synthetic.main.activity_jet_pack_map.*

/**
 * JetPack的集合
 */
class JetPackMapActivity : BaseTitleActivity() {

    override fun getTitleLayout(): Int {
        return R.layout.activity_jet_pack_map
    }

    override fun initView() {
        super.initView()
        setTitleContent("JetPack的集合")
    }

    override fun initListener() {
        super.initListener()

        setonClickListener(tv_lifecycle,
                tv_view_model,
                tv_live_date,
                tv_room,
                tv_room2,
                tv_navigation,
                tv_navigation2,
                tv_paging
        )
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
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

}