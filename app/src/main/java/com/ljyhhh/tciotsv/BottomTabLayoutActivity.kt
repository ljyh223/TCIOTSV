package com.ljyhhh.tciotsv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljyhhh.tciotsv.home.HomeFragment
import com.ljyhhh.tciotsv.setting.SettingFragment
import com.tab.bottom.kit.TabBottomInfo
import com.tab.bottom.kit.tab.TabViewAdapter
import kotlinx.android.synthetic.main.activity_bottom_tab_layout.*
class BottomTabLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_tab_layout)
        initBottomTabAndFragment()


    }

    var mCurrentItemIndex : Int = 0
    private fun initBottomTabAndFragment() {
        tab_bottom_layout.setTabAlpha(0.55f)
        //获取tab数据
        val infoList = getFragmentInfoList()
        //初始化所有的底部tab
        tab_bottom_layout.inflateInfo(infoList)
        //初始化所有的Fragment
        initFragmentTabView(infoList)

        //添加tab切换的监听
        tab_bottom_layout!!.addTabSelectedChangeListener { index, prevInfo, nextInfo -> //切换fragment
            fragment_tab_view!!.currentItem = index
            mCurrentItemIndex = index
        }

        //设置默认选中的tab,只要这个方法被调用,上面的监听tab的onTabSelectedChange()方法就会调用,就会设置当前的fragment
        tab_bottom_layout!!.defaultSelected(infoList[mCurrentItemIndex])



    }

    private fun getFragmentInfoList(): List<TabBottomInfo<*>> {
        val infoList: MutableList<TabBottomInfo<*>> =
            ArrayList()
        val defaultColor: Int = resources.getColor(R.color.gray)
        val tintColor: Int = resources.getColor(R.color.coral)
        val homeInfo = TabBottomInfo(
            "Home",
            "fonts/iconfont.ttf",
            getString(R.string.home),
            null,
            defaultColor,
            tintColor
        )
        homeInfo.mFragment = HomeFragment::class.java

        val settingInfo = TabBottomInfo(
            "Setting",
            "fonts/iconfont.ttf",
            getString(R.string.setting),
            null,
            defaultColor,
            tintColor
        )

        settingInfo.mFragment = SettingFragment::class.java
        infoList.add(homeInfo)
        infoList.add(settingInfo)

        return infoList
    }
    private fun initFragmentTabView(infoList: List<TabBottomInfo<*>>) {
        val adapter = TabViewAdapter(supportFragmentManager, infoList)
        fragment_tab_view!!.adapter = adapter
    }

}