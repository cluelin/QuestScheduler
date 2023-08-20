package com.cluelin.app.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cluelin.app.R
import com.cluelin.app.SharedViewModel
import com.cluelin.app.db.DataManager
import com.cluelin.app.utils.Common
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class QuestLog : Fragment() {
    private lateinit var chart: LineChart
    private lateinit var dataManager: DataManager
    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        TODO
//        quest 활동기록을 이용해서 chart 그리기.
//        stopwatch 구현 후 그래프 업데이트

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        dataManager = sharedViewModel.dataManager


        chart = view.findViewById(R.id.chart)

        val entries = initEntry()
        // Create a LineDataSet with the entries
        val dataSet = LineDataSet(entries, "Sample Data")

        // Customize the appearance of the data set
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

        // Create a LineData object and set the data set
        val lineData = LineData(dataSet)

        // Set the data for the chart
        chart.data = lineData

        // Customize the appearance of the chart
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)




        this.setYAxis()
        this.setXAxis()


        // Refresh the chart to display changes
        chart.invalidate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quest_log, container, false)
        return view
    }

    private fun initEntry() : List<Entry>{
//        val logs: List<QuestLog> = dataManager.getQuestLogListByTitleAndDays("PCSE", 7)
//        val entries : MutableList<Entry> = mutableListOf()
//        for (log in logs){
//            entries.add(Entry(log.completedTime, 1f))
//        }

        // Create sample data entries
        val entries = listOf(
            Entry(-7f, timeToFloat("23:30:00")),
            Entry(-5f, timeToFloat("10:19:00")),
            Entry(-3f, timeToFloat("10:19:00")),
            Entry(0f, timeToFloat("08:19:00")),
        )

        return entries
    }

    private fun setYAxis(){

        // disable dual axis (only use LEFT axis)
        chart.axisRight.isEnabled = false

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        chart.axisLeft.apply {
            valueFormatter = TimeValueFormatter(timeFormat) // Set custom value formatter
            textColor = Color.WHITE
        }
    }

    private fun setXAxis(){

        val xAxis = chart.xAxis

        xAxis.axisMinimum = -7f

        xAxis.position = XAxisPosition.BOTTOM
//        xAxis.typeface = tfLight
        xAxis.setDrawGridLines(false)
//        chart.xAxis.labelRotationAngle = -45f
        xAxis.textColor = Color.WHITE
//        xAxis.granularity = (1000*60*60*24).toFloat() // only intervals of 1 day
        xAxis.granularity = 1f

        xAxis.valueFormatter = DateAXisFormatter()
    }

    private fun timeToFloat(dateStr: String): Float {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date = inputFormat.parse(dateStr)
        val formattedDate = outputFormat.format(date)

        Log.i(Common().TAG, "time : $formattedDate")
        return date.time.toFloat()
    }

    private class TimeValueFormatter(private val timeFormat: SimpleDateFormat) :
        ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return timeFormat.format(value.toLong())
        }
    }

    private class DateAXisFormatter() : ValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()

            calendar.add(Calendar.DAY_OF_YEAR, value.toInt())
            val date = calendar.time

            return dateFormat.format(date)
        }
    }
}