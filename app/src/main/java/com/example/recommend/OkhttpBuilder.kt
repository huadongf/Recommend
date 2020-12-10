package com.example.recommend



class OkhttpBuilder private constructor() {
    @JvmField
    var url: String? = null
    @JvmField
    var okhttpCall: OkhttpCall? = null
    var connectTimeout = 0
    var readTimeout = 0

    class Builder {
        private var url: String? = null
        private var connectTimeout = 10
        private var readTimeout = 20
        private var okhttpCall: OkhttpCall? = null
        fun setUrl(url: String?): Builder {
            this.url = url
            return this
        }

        fun setOkhttpCall(okhttpCall: OkhttpCall?): Builder {
            this.okhttpCall = okhttpCall
            return this
        }

        fun build(): OkhttpBuilder {
            val okhttpBuilder = OkhttpBuilder()
            okhttpBuilder.url = url
            okhttpBuilder.connectTimeout = connectTimeout
            okhttpBuilder.readTimeout = readTimeout
            okhttpBuilder.okhttpCall = okhttpCall
            return okhttpBuilder
        }
    }
}