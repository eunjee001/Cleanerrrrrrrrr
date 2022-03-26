package com.kyoungss.cleaner.data

interface Repository {

    suspend fun getCurrentPath(): String

    suspend fun getFilePathList() : List<String>

//    suspend fun getDirectoryPathList() : List<String>
}