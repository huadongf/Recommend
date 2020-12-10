package com.example.recommend

object UrlObtainer {
    /**
     * 获取新闻
     *
     * @param num 数量 默认10，最大40
     * @param start 起始位置，默认0 最大400
     */
    fun getNews(channel: String, num: Int, start: Int): String {
        return "https://api.jisuapi.com/news/get?channel=" + channel +
                "&start=" + start +
                "&num=" + num +
                "&appkey=cf2efa79d8df0ede"
    }

    /**
     * 获取新闻频道
     */
    val channels: String
        get() = "https://api.jisuapi.com/news/channel?appkey=cf2efa79d8df0ede"
}