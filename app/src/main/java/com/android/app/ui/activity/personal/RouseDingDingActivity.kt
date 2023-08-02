package com.android.app.ui.activity.personal

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.DistanceSearch
import com.amap.api.services.route.DistanceSearch.DistanceQuery
import com.android.app.R
import com.android.app.app.Keepalive.LifecycleManager
import com.android.app.databinding.ActivityRouseDingDingBinding
import com.android.app.utils.location.LocationUtil
import com.android.common.utils.LogUtil
import com.android.common.utils.SpUtil
import com.android.helper.app.BaseApplication
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.ResourceUtil
import com.android.helper.utils.ServiceUtil
import com.android.helper.utils.ToastUtil

/**
 * 唤醒钉钉的页面
 * 设计规则：
 *      1：需要地图的定位页面，可以手动的选择定位的位置，并且把选中的地方逆地里解析出具体的定位信息
 *      2：需要后台定位信息，获取当前的具体位置信息
 *      3：查看是否可以控制轮询定位的时间，如果可以，就动态定位，不行的话，就使用handler之类的去控制
 *      4：对比当前的定位信息，设置轮询的时间
 *      5：唤醒钉钉
 */
class RouseDingDingActivity : AppBaseBindingTitleActivity<ActivityRouseDingDingBinding>() {

    private lateinit var myLocationStyle: MyLocationStyle
    private lateinit var mAMap: AMap
    private var mCityCode = "" // 城市编码
    private var mMoved: Boolean = false // 是否移动过
    private val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result != null && result.data != null) {
            if (result.resultCode == 123) {
                if (result.data != null) {
                    val data = result.data
                    val title = data?.getStringExtra("title")
                    val latLonPoint = data?.getParcelableExtra<LatLonPoint>("result")
                    if (latLonPoint != null) {
                        val latLng = LatLng(latLonPoint.latitude, latLonPoint.longitude)
                        SpUtil.putString("latitude", latLonPoint.latitude.toString())
                        SpUtil.putString("longitude", latLonPoint.longitude.toString())

                        addMarker(latLng, title!!, "")
                        moveMap(latLng)
                    }
                    LogUtil.e("result:   $result")
                }
            } else if (result.resultCode == Activity.RESULT_OK) {
                LogUtil.e("其他的跳转！")
            }
        }
    }

    override fun setTitleContent(): String {
        return "唤醒钉钉"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRouseDingDingBinding {
        return ActivityRouseDingDingBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        lifecycle()

        notification() // //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mBinding.mapView.onCreate(savedInstanceState)
        mAMap = mBinding.mapView.map // 1:构建搜索对象
        val distanceSearch = DistanceSearch(this) // 2：设置搜索监听
        distanceSearch.setDistanceSearchListener { distanceResult, errorCode ->
            if (errorCode == 1000) {
                val distanceResults = distanceResult.distanceResults
                if (distanceResults.size > 0) {
                    val distanceItem = distanceResults[0] // 获取距离 单位：米
                    var distance = distanceItem.distance // 行驶时间 单位：秒
                    val duration = distanceItem.duration

                    ToastUtil.show("距离：$distance   时间：$duration")
                    LogUtil.e("距离：$distance   时间：$duration")

                    distance = 20f;

                    if (distance < 50) {
                        val packageManager = packageManager
                        var intent: Intent? = Intent()
                        intent = packageManager.getLaunchIntentForPackage("com.alibaba.android.rimet")
                        if (intent != null) {
                            startActivity(intent)
                        }
                    }
                }
            }
        } // 3:构建搜索参数的对象
        val distanceQuery = DistanceQuery() // 4：设置距离测量起点数据集合
        //  val arrayList = arrayListOf<LatLonPoint>()
        // 5：设置测量方式，支持直线和驾车 直线：TYPE_DISTANCE：驾车：TYPE_DRIVING_DISTANCE 步行：	TYPE_WALK_DISTANCE
        distanceQuery.type = DistanceSearch.TYPE_WALK_DISTANCE // 6:测量距离请求接口，调用后会发起距离测量请求。
        // distanceSearch.calculateRouteDistanceAsyn(distanceQuery)
        LocationUtil
            .Builder(mActivity)
            .setLoop(true)
            .setInterval(5000)
            .setBackgroundRunning(true)
            .setLocationListener { aMapLocation ->
                if (aMapLocation != null) {
                    val latitude = aMapLocation.latitude
                    val longitude = aMapLocation.longitude
                    mCityCode = aMapLocation.cityCode // 城市编码
                    LogUtil.e("latitude:$latitude  longitude:$longitude 城市编码：$mCityCode") // 结束点
                    val latitudeValue = SpUtil.getString("latitude")
                    val longitudeValue = SpUtil.getString("longitude")
                    LogUtil.e("保存的数据为：  latitudeValue：$latitudeValue   longitudeValue:$longitudeValue")
                    if (!TextUtils.isEmpty(latitudeValue) && !TextUtils.isEmpty(longitudeValue)) { // 4：设置距离测量起点数据集合
                        val arrayList = arrayListOf<LatLonPoint>() // 开始点
                        arrayList.add(LatLonPoint(latitude, longitude)) // 4：设置起点的集合
                        distanceQuery.origins = arrayList // 5：设置终点
                        distanceQuery.destination = LatLonPoint(latitudeValue.toDouble(), longitudeValue.toDouble()) // 6:测量距离请求接口，调用后会发起距离测量请求。
                        distanceSearch.calculateRouteDistanceAsyn(distanceQuery)
                    }

                    if (!mMoved) {
                        LogUtil.e("latitude:$latitude  longitude:$longitude")
                        val latLng = LatLng(latitude, longitude)
                        moveMap(latLng) // 定位蓝点
                        locationPoint()

                        touchSetting()

                        mMoved = true
                    }
                }
            }
            .build()
        val intent = Intent(mActivity, SearchMapActivity::class.java)
        if (!TextUtils.isEmpty(mCityCode)) {
            intent.putExtra("cityCode", mCityCode)
        } // 点击跳转
        mBinding.btnSearch.setOnClickListener {
            register.launch(intent)
        } // 长安点击获取屏幕位置
        mAMap.setOnMapLongClickListener { latLng ->
            if (latLng != null) {
                addMarker(latLng, "", "屏幕上的经纬度") // 从屏幕上获取经纬度
                val point = Point(latLng.latitude.toInt(), latLng.longitude.toInt())
                val fromScreenLocation = mAMap.projection.fromScreenLocation(point)
                LogUtil.e("fromScreenLocation:$fromScreenLocation")
            }
        }
    }

    private fun notification() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            val serviceRunning = ServiceUtil.isServiceRunning(mActivity, NotificationService::class.java) // 没有运行的时候去开启
            if (!serviceRunning) {
                val notificationEnabled = ServiceUtil.notificationEnabled()
                if (!notificationEnabled) {
                    val intents = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    register.launch(intents)
                } else {
                    val intent = Intent(this, NotificationService::class.java)
                    intent.putExtra("packageName", "com.alibaba.android.rimet")
                    ServiceUtil.startService(this, intent)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtil.e("onNewIntent:")
    }

    private fun addMarker(latLng: LatLng, title: String, description: String) { // 添加标记点
        val addMarker = mAMap.addMarker(MarkerOptions().position(latLng))
        if (!TextUtils.isEmpty(title)) {
            addMarker.title = title;
        }
        if (!TextUtils.isEmpty(description)) {
            addMarker.snippet = description;
        }
        addMarker.showInfoWindow()
    } // <editor-fold desc="手势交互" >
    /**
     * 手势交互
     */
    private fun touchSetting() { //true：显示室内地图；false：不显示；
        mAMap.showIndoorMap(true) // 手势交互
        val uiSettings = mAMap.uiSettings; //实例化UiSettings类对象
        uiSettings.isZoomControlsEnabled = true // 缩放按钮可见
        uiSettings.zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER // 缩放按钮的位置
        uiSettings.isCompassEnabled = true  // 显示指南针
        uiSettings.isMyLocationButtonEnabled = true //显示默认的定位按钮
    } //</editor-fold>
    // <editor-fold desc="移动位置" >
    /**
     * 移动位置
     */
    private fun moveMap(latLng: LatLng) { //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 18f, 30f, 0f))
        mAMap.moveCamera(mCameraUpdate)
    } //</editor-fold>
    // <editor-fold desc="定位蓝点" >
    /**
     * 定位蓝点的控制
     */
    private fun locationPoint() { // 实现定位蓝点
        myLocationStyle = MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        // 定位蓝点展现模式 ---> 连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER); // 控制是否显示定位蓝点
        myLocationStyle.showMyLocation(true)

        myLocationStyle.strokeColor(ResourceUtil.getColor(R.color.red_9)) //设置定位蓝点精度圆圈的边框颜色的方法。
        // myLocationStyle.radiusFillColor(ResourceUtil.getColor(R.color.green_3))//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.strokeWidth(10f) // 设置定位蓝点精度圈的边框宽度的方法。
        mAMap.myLocationStyle = myLocationStyle //设置定位蓝点的Style
        mAMap.isMyLocationEnabled = true; // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    /**
     * 账号保活
     */
    private fun lifecycle() {
        val serviceName = "com.android.app.ui.activity.personal.MapService";
        val jobServiceName = "com.android.app.app.Keepalive.AppJobService";

        LifecycleManager.getInstance() // com.android.app.ui.activity.personal.MapService
            .startLifecycle(BaseApplication.getInstance().application, serviceName, jobServiceName)
    }

    //</editor-fold>
    override fun onResume() {
        super.onResume() //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause() //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState) //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy() //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mapView.onDestroy();
    }

    /**
     * Activity初始化view
     */
    override fun initView() {
    }
}

