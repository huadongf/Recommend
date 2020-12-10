package com.example.recommend

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: NewsData): Long

    @Update
    fun updateUser(newUser: NewsData)

    @Query("select * from NewsData")
    fun loadAllUsers(): List<NewsData>

    @Query("select * from NewsData where idd = :idd")
    fun chazhao(idd: Long): List<NewsData>

    @Delete
    fun deleteUser(user: NewsData)

    @Query("delete from NewsData where idd = :idd")
    fun deleteUserbyidd(idd: Long): Int

    @Query("delete  from NewsData")
    fun del()

    @Query("select * from NewsData where title like'%'||:aa||'%' or src like'%'||:aa||'%' or time like'%'||:aa||'%' or pic like'%'||:aa||'%' or url like'%'||:aa||'%' or channel like'%'||:aa||'%' " )
    fun find(aa:String):List<NewsData>


}