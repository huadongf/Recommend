package com.example.recommend

object DbConstant {
    // 数据库名
    const val DB_NAME = "FNews.db"

    // 历史记录表
    const val TABLE_HISTORY = "TABLE_HISTORY"

    // 历史记录表的记录
    const val TABLE_HISTORY_ID = "TABLE_HISTORY_ID" // 自增 id（主键）
    const val TABLE_HISTORY_TITLE = "TABLE_HISTORY_TITLE"
    const val TABLE_HISTORY_SRC = "TABLE_HISTORY_SRC"
    const val TABLE_HISTORY_TIME = "TABLE_HISTORY_TIME"
    const val TABLE_HISTORY_PIC = "TABLE_HISTORY_PIC"
    const val TABLE_HISTORY_URL = "TABLE_HISTORY_URL"
    const val TABLE_HISTORY_CHANNEL = "TABLE_HISTORY_CHANNEL"

    // 推荐记录表
    const val TABLE_RECOMMEND = "TABLE_RECOMMEND"

    // 推荐记录表的记录
    const val TABLE_RECOMMEND_ID = "TABLE_RECOMMEND_ID"
    const val TABLE_RECOMMEND_CHANNEL = "TABLE_RECOMMEND_CHANNEL"
}