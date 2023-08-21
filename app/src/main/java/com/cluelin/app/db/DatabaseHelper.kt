package com.cluelin.app.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.cluelin.app.utils.Common
import java.util.*


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "my_database.db"
        private const val DATABASE_VERSION = 2
    }

    // Define table and columns
    private val TABLE_NAME_QUESTS = "quests"
    private val TABLE_NAME_QUESTS_LOGS = "quests_logs"
    private val COLUMN_ID = "_id"
    private val COLUMN_QUEST_ID = "quest_id"
    private val COLUMN_TITLE = "_title"
    private val COLUMN_TERM = "_term"
    private val COLUMN_LAST_COMPLETED_TIME = "_last_completed_time"
    private val COLUMN_COMPLETED_TIME = "_completed_time"

    override fun onCreate(db: SQLiteDatabase) {
        // Define your table creation SQL query here
        // For simplicity, let's assume we have a "notes" table
        db.execSQL("CREATE TABLE $TABLE_NAME_QUESTS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_TERM INTEGER, $COLUMN_LAST_COMPLETED_TIME TEXT, UNIQUE($COLUMN_TITLE))")
        db.execSQL("CREATE TABLE $TABLE_NAME_QUESTS_LOGS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_QUEST_ID INTEGER, $COLUMN_COMPLETED_TIME TEXT, FOREIGN KEY($COLUMN_QUEST_ID) REFERENCES $TABLE_NAME_QUESTS($COLUMN_ID))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implement database upgrade logic here if needed
    }

    private fun configureQuestValue(
        title: String,
        term: Int,
        completedTime: String?
    ): ContentValues {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_TERM, term)
            put(COLUMN_LAST_COMPLETED_TIME, completedTime)
        }

        return values
    }

    private fun configureLOGValue(questID: Int, completedTime: String): ContentValues {
        val values = ContentValues().apply {
            put(COLUMN_QUEST_ID, questID)
            put(COLUMN_COMPLETED_TIME, completedTime)
        }

        return values
    }

    fun insertQuest(quest: Quest) {
        val values = this.configureQuestValue(quest.title, quest.term, quest.lastCompletedTime)

        val db = writableDatabase
        db.insert(TABLE_NAME_QUESTS, null, values)
        db.close()
    }

    fun insertQuestLog(questID: Int, completedTime: String) {
        val values = this.configureLOGValue(questID, completedTime)
        val db = writableDatabase
        db.insert(TABLE_NAME_QUESTS_LOGS, null, values)
        db.close()
    }

    // Update an existing note
    fun updateQuest(quest: Quest) {
        val db = writableDatabase
        val values = this.configureQuestValue(quest.title, quest.term, quest.lastCompletedTime)
        db.update(TABLE_NAME_QUESTS, values, "$COLUMN_TITLE=?", arrayOf(quest.title))
        db.close()
    }

    fun updateCompletedTime(questTitle: String, completedTime: String) {


        val quest = getQuestByTitle(questTitle)
        quest?.let {
            val contentValues = ContentValues().apply {
                put(COLUMN_LAST_COMPLETED_TIME, completedTime)
            }
            val db = writableDatabase
            db.update(TABLE_NAME_QUESTS, contentValues, "$COLUMN_TITLE=?", arrayOf(questTitle))

            this.insertQuestLog(it.id, completedTime)

            db.close()
        }
    }

    fun updateTerm(questTitle: String, delta: Int) {
        val quest = getQuestByTitle(questTitle)

        quest?.let {

            val values = ContentValues().apply {
                put(COLUMN_TERM, quest.term + delta)
            }

            val db = writableDatabase
            db.update(TABLE_NAME_QUESTS, values, "$COLUMN_TITLE=?", arrayOf(questTitle))
            db.close()
        }
    }

    // Retrieve a note by ID
    fun getQuestById(id: Int): Quest? {
        val selection = "$COLUMN_ID=?"
        val selectionArgs = arrayOf(id.toString())

        return this.getQuest(selection, selectionArgs)
    }

    fun getQuestByTitle(title: String): Quest? {
        val selection = "$COLUMN_TITLE=?"
        val selectionArgs = arrayOf(title)

        return this.getQuest(selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun getQuest(selection: String, args: Array<String>): Quest? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME_QUESTS,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_TERM, COLUMN_LAST_COMPLETED_TIME),
            selection,
            args,
            null,
            null,
            null
        )

        val quest: Quest? = if (cursor.moveToFirst()) {
            Quest(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_TERM)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LAST_COMPLETED_TIME))
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return quest
    }


    fun getAllQuests(): List<Quest> {
        return this.getQuestList(null)
    }

    fun getAllUnDoneQuests(): List<Quest> {
        val selection =
            "$COLUMN_LAST_COMPLETED_TIME IS null OR " +
                    "CAST(julianday('now') - julianday($COLUMN_LAST_COMPLETED_TIME) AS INTEGER) >= $COLUMN_TERM"

        return this.getQuestList(selection)
    }

    @SuppressLint("Range")
    private fun getQuestList(selection: String?): List<Quest> {
        val quests = mutableListOf<Quest>()
        val db = readableDatabase

        //TODO
//        last completed time이랑 term이랑 비교해서 시간이 줄어들건데, 그럼 우선순위를 위로해주자.
        val cursor: Cursor = db.query(
            TABLE_NAME_QUESTS,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_TERM, COLUMN_LAST_COMPLETED_TIME),
            selection,
            null,
            null,
            null,
            "$COLUMN_LAST_COMPLETED_TIME ," +
                    "$COLUMN_TERM - " +
                    "CASE " +
                    "    WHEN $COLUMN_LAST_COMPLETED_TIME IS NULL THEN 0 " +
                    "    ELSE CAST(julianday('now') - julianday($COLUMN_LAST_COMPLETED_TIME) AS INTEGER)" +
                    "END"

        )
        while (cursor.moveToNext()) {
            val quest = Quest(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_TERM)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LAST_COMPLETED_TIME))
            )
            quests.add(quest)
        }
        cursor.close()
        db.close()
        return quests
    }

    fun getQuestLogListByTitle(questTitle: String): List<QuestLog> {
        val questLogs = mutableListOf<QuestLog>()
        val quest = this.getQuestByTitle(questTitle)
        quest?.let {
            val selection = "$COLUMN_QUEST_ID = ${it.id}"

            questLogs.addAll(this.getQuestLogsList(selection))
        }

        return questLogs
    }

    fun getQuestLogListByTitleAndDays(questTitle: String, days: Int): List<QuestLog> {
        val questLogs = mutableListOf<QuestLog>()
        val quest = this.getQuestByTitle(questTitle)
        quest?.let {
            val selection = "$COLUMN_QUEST_ID = ${it.id} AND " +
                    "CAST(julianday('now') - julianday($COLUMN_COMPLETED_TIME) AS INTEGER) <= $days"

            Log.i(
                Common.TAG,
                "this.getQuestLogsList(selection) : ${this.getQuestLogsList(selection)}"
            )
            questLogs.addAll(this.getQuestLogsList(selection))
        }

        return questLogs
    }

    fun getQuestLogListByID(questId: Int): List<QuestLog> {
        val selection = "$COLUMN_QUEST_ID = $questId"

        return this.getQuestLogsList(selection)
    }

    @SuppressLint("Range")
    private fun getQuestLogsList(selection: String?): List<QuestLog> {
        val questLogs = mutableListOf<QuestLog>()
        val db = readableDatabase

        val cursor: Cursor = db.query(
            TABLE_NAME_QUESTS_LOGS,
            arrayOf(COLUMN_ID, COLUMN_QUEST_ID, COLUMN_COMPLETED_TIME),
            selection,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val questLog = QuestLog(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_QUEST_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETED_TIME))
            )
            questLogs.add(questLog)
        }
        cursor.close()
        db.close()
        return questLogs
    }

    fun removeAllRows() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME_QUESTS")
        db.execSQL("DELETE FROM $TABLE_NAME_QUESTS_LOGS")
        db.close()
    }
}