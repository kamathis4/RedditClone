package com.adikmt.utils.db

interface DataBaseFactory {
    fun connect()

    suspend fun <T> dbQuery(block: () -> T): T

    suspend fun dropAll()
}

data class DatabaseConfig(
    val driver: String,
    val url: String,
    val username: String,
    val password: String,
    val maxPoolSize: Int,
)