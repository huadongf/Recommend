package com.example.recommend

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
class NewsPagerAdapter(fm: FragmentManager?, //碎片集合
                       private val mFragmentList: List<Fragment>, //tab的标题
                       private val mPageTitleList: List<String>) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mPageTitleList[position]
    }
}