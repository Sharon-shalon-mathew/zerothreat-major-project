package com.zerothreat.app.data

import kotlinx.coroutines.flow.Flow

class UrlRepository(private val dao: CheckedUrlDao) {
    val allUrls: Flow<List<CheckedUrl>> = dao.getAllUrls()
    val threats: Flow<List<CheckedUrl>> = dao.getThreats()
    val urlCount: Flow<Int> = dao.getCount()
    val threatsCount: Flow<Int> = dao.getThreatsCount()
    
    suspend fun insertUrl(url: CheckedUrl) {
        dao.insertUrl(url)
    }
    
    suspend fun clearAll() {
        dao.deleteAll()
    }
}
