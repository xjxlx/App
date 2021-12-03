package com.android.app.ui.activity.personal

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.android.app.R
import com.android.app.databinding.ActivityRouseDingDingBinding
import com.android.helper.base.title.BaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.android.helper.utils.ResourceUtil

/**
 * 唤醒钉钉的页面
 * 设计规则：
 *      1：需要地图的定位页面，可以手动的选择定位的位置，并且把选中的地方逆地里解析出具体的定位信息
 *      2：需要后台定位信息，获取当前的具体位置信息
 *      3：查看是否可以控制轮询定位的时间，如果可以，就动态定位，不行的话，就使用handler之类的去控制
 *      4：对比当前的定位信息，设置轮询的时间
 *      5：唤醒钉钉
 */
class RouseDingDingActivity : BaseBindingTitleActivity<ActivityRouseDingDingBinding>() {
    
    private lateinit var myLocationStyle: MyLocationStyle
    private lateinit var mAMap: AMap
    
    override fun setTitleContent(): String {
        return "唤醒钉钉"
    }
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRouseDingDingBinding {
        return ActivityRouseDingDingBinding.inflate(inflater, container, true)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mBinding.mapView.onCreate(savedInstanceState)
        mAMap = mBinding.mapView.map
        
        initPermission()
        initLocation()
    }
    
    private fun initPermission() {
        // todo  定位权限异常
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 234);//自定义的code
        }
    }
    
    override fun initData() {
    }
    
    /**
     * 初始化地图
     */
    private fun initLocation() {
        // 定位蓝点
        locationPoint()
        
        //true：显示室内地图；false：不显示；
        mAMap.showIndoorMap(true)
        
        //设置希望展示的地图缩放级别,单纯的设置缩放的级别，默认的地方是天安门的中山公园
        // val zoomTo = CameraUpdateFactory.zoomTo(19f)
        // mAMap.moveCamera(zoomTo)
        
        // 监听定位的信息，获取当前的坐标
        mAMap.setOnMyLocationChangeListener(object : AMap.OnMyLocationChangeListener {
            override fun onMyLocationChange(logUtil: Location) {
                
                val latitude = logUtil.latitude
                val longitude = logUtil.longitude
                
                LogUtil.e("latitude:$latitude  longitude:$longitude")
                
                val latLng = LatLng(latitude, longitude)
                //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
                val mCameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 18f, 30f, 0f))
                mAMap.moveCamera(mCameraUpdate)
                
                // 移除定位的信息，避免返回的定位
                mAMap.removeOnMyLocationChangeListener(this)
            }
        })
        
        // 手势交互
        val uiSettings = mAMap.uiSettings;//实例化UiSettings类对象
        uiSettings.isZoomControlsEnabled = true // 缩放按钮可见
        uiSettings.zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER // 缩放按钮的位置
        
        uiSettings.isCompassEnabled = true  // 显示指南针
        
        uiSettings.isMyLocationButtonEnabled = true; //显示默认的定位按钮
    }
    
    /**
     * 定位蓝点的控制
     */
    private fun locationPoint() {
        // 实现定位蓝点
        myLocationStyle = MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        
        // 定位蓝点展现模式 ---> 连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        
        // 控制是否显示定位蓝点
        myLocationStyle.showMyLocation(true)
        
        myLocationStyle.strokeColor(ResourceUtil.getColor(R.color.red_9))//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(ResourceUtil.getColor(R.color.green_3))//设置定位蓝点精度圆圈的边框颜色的方法。
        
        myLocationStyle.strokeWidth(10f) // 设置定位蓝点精度圈的边框宽度的方法。
        
        mAMap.myLocationStyle = myLocationStyle //设置定位蓝点的Style
        
        mAMap.isMyLocationEnabled = true;// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }
    
    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mapView.onResume()
    }
    
    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mapView.onPause()
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mapView.onSaveInstanceState(outState)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mapView.onDestroy();
    }
}

