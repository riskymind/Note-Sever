package com.example.repository

import com.example.data.table.NoteTable
import com.example.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun init() {
        // get connection
        val database = Database.connect(hikari())
        transaction(database) {
            // this will create [ UserTable ] if it doesn't exist
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)
        }
    }

    private fun hikari() = HikariDataSource(HikariConfig().apply {
        driverClassName = System.getenv("JDBC_DRIVER")
        jdbcUrl = System.getenv("DATABASE_URL")
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

//
//            val uri = URI(System.getenv("DATABASE_URL"))
//            val username = uri.userInfo.split(":").toTypedArray()[0]
//            val password = uri.userInfo.split(":").toTypedArray()[1]
//
//            jdbcUrl =
//                "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require" + "&user=$username&password=$password"


    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }

}