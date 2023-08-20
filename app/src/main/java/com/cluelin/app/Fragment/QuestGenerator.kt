package com.cluelin.app.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cluelin.app.R
import com.cluelin.app.SharedViewModel
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest


class QuestGenerator : Fragment() {
    val TAG = "injae.jang"

    private lateinit var btnAdd : Button
    private lateinit var etQuestTitle: EditText
    private lateinit var etQuestTerm: EditText

    private lateinit var dataManager: DataManager
    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        dataManager = sharedViewModel.dataManager
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var addFragment = inflater.inflate(R.layout.fragment_quest_add, container, false)

        mappingUI(addFragment)

        btnAdd.setOnClickListener {
            this.insertQuest()
            Toast.makeText(requireContext(), "New Quest Created", Toast.LENGTH_SHORT).show() // 토스트 말고 다른 방식으로 표현하는게 좋을듯.
        }

        return addFragment
    }

    private fun mappingUI(view: View){
        btnAdd = view.findViewById(R.id.btn_add_quest)
        etQuestTitle = view.findViewById(R.id.quest_name)
        etQuestTerm = view.findViewById(R.id.quest_term)
    }


    private fun insertQuest(){
        val questTitle = etQuestTitle.text.toString()
        val questTerm = Integer.parseInt(etQuestTerm.text.toString())

        val quest = Quest(questTitle, questTerm)
        dataManager.insertQuest(quest)

        etQuestTitle.setText("")
        etQuestTerm.setText("")
        Log.i(TAG, "Invoked insertQuest(), title ${questTitle}, term ${questTerm}")
    }


}