package com.kyoungss.cleaner

interface Repository {

    suspend fun getCurrentPath(): String

    suspend fun getFilePathList() : List<String>
    fun getFileList(): ArrayList<String>
    fun removeFilePath(Path: String)

//    suspend fun getDirectoryPathList() : List<String>
}