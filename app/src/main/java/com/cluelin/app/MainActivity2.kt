package com.cluelin.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity2 : AppCompatActivity() , OnItemClickListener{

    lateinit var dataManager :DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        dataManager = DataManager(applicationContext)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)


//        dataManager.removeAllRows()
//        dataManager.insertQuest(Quest("Daily Quest", 1))
        dataManager.insertQuest(Quest("청소기,건조기 먼지 정리", 7))
        dataManager.insertQuest(Quest("식기세척기 쓰레기 버리기", 30))


        val questList = dataManager.getAllUnDoneQuests()

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = MyAdapter(questList as MutableList<Quest>, this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(quest: Quest) {

        val currentDateTime: LocalDateTime = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime: String = currentDateTime.format(formatter)

        dataManager.updateCompletedTime(quest.title, formattedDateTime)
    }
}




interface OnItemClickListener {
    fun onItemClick(quest: Quest)
}