package com.example.recommend

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.recommend.App.Companion.context
import java.util.*
class DatabaseManager private constructor() {
    private val mDb: SQLiteDatabase

    /**
     * 插入一条新的历史记录
     */
    fun insertHistory(newsData: NewsData) {
        val values = ContentValues()
        values.put(DbConstant.TABLE_HISTORY_TITLE, newsData.title)
        values.put(DbConstant.TABLE_HISTORY_SRC, newsData.src)
        values.put(DbConstant.TABLE_HISTORY_TIME, newsData.time)
        values.put(DbConstant.TABLE_HISTORY_PIC, newsData.pic)
        values.put(DbConstant.TABLE_HISTORY_CHANNEL, newsData.channel)
        values.put(DbConstant.TABLE_HISTORY_URL, newsData.url)
        mDb.insert(DbConstant.TABLE_HISTORY, null, values)
    }

    /**
     * 删除一条历史记录
     */
    fun deleteHistory(url: String) {
        mDb.delete(DbConstant.TABLE_HISTORY,
            DbConstant.TABLE_HISTORY_URL + " = ?", arrayOf(url))
    }

    /**
     * 插入一条新的推荐记录
     */
    fun insertRecommend(channel: String?) {
        val values = ContentValues()
        values.put(DbConstant.TABLE_RECOMMEND_CHANNEL, channel)
        mDb.insert(DbConstant.TABLE_RECOMMEND, null, values)
    }

    /**
     * 查询推荐记录条数
     */
    val recommendCount: Long
        get() {
            val sql = "select count(*) from " + DbConstant.TABLE_RECOMMEND
            val cursor = mDb.rawQuery(sql, null)
            cursor.moveToFirst()
            val count = cursor.getLong(0)
            cursor.close()
            return count
        }

    /**
     * 删除前 n 条推荐记录
     */
    fun deleteRecommend(n: Int) {
        val sql = "delete from " + DbConstant.TABLE_RECOMMEND +
                " where " + DbConstant.TABLE_RECOMMEND_ID + " in(" +
                "select " + DbConstant.TABLE_RECOMMEND_ID + " from " + DbConstant.TABLE_RECOMMEND +
                " order by " + DbConstant.TABLE_RECOMMEND_ID +
                " limit " + n + ")"
        mDb.execSQL(sql)
    }

    /**
     * 查询所有推荐记录（较新的记录排前面）
     */
    fun queryAllRecommend(): List<String> {
        val res: MutableList<String> = ArrayList()
        val cursor = mDb.query(DbConstant.TABLE_RECOMMEND, null, null,
            null, null, null, null)
        if (cursor.moveToLast()) {
            do {
                res.add(cursor.getString(cursor.getColumnIndex(DbConstant.TABLE_RECOMMEND_CHANNEL)))
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        return res
    }

    companion object {
        private var mManager: DatabaseManager? = null
        val instance: DatabaseManager?
            get() {
                if (mManager == null) {
                    mManager = DatabaseManager()
                }
                return mManager
            }
    }

    init {
        val helper: SQLiteOpenHelper = DatabaseHelper(
            context, DbConstant.DB_NAME, null, 1)
        mDb = helper.writableDatabase
    }
}