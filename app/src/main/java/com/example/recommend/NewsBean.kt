package com.example.recommend

class NewsBean {
    var msg: String? = null
    var result: ResultBean? = null

    class ResultBean {
        var list: List<ListBean>? = null

        class ListBean {
            var title: String? = null
            var time: String? = null
            var src: String? = null
            var pic: String? = null
            var content: String? = null
            var url: String? = null
        }
    }
}