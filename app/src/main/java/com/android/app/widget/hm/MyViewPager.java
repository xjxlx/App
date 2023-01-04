//package com.android.app.widget.hm;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Scroller;
///**
// * 开发流程:
// * 1. 写类继承ViewGroup
// * 2. 在Activity中添加ImageView
// * 3. 重写onLayout设置位置, 一字排开
// * 4. 重写onTouchEvent, 使用手势识别器滑动 scrollBy
// * 5. 弹起手指, 确定下一页位置, 滑动到该位置 scrollTo
// * 6. 使用Scroller实现平滑滑动效果
// * 7. 增加测试页面(ScrollView),重写onMeasure方法
// * 8. 事件处理 onInterceptTouchEvent
// * 9. 增加RadioButton, 点击RadioButton切换页面
// * 10. 切换页面, 更新RaidoButton
// * @author Kevin
// * @date 2015-12-16
// */
//public class MyViewPager extends ViewGroup {
//
//	private GestureDetector mDetector;
//	private Scroller mScroller;
//
//	public MyViewPager(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		init();
//	}
//
//	public MyViewPager(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init();
//	}
//
//	public MyViewPager(Context context) {
//		super(context);
//		init();
//	}
//
//	private void init() {
//		mDetector = new GestureDetector(getContext(),
//				new GestureDetector.SimpleOnGestureListener() {
//					// e1:起点;e2:终点;distanceX: 水平滑动距离; distanceY:竖直距离
//					@Override
//					public boolean onScroll(MotionEvent e1, MotionEvent e2,
//							float distanceX, float distanceY) {
//
//						scrollBy((int) distanceX, 0);// 基于当前位置偏移多远距离, 相对位置
//						// scrollTo(x, y)//绝对位置, 要移动到哪里去
//
//						return super.onScroll(e1, e2, distanceX, distanceY);
//					}
//				});
//
//		// 滑动器
//		mScroller = new Scroller(getContext());
//	}
//
//	// 测量布局宽高
//	// 解决测试页面子控件不显示的bug
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);// 测量ViewPager宽高
//		for (int i = 0; i < getChildCount(); i++) {
//			View child = getChildAt(i);
//			child.measure(widthMeasureSpec, heightMeasureSpec);// 测量当前子控件宽高
//		}
//
//		System.out.println("widthMeasureSpec:" + widthMeasureSpec);
//		System.out.println("heightMeasureSpec:" + heightMeasureSpec);
//
//		int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 获取宽度模式
//		int widthSize = MeasureSpec.getSize(widthMeasureSpec);// 获取宽度值
//
//		// MeasureSpec.AT_MOST 至多模式, 类似wrap_content
//		// MeasureSpec.EXACTLY 确定模式, 类似宽高写死, match_parent
//		// MeasureSpec.UNSPECIFIED 未确定模式, listview
//
//		System.out.println("widthMode:" + widthMode);
//		System.out.println("widthSize:" + widthSize);
//
//	}
//
//	// 设置位置
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		// 重写此方法来设置每个子控件的位置
//		for (int i = 0; i < getChildCount(); i++) {
//			View child = getChildAt(i);
//			// 保证所有item布局一字排开
//			child.layout(getWidth() * i, 0, getWidth() * (i + 1), getHeight());// 设置每个子控件的位置
//		}
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		mDetector.onTouchEvent(event);// 将事件委托给手势识别器处理
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_UP:
//			// 手指抬起之后,决定下一个页面位置
//			int scrollX = getScrollX();// 当前滑动之后的位置
//			// System.out.println("scrollX-->" + scrollX);
//			int position = scrollX / getWidth();// 计算出当前位置
//			// System.out.println("position-->" + position);
//
//			int offset = scrollX % getWidth();// 计算出多出来的偏移量
//			if (offset > getWidth() / 2) {
//				// 跳到下一页
//				position++;
//			}
//
//			// 避免越界
//			if (position > getChildCount() - 1) {
//				position = getChildCount() - 1;
//			}
//
//			// 避免越界
//			if (position < 0) {
//				position = 0;
//			}
//
//			// System.out.println("position-->" + position);
//
//			// // 移动到下一页位置
//			// // scrollTo(position * getWidth(), 0);
//			// int distance = position * getWidth() - scrollX;// 移动距离 = 终点位置 -
//			// 起点位置
//			// // 100px -> 100ms
//			// // 以距离绝对值作为动画时间, 保证匀速滑动
//			// // 此处并不能直接启动动画,而是不断回调computeScroll方法, 在该方法里手动修改当前滑动位置
//			// mScroller.startScroll(scrollX, 0, distance, 0,
//			// Math.abs(distance));// 参1:起始x位置;参2:起始y位置;参3:x偏移量;参4:y偏移量;参5:动画时间
//			// invalidate();// 刷新界面
//
//			setCurrentItem(position);
//			break;
//
//		default:
//			break;
//		}
//
//		return true;
//	}
//
//	// 当使用滑动器Scroller调用startScroll时, 系统会不断回调此方法, 在此方法中处理滑动逻辑
//	@Override
//	public void computeScroll() {
//		if (mScroller.computeScrollOffset()) {// 判断动画是否结束
//			int currX = mScroller.getCurrX();// 获取当前应该滑动到的位置
//			System.out.println("currX--->" + currX);
//			scrollTo(currX, 0);// 滑动到该位置
//			invalidate();// 刷新界面
//		}
//
//	}
//
//	// 事件分发的方法
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		return super.dispatchTouchEvent(ev);
//	}
//
//	int startX;
//	int startY;
//
//	// 事件中断的方法
//	// dispatchTouchEvent->onInterceptTouchEvent->onTouchEvent
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			startX = (int) ev.getX();
//			startY = (int) ev.getY();
//
//			// 由于此处运行结束之后返回的是false, 事件被ScrollView拿走,导致ViewPager丢掉了按下的事件,
//			// 从而手势识别器判断出现误差
//			// 解决办法: 手动给手势识别器把此事件补上
//			mDetector.onTouchEvent(ev);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			int newX = (int) ev.getX();
//			int newY = (int) ev.getY();
//
//			int dx = newX - startX;
//			int dy = newY - startY;
//
//			if (Math.abs(dx) > Math.abs(dy)) {
//				// 左右滑动
//				return true;// 要中断事件传递, 从而子控件获取不到事件
//			}
//
//			break;
//
//		default:
//			break;
//		}
//
//		return false;
//	}
//
//	// 切换到某个页面
//	public void setCurrentItem(int position) {
//		// 移动到下一页位置
//		// scrollTo(position * getWidth(), 0);
//		int distance = position * getWidth() - getScrollX();// 移动距离 = 终点位置 -
//															// 起点位置
//		// 100px -> 100ms
//		// 以距离绝对值作为动画时间, 保证匀速滑动
//		// 此处并不能直接启动动画,而是不断回调computeScroll方法, 在该方法里手动修改当前滑动位置
//		mScroller.startScroll(getScrollX(), 0, distance, 0, Math.abs(distance));// 参1:起始x位置;参2:起始y位置;参3:x偏移量;参4:y偏移量;参5:动画时间
//		invalidate();// 刷新界面
//
//		// 通知回调
//		if (mListener != null) {
//			mListener.onPageSelected(position);
//		}
//	}
//
//	private OnPageChangeListener mListener;
//
//	public void setOnPageChangeListener(OnPageChangeListener listener) {
//		mListener = listener;
//	}
//
//	public interface OnPageChangeListener {
//		public void onPageSelected(int position);
//	}
//
//}
