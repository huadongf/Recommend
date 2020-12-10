package com.example.recommend

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import java.util.*
import kotlin.concurrent.thread

class NewsFragment : Fragment() {
    private var channel: String? = null
    private var mNewsRv: RecyclerView? = null
    private var mIsVisible = false
    private var mIsViewCreated = false
    private var mIsLoadData = false
    private lateinit var adapter:NewsAdapter
    private lateinit var ss:SwipeRefreshLayout
    val dataList: MutableList<NewsData> = ArrayList()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (arguments != null) {
            channel = requireArguments().getString("channel")
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_news, null)
        initView(view)
        ss=view.findViewById(R.id.swipeRefresh)
        ss.setOnRefreshListener {
            refreshFruits(adapter)
        }
        return view
    }
    private fun initView(root: View) {
        mNewsRv = root.findViewById(R.id.rv_news_list)
        val linearLayoutManager = LinearLayoutManager(context)
        mNewsRv?.layoutManager = linearLayoutManager
        mIsViewCreated = true

        if (mIsVisible && !mIsLoadData) {

            requestNews()
        }
    }

    private fun requestNews() {
        val url = UrlObtainer.getNews(channel.toString(), 40, 0)
        val builder = OkhttpBuilder.Builder()
            .setUrl(url)
            .setOkhttpCall(object : OkhttpCall {
                override fun onResponse(json: String) {   // 得到 json 数据
                    val gson = Gson()
                    val bean = gson.fromJson(json, NewsBean::class.java)
                    val result = bean.result
                    val listBeans = result!!.list
                    dataList.clear()
                    val userdao = AppDatabase.getDatabase(requireContext()).userDao()
                    userdao.del()
                    for (listBean in listBeans!!) {
                        val aa=NewsData(listBean.title, listBean.src,
                            listBean.time, listBean.pic, listBean.url, channel)
                        dataList.add(aa)
                        userdao.insertUser(aa)
                    }
                    adapter = NewsAdapter(context!!, dataList, object :
                        NewsAdapter.NewsListener {
                        override fun onClickItem(url: String?,pos:Int) {
                            val intent = Intent(requireContext(), NewsActivity::class.java)
                            intent.putExtra(NewsActivity.KEY_URL, url)
                            intent.putExtra("title", dataList[pos].title)
                            intent.putExtra("image", dataList[pos].pic)
                            intent.putExtra("url", dataList[pos].url)
                            startActivity(intent)
                        }
                    })

                    // 滑到第二个 tab 时，mNewsRv 引用指向第一个 tab
                    mNewsRv!!.adapter = adapter
                    mIsLoadData = true
                }
                override fun onFailure(errorMsg: String) {
                }
            })
            .build()
        OkhttpUtil.getRequest(builder)
    }
    private fun refreshFruits(adapter: NewsAdapter) {
        thread {
            Thread.sleep(800)
            activity?.runOnUiThread {
                dataList.shuffle()
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(),"智能推荐引擎已更新", Toast.LENGTH_SHORT).show()
                ss.isRefreshing = false
            }
        }
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisible = isVisibleToUser
        if (isVisibleToUser && mIsViewCreated && !mIsLoadData) {
            requestNews()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mIsLoadData = false
        mIsViewCreated = false
    }


    companion object {
        /**
         * 返回碎片实例
         */
        fun newInstance(channel: String): NewsFragment {
            val fragment = NewsFragment()
            //动态加载fragment，接受activity传入的值
            val bundle = Bundle()
            bundle.putString("channel", channel)
            fragment.arguments = bundle
            return fragment
        }
    }
}