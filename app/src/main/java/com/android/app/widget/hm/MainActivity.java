//package com.android.app.widget.hm;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//
//import com.android.app.R;
//import com.itheima.myviewpager78.MyViewPager.OnPageChangeListener;
//
//public class MainActivity extends Activity {
//
//	private MyViewPager mViewPager;
//	private RadioGroup rgGroup;
//
//	private int[] mImageIds = new int[] { R.drawable.a1, R.drawable.a2,
//			R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6 };
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		mViewPager = (MyViewPager) findViewById(R.id.my_viewpager);
//		rgGroup = (RadioGroup) findViewById(R.id.rg_group);
//
//		// 给viewpager添加item布局对象
//		for (int i = 0; i < mImageIds.length; i++) {
//			ImageView view = new ImageView(this);
//			view.setBackgroundResource(mImageIds[i]);
//
//			mViewPager.addView(view);
//		}
//
//		// 添加测试页面
//		View testView = View.inflate(this, R.layout.test_item, null);
//		// mViewPager.addView(testView);
//		mViewPager.addView(testView, 2);// 插入到第二个位置
//
//		// 给RadioGroup添加RadioButton
//		for (int i = 0; i <= mImageIds.length; i++) {
//			RadioButton rb = new RadioButton(this);
//			rb.setId(i);// 必须设置id,保证只有一个被选中
//			rgGroup.addView(rb);
//		}
//
//		rgGroup.check(0);// 默认第一个选中
//
//		// 监听RadioButton点击事件
//		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				int pos = checkedId;// 位置等于id
//
//				mViewPager.setCurrentItem(pos);
//			}
//		});
//
//		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int position) {
//				// 切换radiobutton
//				int id = position;
//				rgGroup.check(id);
//			}
//		});
//	}
//
//}
