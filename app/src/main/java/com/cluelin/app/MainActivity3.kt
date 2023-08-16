package com.cluelin.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cluelin.app.Fragment.QuestGenerator
import com.cluelin.app.Fragment.QuestList
import com.cluelin.app.Fragment.QuestLog
import com.google.android.material.navigation.NavigationBarView

class MainActivity3 : AppCompatActivity() {

    var questList = QuestList()
    var questLog = QuestLog()
    var questGenerator = QuestGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        supportFragmentManager.beginTransaction().replace(R.id.containers, questList).commit()


        var navigation: NavigationBarView = findViewById(R.id.bottom_navigation_view)
        navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.containers, questList)
                        .commit()
                    true
                }

                R.id.log -> {
                    supportFragmentManager.beginTransaction().replace(R.id.containers, questLog)
                        .commit()
                    true
                }
                R.id.add -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containers, questGenerator)
                        .commit()
                    true
                }
                else -> false
            }

        }
    }
}