package com.android.app.ui.activity.otherutils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.app.databinding.ActivityLocationBinding;
import com.android.helper.base.AppBaseBindingActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.ToastUtil;
import com.android.helper.utils.location.LocationUtil;
import com.android.helper.utils.location.ReGeocodeResultListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 地图信息
 */
public class LocationActivity extends AppBaseBindingActivity<ActivityLocationBinding> {

    @Override
    public ActivityLocationBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityLocationBinding.inflate(inflater, container, false);
    }

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

        mBinding.btnStartLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = mBinding.edInputAddress.getText().toString();
                String city = mBinding.edInputCity.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    ToastUtil.show("数据不能为空！");
                    return;
                }
                StringBuffer buffer = new StringBuffer();

                LocationUtil.getLocationForAddress(mActivity, address, city, geocodeResult -> {
                    List<GeocodeAddress> addressList = geocodeResult.getGeocodeAddressList();
                    if (addressList != null && addressList.size() > 0) {
                        for (int i = 0; i < addressList.size(); i++) {
                            GeocodeAddress geocodeAddress = addressList.get(i);
                            if (geocodeAddress != null) {
                                LatLonPoint latLonPoint = geocodeAddress.getLatLonPoint();
                                String formatAddress = geocodeAddress.getFormatAddress();
                                String city1 = geocodeAddress.getCity();

                                String province = geocodeAddress.getAdcode();

                                String info = "city ：" + city1 + " ---> Address:" + formatAddress + " ---> latLonPoint: " + latLonPoint.toString() + " ---> code:" + province;

                                buffer.append(info);
                                buffer.append("\r\n");

                                LogUtil.e("返回的数据为：" + info);
                            }
                        }
                    }
                    mBinding.tvLocationInfo.setText(buffer.toString());
                });
            }
        });

        mBinding.btnStartAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = mBinding.edInputLocation.getText().toString();
                //  32.828075,112.595871
                LocationUtil.getAddressForLatitude(mActivity, 33.126469, 112.934043, GeocodeSearch.AMAP, new ReGeocodeResultListener() {
                    @Override
                    public void onReGeocodeSearched(RegeocodeResult regeocodeResult) {
                        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                        String formatAddress = regeocodeAddress.getFormatAddress();
                        LogUtil.e("address:" + formatAddress);
                        mBinding.tvAddressInfo.setText(formatAddress);
                    }
                });
            }
        });
    }
}