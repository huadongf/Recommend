package com.example.recommend

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
class DatabaseHelper internal constructor(context: Context?, name: String?,
                                          factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_HISTORY)
        db.execSQL(CREATE_TABLE_RECOMMEND)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        // 创建历史记录表
        private const val CREATE_TABLE_HISTORY = ("create table " + DbConstant.TABLE_HISTORY
                + " (" + DbConstant.TABLE_HISTORY_ID + " integer primary key autoincrement, "
                + DbConstant.TABLE_HISTORY_TITLE + " text, "
                + DbConstant.TABLE_HISTORY_SRC + " text, "
                + DbConstant.TABLE_HISTORY_TIME + " text, "
                + DbConstant.TABLE_HISTORY_PIC + " text, "
                + DbConstant.TABLE_HISTORY_CHANNEL + " text, "
                + DbConstant.TABLE_HISTORY_URL + " text)")

        // 创建推荐记录表
        private const val CREATE_TABLE_RECOMMEND = ("create table " + DbConstant.TABLE_RECOMMEND
                + " (" + DbConstant.TABLE_RECOMMEND_ID + " integer primary key autoincrement, "
                + DbConstant.TABLE_RECOMMEND_CHANNEL + " text)")
    }
}