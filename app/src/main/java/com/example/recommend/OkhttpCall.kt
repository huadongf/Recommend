package com.example.recommend

interface OkhttpCall {
    fun onResponse(json: String)
    fun onFailure(errorMsg: String)
}