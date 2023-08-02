package com.android.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Scroller
import com.android.common.utils.LogUtil
import kotlin.math.abs

/**
 * @author : 流星
 * @CreateDate: 2022/10/18-0:02
 * @Description:
 */
@SuppressLint("ViewConstructor")
class ViewPager2 : ViewGroup {

    var dxxx = 0

    // 手势识别器
    private var mGestureDetector: GestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            LogUtil.e("onScroll!")
            // e1:起点;e2:终点;distanceX: 水平滑动距离; distanceY:竖直距离
            dxxx = distanceX.toInt()
            scrollBy(distanceX.toInt(), 0)
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    })

    // 滑动器
    private val mScroller = Scroller(context)

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 测量ViewPager宽高
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        for (item in 0 until childCount) {
            val childAt = getChildAt(item)
            // 测量子view的宽高
            childAt.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in 0 until childCount) {
            LogUtil.e("index:$index")
            val childAt = getChildAt(index)
            // 开始摆放位置
            childAt.layout(width * index, 0, (width + 1) * index + width, height)
        }
    }

    private var mStartX = 0
    private var mStartY = 0

    // 拦截
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = ev.x.toInt()
                mStartY = ev.y.toInt()

                // 此处如果返回了true,则全部让viewGroup给消耗掉，设置了layout后，会左右滑动，但是scrollview就不会上下滑动了
                // 这里设置给手势识别器去处理，让手势识别器去处理到底是上下还是左右滑动
                mGestureDetector.onTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()

                val dx = endX - mStartX
                val dy = endY - mStartY
                if (abs(dx) > abs(dy)) {
                    LogUtil.e("左右滑动")
                    // 左右滑动的时候，viewGroup自己消耗，不让别人使用
                    return true // 要中断事件传递, 从而子控件获取不到事件
                } else {
                    // 上下滑动的时候，不去管他，让父类去自己处理
                    LogUtil.e("上下滑动")
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 把手势识别器交给touch去控制
        mGestureDetector.onTouchEvent(event)
        //        return super.onTouchEvent(event)

        when (event?.action) {
            // 手指抬起
            MotionEvent.ACTION_UP -> {
                // 滑动的距离
                val scrollX = scrollX
                // 当前的角标
                var position = scrollX / width
                LogUtil.e("position:$position")

                // 获取余数
                val offset = scrollX % width
                LogUtil.e("offset:$offset")
                if (offset > width / 2) {
                    position++
                }

                // 避免越界
                if (position <= 0) {
                    position = 0
                }
                if (position > childCount - 1) {
                    position = childCount - 1
                }

                // 滑动
                setCurrentItem(position)
            }
        }
        return true
    }

    private fun setCurrentItem(position: Int) {
        // 滑动的距离
        // scrollTo(position * width, 0)

        // 偏移值
        var dx = 0
        //         int distance = position * getWidth() - scrollX;// 移动距离 = 终点位置 -

        dx = position * width - scrollX
        // 开始滑动
        // 参1:起始x位置;参2:起始y位置;参3:x偏移量;参4:y偏移量;参5:动画时间
        // 此处并不能直接启动动画,而是不断回调computeScroll方法, 在该方法里手动修改当前滑动位置
        // 以距离绝对值作为动画时间, 保证匀速滑动
        // 此处并不能直接启动动画,而是不断回调computeScroll方法, 在该方法里手动修改当前滑动位置
        mScroller.startScroll(position * width, 0, dx, 0, abs(dx))
        invalidate() // 刷新界面
    }

    // 当使用滑动器Scroller调用startScroll时, 系统会不断回调此方法, 在此方法中处理滑动逻辑
    override fun computeScroll() {
        super.computeScroll()
        LogUtil.e("computeScroll!!!")
        // //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。
        if (mScroller.computeScrollOffset()) { // 判断动画是否结束
            // 获取滑动的X轴位置
            val currX = mScroller.currX // 获取当前应该滑动到的位置
            scrollTo(currX, 0) // 滑动到该位置
            invalidate() // 刷新界面
        }
    }
}
