package com.example.recommend

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.math.abs


class NewsAdapter(private val mContext: Context, private val mNewsList: MutableList<NewsData>, private val mListener: NewsListener) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), View.OnClickListener {
    interface NewsListener {
        fun onClickItem(url: String?,pos:Int)
    }

    override fun onClick(view: View) {}
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news, null))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.title.text = mNewsList[position].title
        holder.src.text = mNewsList[position].src
        holder.time.text = getTime(mNewsList[position].time!!)
        Glide.with(mContext)
            .load(mNewsList[position].pic)
            .into(holder.pic)
        holder.itemView.setOnClickListener {
            saveHistory(mNewsList[position])
            DatabaseManager.instance?.insertRecommend(mNewsList[position].channel)
            mListener.onClickItem(mNewsList[position].url,position)
        }
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tv_item_news_title)
        var src: TextView = itemView.findViewById(R.id.tv_item_news_src)
        var time: TextView = itemView.findViewById(R.id.tv_item_news_time)
        var pic: ImageView = itemView.findViewById(R.id.iv_item_news_pic)

    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(time: String): String {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = format.parse(time) ?: return ""
            val diff = System.currentTimeMillis() - date.time
            val diffHours = diff / (60 * 60 * 1000) % 24
            val hours = (abs(diffHours) + 1).toInt()
            return hours.toString() + "小时前"
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun saveHistory(data: NewsData) {
        DatabaseManager.instance?.deleteHistory(data.url!!)
        DatabaseManager.instance?.insertHistory(data)
    }
}