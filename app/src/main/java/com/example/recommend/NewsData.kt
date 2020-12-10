package com.example.recommend

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsData (
    var title: String?,
    var src: String?,
    var time: String?,
    var pic: String?,
    var url: String?,
    var channel: String?,
){
    @PrimaryKey(autoGenerate = true)
    var idd: Long = 0
}