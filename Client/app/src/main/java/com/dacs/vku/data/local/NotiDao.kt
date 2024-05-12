//@file:Suppress("AndroidUnresolvedRoomSqlReference")
//
//package com.dacs.vku.data.local
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.dacs.vku.domain.model.NotiItem
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface NotiDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsert(noti: NotiItem)
////
////    @Delete
////    suspend fun delete(noti: NotiItem)
//
//    @Query("SELECT * FROM noti")
//    fun getNoti(): Flow<List<NotiItem>>
//
//}