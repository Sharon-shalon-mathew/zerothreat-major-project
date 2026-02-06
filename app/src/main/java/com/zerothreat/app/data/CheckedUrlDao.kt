package com.zerothreat.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckedUrlDao {
    @Query("SELECT * FROM checked_urls ORDER BY timestamp DESC")
    fun getAllUrls(): Flow<List<CheckedUrl>>
    
    @Query("SELECT * FROM checked_urls WHERE threatLevel != 'SAFE' ORDER BY timestamp DESC")
    fun getThreats(): Flow<List<CheckedUrl>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrl(url: CheckedUrl)
    
    @Query("DELETE FROM checked_urls")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM checked_urls")
    fun getCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM checked_urls WHERE threatLevel = 'PHISHING' OR threatLevel = 'SUSPICIOUS'")
    fun getThreatsCount(): Flow<Int>
}
