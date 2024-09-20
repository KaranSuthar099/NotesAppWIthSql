package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SqliteDatabase(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "NOTES"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "NOTES_TABLE"
        private const val ID = "ID"
        private const val TITLE = "TITLE"
        private const val CONTENT = "CONTENT"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$TITLE TEXT, $CONTENT TEXT) ")
        Log.d("SQL", "Table Created")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("Drop Table $TABLE_NAME")
        onCreate(db)
    }

    fun addNote(title:String, content:String) {
        val values = ContentValues().apply {
            put(TITLE, title)
            put(CONTENT, content)
        }
        writableDatabase.insert(TABLE_NAME, null, values)
        Log.d("SQL", "Note Added successfully")
    }

    fun readData(): List<NoteModal> {
        val db = this.readableDatabase
        val notesList = mutableListOf<NoteModal>()

        // Corrected query method parameters
        val cursor = db.query(
            TABLE_NAME,             // Table name
            arrayOf(ID, TITLE, CONTENT), // Columns to retrieve
            null,                   // Selection (null means no filter)
            null,                   // Selection arguments (null means no filter args)
            null,                   // Group by (null means no grouping)
            null,                   // Having (null means no post-grouping filter)
            null                    // Order by (null means no specific ordering)
        )

        // Check if cursor has results
        if (cursor.moveToFirst()) {
            do {
                // Extract data from cursor
                val id = cursor.getString(cursor.getColumnIndex(ID)).toInt()
                val title = cursor.getString(cursor.getColumnIndex(TITLE))
                val content = cursor.getString(cursor.getColumnIndex(CONTENT))

                // Add to list
                notesList.add(NoteModal(id, title, content))
            } while (cursor.moveToNext())
        }

        // Close the cursor to free up resources
        cursor.close()
        return notesList
    }

    fun deleteNote(note:NoteModal){
        this.writableDatabase.delete(TABLE_NAME, "$ID = ?", arrayOf(note.id.toString()))
    }

    fun updateNote(note: NoteModal) {
        val values = ContentValues()
        values.put(TITLE, note.title)
        values.put(CONTENT, note.content)
        this.writableDatabase.update(TABLE_NAME, values, "$ID = ?", arrayOf(note.id.toString()))

    }
}