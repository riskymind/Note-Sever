package com.example.repository

import com.example.data.model.User
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class Repo {

    // add user to user table
    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[name] = user.userName
                userTable[email] = user.email
                userTable[hashPassword] = user.hashPassword
            }
        }
    }


    // search user by email
    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()

    }

    //convert ResultRow to [ User ] object
    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            email = row[UserTable.email],
            userName = row[UserTable.name],
            hashPassword = row[UserTable.hashPassword]
        )
    }
}