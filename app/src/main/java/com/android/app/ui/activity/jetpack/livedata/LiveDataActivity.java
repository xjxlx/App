package com.android.app.ui.activity.jetpack.livedata;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.android.app.R;
import com.android.app.databinding.ActivityLiveDataBinding;
import com.android.common.base.BaseBindingTitleActivity;
import com.android.common.utils.LogUtil;
import com.android.helper.utils.FragmentUtil;
import com.android.helper.utils.ToastUtil;

/**
 * Mutable: /ˈmjuːtəbl/ 缪特保
 * 使用好处：
 * 1:在需要观察的界面使用viewModel的对象去观察数据源的变化，可以一个地方设置，所有地方公用，已经离不开这种模式了，
 * 只要一个地方设置了数据，其他地方只有监听对象，就能立马更新数据，可以做到数据的实时同步。
 * 2：liveData 的内部方法onChanged,只有在页面可见的时候发生作用，避免了一些子线程或者页面不可见时候更新界面导致的
 * 崩溃，减少了很多代码的检查操作。
 * <p>
 * 逻辑：
 * <p>
 * 测试：
 */
public class LiveDataActivity extends BaseBindingTitleActivity {

    private LiveDataModel mLiveDataModel;
    private TextView mTvLiveDateContent;
    private TextView mMTvHint;
    private MutableLiveModel mMutableLiveModel;
    private TextView mTvMutableLiveDateContent;
    private Observer<TestMutableLiveData> mObserver;

    @Override
    public void initView() {
        mTvLiveDateContent = findViewById(R.id.tv_live_date_content);
        mTvMutableLiveDateContent = findViewById(R.id.tv_mutable_live_date_content);
        mMTvHint = findViewById(R.id.tv_hint);
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.bt_live_data, R.id.bt_mutable_live_data, R.id.btn_test_start_for_rever, R.id.btn_test_stop_for_rever);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        ;
        mMTvHint.setText("使用好处：\n" + "1:在需要观察的界面使用viewModel的对象去观察数据源的变化，可以一个地方设置，所有地方公用，已经离不开这种模式了，" + "只要一个地方设置了数据，其他地方只有监听对象，就能立马更新数据，可以做到数据的实时同步。" + "\r\n" + "2：liveData 的内部方法onChanged,只有在页面可见的时候发生作用，避免了一些子线程或者页面不可见时候更新界面导致的" + "崩溃，减少了很多代码的检查操作。");
        testLiveData();
        testMutableLiveData();
        FragmentUtil fragmentUtil = new FragmentUtil(mActivity);
        fragmentUtil.add(R.id.fl_live_data_1, LiveData1Fragment.newInstance(), "", (successful, tag, o) -> {
        });
        fragmentUtil.add(R.id.fl_live_data_2, LiveData2Fragment.newInstance(), "", (successful, tag, o) -> {
        });
    }

    private void testLiveData() {
        // liveData的测试
        mLiveDataModel = new ViewModelProvider(this).get(LiveDataModel.class);
        mLiveDataModel.getLiveData().observe(this, new Observer<TestLiveData>() {
            @Override
            public void onChanged(TestLiveData testLiveData) {
                int age = testLiveData.getAge();
                String name = testLiveData.getName();
                LogUtil.e("name:" + name + "  age:" + age);
                mTvLiveDateContent.setText(name);
            }
        });
        // 添加永远发送的请求
        mLiveDataModel.getLiveData().observeForever(new Observer<TestLiveData>() {
            @Override
            public void onChanged(TestLiveData testLiveData) {
                String name = testLiveData.getName();
                int age = testLiveData.getAge();
                ToastUtil.show("Name:" + name);
                LogUtil.e("------->name:" + name + "  age:" + age);
            }
        });
    }

    private void testMutableLiveData() {
        mMutableLiveModel = new ViewModelProvider(this).get(MutableLiveModel.class);
        mObserver = new Observer<TestMutableLiveData>() {
            @Override
            public void onChanged(TestMutableLiveData testMutableLiveData) {
                LogUtil.e(testMutableLiveData.toString());
                mTvMutableLiveDateContent.setText(testMutableLiveData.getName());
            }
        };
        mMutableLiveModel.getData().observe(this, mObserver);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_live_data:
                String anotherName = "John Doe";
                mLiveDataModel.getLiveData().setName(anotherName);
                break;
            case R.id.bt_mutable_live_data:
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        mMutableLiveModel.getData().setName("赵六");
                    }
                };
                thread.start();
                break;
            case R.id.btn_test_start_for_rever:
                mHandler.sendEmptyMessageDelayed(123, 3000);
                break;
            case R.id.btn_test_stop_for_rever:
                mHandler.removeCallbacksAndMessages(null);
                mMutableLiveModel.getData().removeObserver(mObserver);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLiveDataModel.getLiveData().setName("2222");
            mHandler.sendEmptyMessageDelayed(123, 3000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @NonNull
    @Override
    public ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityLiveDataBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "";
    }
}