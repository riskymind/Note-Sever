package com.example.repository

import com.example.data.model.Note
import com.example.data.model.User
import com.example.data.table.NoteTable
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class Repo {

    // ======================USER===================

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


    // ================Note===============

    // Add note to DB
    suspend fun addNote(note: Note, email: String) {
        dbQuery {
            NoteTable.insert { noteTable ->
                noteTable[id] = note.id
                noteTable[userEmail] = email
                noteTable[noteTitle] = note.noteTitle
                noteTable[description] = note.description
                noteTable[date] = note.date
            }
        }
    }

    // Update Note
    suspend fun updateNote(note: Note, email: String) {
        dbQuery {
            NoteTable.update(
                where = {
                    NoteTable.userEmail.eq(email) and NoteTable.id.eq(note.id)
                }
            ) { noteTable ->
                noteTable[noteTitle] = note.noteTitle
                noteTable[description] = note.description
                noteTable[date] = note.date
            }
        }
    }

    // Delete Note
    suspend fun deleteNote(id: String, email: String) {
        dbQuery {
            NoteTable.deleteWhere {
                NoteTable.userEmail.eq(email) and NoteTable.id.eq(id)
            }
        }
    }

    // Get all notes from DB of a particular user
    suspend fun getAllNotes(email: String): List<Note> = dbQuery {
        NoteTable.select {
            NoteTable.userEmail.eq(email)
        }.mapNotNull { rowToNote(it) }
    }

    // Convert rowResult to [Note] object
    private fun rowToNote(row: ResultRow?): Note? {
        if (row == null) {
            return null
        }

        return Note(
            id = row[NoteTable.id],
            noteTitle = row[NoteTable.noteTitle],
            description = row[NoteTable.description],
            date = row[NoteTable.date]
        )
    }

}