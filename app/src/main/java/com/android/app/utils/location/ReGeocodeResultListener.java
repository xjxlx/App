package com.android.app.utils.location;

import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 逆地理编码接口
 */
public interface ReGeocodeResultListener {
    void onReGeocodeSearched(RegeocodeResult regeocodeResult);
}
