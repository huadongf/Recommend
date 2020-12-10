package com.example.recommend.ui.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recommend.R
import com.example.recommend.*
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

class DashboardFragment : Fragment() {
    private lateinit var mListRv: RecyclerView
    private val mDataList: MutableList<NewsData> = ArrayList()
    private val mCount = AtomicInteger(ALL_CHANNEL)
    private val mCountMap: MutableMap<String, Int> = HashMap()
    private val mmChannelList: MutableList<String> = ArrayList()
    private lateinit var adapter: NewsAdapter
    private lateinit var ss:SwipeRefreshLayout
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       mmChannelList.clear()
        mDataList.clear()
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        ss=root.findViewById(R.id.swipeRefresh2)
        ss.setOnRefreshListener {
            refreshFruits(adapter)
        }
        val my= ViewModelProvider(requireActivity()).get(Myvi::class.java)
        val listStr = java.lang.String.join("-", my.mChannelList)
        mmChannelList.addAll(listOf(*listStr.split("-").toTypedArray()))
        mListRv = root.findViewById(R.id.rv_recommend_list)
        val count = DatabaseManager.instance?.recommendCount
        if (count != null) {
            if (count > 100) {
                val dec = (count.minus(100)).toInt()
                DatabaseManager.instance?.deleteRecommend(dec)
            }
        }
        val dbChannels = DatabaseManager.instance?.queryAllRecommend()
        if (dbChannels != null) {
            for (channel in dbChannels) {
                if (mCountMap.containsKey(channel)) {
                    val newCount = mCountMap[channel]!! + 1
                    mCountMap[channel] = newCount
                } else {
                    mCountMap[channel] = 1
                }
            }
        }
            ensureCount()
            mListRv.layoutManager = GridLayoutManager(requireContext(), 1)
        return root
    }
    private fun ensureCount() {
        mmChannelList.sortWith { s, t1 ->
            val c1 = if (mCountMap.containsKey(s)) mCountMap[s]!! else 0
            val c2 = if (mCountMap.containsKey(t1)) mCountMap[t1]!! else 0
            c1 - c2
        }
        val allCount = ALL_COUNT.toFloat()
        val recommendCount = (allCount * 0.85).toInt()
        val otherCount = (allCount * 0.05).toInt()
        val channel1 = mmChannelList[0]
        val count1 = if (mCountMap.containsKey(channel1)) mCountMap[channel1]!! else 1
        val channel2 = mmChannelList[1]
        val count2 = if (mCountMap.containsKey(channel2)) mCountMap[channel2]!! else 1
        val realCount1 = (count1.toFloat() / (count1 + count2) * recommendCount).toInt()
        val realCount2 = (count2.toFloat() / (count1 + count2) * recommendCount).toInt()
        val otherChannels = otherChannel
        mCount.set(2 + otherChannels.size)
        requestNews(channel1, realCount1)
        requestNews(channel2, realCount2)
        for (i in otherChannels.indices) {
            requestNews(otherChannels[i], otherCount)
        }
    }

    private val otherChannel: List<String>
        get() {
            val res = HashSet<String>()
            while (res.size <= OTHER_CHANNEL) {
                val random = Random()
                val i = random.nextInt(mmChannelList.size - 2) + 2
                val channel = mmChannelList[i]
                res.add(channel)
            }
            return ArrayList(res)
        }

    private fun requestNews(channel: String, count: Int) {
        val url = UrlObtainer.getNews(channel, count, 0)
        val builder = OkhttpBuilder.Builder()
            .setUrl(url)
            .setOkhttpCall(object : OkhttpCall {
                override fun onResponse(json: String) {   // 得到 json 数据
                    val gson = Gson()
                    val bean = gson.fromJson(json, NewsBean::class.java)
                    val result = bean.result
                    val listBeans = result?.list
                    if (listBeans != null) {
                        for (listBean in listBeans) {
                            var index = Random().nextInt(mDataList.size + 1) - 1
                            if (index < 0) {
                                index = 0
                            }
                            mDataList.add(index, NewsData(listBean.title, listBean.src,
                                listBean.time, listBean.pic, listBean.url, channel))
                        }
                    }
                    endRequest()
                }

                override fun onFailure(errorMsg: String) {
                    endRequest()
                }
            })
            .build()
        OkhttpUtil.getRequest(builder)
    }

    private fun endRequest() {
        mCount.decrementAndGet()
        if (mCount.get() == 0) {
            // 请求完毕，展示数据
            adapter = NewsAdapter(requireContext(), mDataList, object : NewsAdapter.NewsListener {
                override fun onClickItem(url: String?,pos:Int) {
                    val intent = Intent(requireContext(), NewsActivity::class.java)
                    intent.putExtra(NewsActivity.KEY_URL, url)
                    intent.putExtra("title", mDataList[pos].title)
                    intent.putExtra("image", mDataList[pos].pic)
                    intent.putExtra("url", mDataList[pos].url)
                    startActivity(intent)
                }
            })
            mListRv.adapter = adapter

        }
    }
    companion object {
        private const val ALL_COUNT = 40
        private const val OTHER_CHANNEL = 3
        private const val ALL_CHANNEL = 5
    }
    private fun refreshFruits(adapter: NewsAdapter) {
        thread {
            Thread.sleep(800)
            activity?.runOnUiThread {
                mDataList.shuffle()
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(),"智能推荐引擎已更新", Toast.LENGTH_SHORT).show()
                ss.isRefreshing = false
            }
        }
    }
}