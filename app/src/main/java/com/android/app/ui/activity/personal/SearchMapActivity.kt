package com.android.app.ui.activity.personal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.android.app.databinding.ActivitySearchMapBinding
import com.android.helper.base.title.BaseBindingTitleActivity
import com.android.helper.utils.LogUtil

class SearchMapActivity : BaseBindingTitleActivity<ActivitySearchMapBinding>() {
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivitySearchMapBinding {
        return ActivitySearchMapBinding.inflate(inflater, container, true)
    }
    
    /**
     * @return 设置标题内容
     */
    override fun setTitleContent(): String {
        return "搜素选点"
    }
    
    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnSearch.setOnClickListener {
            val trim = mBinding.etInputSearch.text.toString().trim()
            if (!TextUtils.isEmpty(trim)) {
                // 开始搜索数据
                search(mBinding.etInputSearch.text.toString().trim())
            }
        }
    }
    
    private fun search(value: String) {
        /**
         * 参数1：keyWord 查询字符串，多个关键字用“|”分割 。
         * 参数2：POI 类型的组合，比如定义如下组合：餐馆|电影院|景点 （POI类型请在网站“相关下载”处获取）
         * 参数3：搜索的区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
         *       待查询城市（地区）的城市编码 citycode、城市名称（中文或中文全拼）、行政区划代码adcode。必设参数
         */
        val query = PoiSearch.Query(value, "", "")
        
        query.pageSize = 200;// 设置每页最多返回多少条poiitem
        query.pageNum = 0;//设置查询页码
        
        // 数据监听
        val poiSearch = PoiSearch(this, query)
        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(pageResult: PoiResult?, errorCode: Int) {
                LogUtil.e("errorCode:$errorCode   pageResult:$pageResult")
                if (errorCode == 1000) {
                    val listResult = pageResult?.pois
                    if (listResult != null && listResult.size > 0) {
                        LogUtil.e("listResult:" + listResult.toString())
                    }
                }
            }
            
            override fun onPoiItemSearched(poiItem: PoiItem?, errorCode: Int) {
            
            }
        })
        // 发送请求
        poiSearch.searchPOIAsyn();
    }
    
    override fun setBackClickListener(view: View?): Boolean {
        val intent = Intent()
        intent.putExtra("result", "猜猜我是谁")
        setResult(0, intent)
        return super.setBackClickListener(view)
    }
    
}