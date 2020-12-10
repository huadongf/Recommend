package com.example.recommend.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.recommend.*
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import java.util.*

class HomeFragment : Fragment() {


    private var mFragmentList //碎片集合
            : MutableList<Fragment>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val my= ViewModelProvider(requireActivity()).get(Myvi::class.java)
        val url = UrlObtainer.channels
        my.mChannelList?.clear()
        setHasOptionsMenu(true)
        val mToolbarContact = root.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbarContact)
        val builder = OkhttpBuilder.Builder()
                .setUrl(url)
                .setOkhttpCall(object : OkhttpCall {
                    override fun onResponse(json: String) {   // 得到 json 数据
                        val gson = Gson()
                        val bean = gson.fromJson(json, ChannelBean::class.java)
                        my.mChannelList = bean.result
                        if (my.mChannelList?.isEmpty() == true) {
                            my.mChannelList = mutableListOf("头条", "新闻", "国内", "国际", "政治", "财经",
                                    "体育", "娱乐", "军事", "教育", "科技", "NBA", "股票", "星座", "女性", "健康", "育儿")
                        }
                        mFragmentList = ArrayList()
                        for (i in my.mChannelList!!.indices) {
                            (mFragmentList as ArrayList<Fragment>).add(NewsFragment.newInstance(my.mChannelList!![i]))
                        }
                        val mViewPager = activity?.findViewById<ViewPager>(R.id.vp_main_news_pager)
                        mViewPager?.adapter = NewsPagerAdapter(activity?.supportFragmentManager, mFragmentList!!, my.mChannelList!!)
                        activity?.findViewById<TabLayout>(R.id.tl_main_news_tab)?.setupWithViewPager(mViewPager) //将TabLayout与ViewPager关联
                    }
                    override fun onFailure(errorMsg: String) {
                        if (my.mChannelList!!.isEmpty()) {
                            my.mChannelList = mutableListOf("头条", "新闻", "国内", "国际", "政治", "财经",
                                    "体育", "娱乐", "军事", "教育", "科技", "NBA", "股票", "星座", "女性", "健康", "育儿")
                        }
                        mFragmentList = ArrayList()
                        for (i in my.mChannelList!!.indices) {
                            (mFragmentList as ArrayList<Fragment>).add(NewsFragment.newInstance(my.mChannelList!![i]))
                        }
                        val mViewPager = activity?.findViewById<ViewPager>(R.id.vp_main_news_pager)
                        mViewPager?.adapter = NewsPagerAdapter(activity?.supportFragmentManager, mFragmentList!!, my.mChannelList!!)
                        activity?.findViewById<TabLayout>(R.id.tl_main_news_tab)?.setupWithViewPager(mViewPager) //将TabLayout与ViewPager关联
                    }
                })
                .build()
        OkhttpUtil.getRequest(builder)
        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.options_menu, menu)
        val searchManager= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setIconifiedByDefault(false)
        }
    }
}