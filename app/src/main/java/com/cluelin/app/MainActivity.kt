package com.cluelin.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TAG = "FORLOG"


        val questBtn = findViewById<Button>(R.id.display_quest)
        // To insert a new note
        val dataManager = DataManager(this.applicationContext)

        questBtn.setOnClickListener {
            // To retrieve all notes
            val quests = dataManager.getAllQuests()
            Log.e(TAG, "클릭 시작")
            Log.e(TAG, quests.toString())


            var unDoneQuests = dataManager.getAllUnDoneQuests()
            Log.e(TAG, unDoneQuests.toString())

            dataManager.updateCompletedTime("Daily Quest", "2023-08-07 8:07:22")
            dataManager.updateCompletedTime("Daily Quest", "2023-08-08 9:07:22")

            unDoneQuests = dataManager.getAllUnDoneQuests()
            Log.e(TAG, unDoneQuests.toString())


            val questLogs = dataManager.getQuestLogListByTitle("Daily Quest")
            Log.e(TAG, questLogs.toString())

//            dataManager.removeAllRows()

        }


        dataManager.insertQuest(Quest("Daily Quest", 1, "2023-08-06 10:07:22"))
        dataManager.insertQuest(Quest("Week Quest", 7, "2023-08-07 10:07:22"))


    }
}
