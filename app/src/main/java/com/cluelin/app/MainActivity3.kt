package com.cluelin.app

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cluelin.app.Fragment.QuestGenerator
import com.cluelin.app.Fragment.QuestList
import com.cluelin.app.Fragment.QuestLogFragment
import com.google.android.material.navigation.NavigationBarView
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest

class MainActivity3 : AppCompatActivity() {

    var questListFragment = QuestList()
    var questLogFragment = QuestLogFragment()
    var questGenerator = QuestGenerator()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        setViewModel()
        setFragment()
    }

    private fun setViewModel(){
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.dataManager = DataManager(this)

        val questList = sharedViewModel.dataManager.getAllUnDoneQuests()
        sharedViewModel.questAdapter = QuestAdapter(questList, questListFragment)
    }

    private fun setFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.containers, questListFragment).commit()

        var navigation: NavigationBarView = findViewById(R.id.bottom_navigation_view)
        navigation.setOnItemSelectedListener { item ->
            rotateFragments(item)
        }
    }

    private fun rotateFragments(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction().replace(R.id.containers, questListFragment).commit()
                return true
            }
            R.id.log -> {
                supportFragmentManager.beginTransaction().replace(R.id.containers, questLogFragment).commit()
                return true
            }
            R.id.add -> {
                supportFragmentManager.beginTransaction().replace(R.id.containers, questGenerator).commit()
                return true
            }
            else -> return false
        }
    }
}