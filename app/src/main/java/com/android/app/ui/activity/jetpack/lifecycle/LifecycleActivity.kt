package com.android.app.ui.activity.jetpack.lifecycle

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.android.app.databinding.ActivityLifecycleBinding
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil

/**
 * lifecycle:【laɪf'saɪkəl】 owner:【ˈəʊnə(r)】---欧拿
 *
 * 目标：
 * ```
 *      测试lifecycle的整体使用流程
 * ```
 * 原理：
 * ```
 *      1：明确lifecycle的原理：
 *          1：lifecycle是用来感知activity和fragment的生命周期的
 *          2：lifecycle:主要是使用了两个类：LifecycleOwner（被观察者）和 LifecycleObserver（观察者）
 * ```
 * 使用的好处：
 * ```
 *      1：假如不适用lifecycle的话，如果说存在多个组件或者模块，在生命周期需要做一些事情的时候，就要全部写入activity或者
 *         fragment里面，如果数量过多，则会使得代码变得很是臃肿，如果使用了lifecycle,则可以让对应的模块在自己的类中去单独
 *         处理自己的业务，然后在fragment或者activity里面去设置观察者，会减轻代码的体积，使得阅读量大大的提升。
 * ```
 * 逻辑：
 * ```
 *      1：创建一个简单的音频播放器，在进入页面的时候准备，点击按钮的时候播放，点击停止按钮也可以停止
 *      2：在退出界面的时候，感知布局的状态，进而停止音乐的播放。
 * ```
 */
class LifecycleActivity : BaseBindingTitleActivity<ActivityLifecycleBinding>() {

    private val player: LifecyclePlayer by lazy {
        return@lazy LifecyclePlayer()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        mBinding.tvExplain.text =
            "假如不适用lifecycle的话，如果说存在多个组件或者模块，在生命周期需要做一些事情的时候，" + "就要全部写入activity或fragment里面，如果数量过多，则会使得代码变得很是臃肿，如果使用了lifecycle," + "则可以让对应的模块在自己的类中去单独处理自己的业务，然后在fragment或者activity里面去设置观察者，" + "会减轻代码的体积，使得阅读量大大的提升"
    }

    override fun initListener() {
        super.initListener()
        val url = "http://dlfile.buddyeng.cn/sv/48717030-177bd5eff7d/48717030-177bd5eff7d.mp3"
        mBinding.btnStart.setOnClickListener { player.start(url) }
        mBinding.btnStop.setOnClickListener { player.stop() }
    }

    override fun initData(savedInstanceState: Bundle?) {
        lifecycle.addObserver(player)

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                val name = event.name
                LogUtil.e("name:$name")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e("onDestroy")
    }

    override fun getTitleContent(): String {
        return "测试事件分发"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityLifecycleBinding {
        return ActivityLifecycleBinding.inflate(layoutInflater, container, true)
    }
}
