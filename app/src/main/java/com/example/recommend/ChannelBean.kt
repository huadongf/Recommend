package com.example.recommend
data class ChannelBean (
    /*
     * result : ["头条","新闻","国内","国际","政治","财经","体育","娱乐","军事","教育","科技","NBA","股票","星座","女性","健康","育儿"]
     */
    var status:Int=0,
    var msg: String,
    var result: MutableList<String>
)