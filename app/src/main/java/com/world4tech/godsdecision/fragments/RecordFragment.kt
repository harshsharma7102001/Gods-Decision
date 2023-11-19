package com.world4tech.godsdecision.fragments

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.world4tech.godsdecision.R
import com.world4tech.godsdecision.databinding.FragmentRecordBinding
import com.world4tech.godsdecision.model.viewModel.DecisionsViewModel
import com.world4tech.godsdecision.services.AlarmReceiver
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class RecordFragment : Fragment() {
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    private val client =  OkHttpClient()
    private lateinit var mViewModel : DecisionsViewModel
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        val floatBtn: ImageView = binding.fab
        var inputOptions:String = ""
        var inputHrs:String=""

        createNotificationChannel()

        binding.reload.setOnClickListener {
            performTask()
        }
        floatBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val customView = LayoutInflater.from(context).inflate(R.layout.dialogue_layout_gpt,null)
            builder.setView(customView)
            val dialog = builder.create()
            dialog.show()
            val addBtn : Button = customView.findViewById(R.id.addBtn)
            val editTxt : EditText = customView.findViewById(R.id.getTask)
            var list: TextView = customView.findViewById(R.id.choiceList)
            val decidebtn: Button = customView.findViewById(R.id.decide)
            val hrsStart:EditText = customView.findViewById(R.id.startTime)
            val hrsEnd:EditText = customView.findViewById(R.id.endTime)
            val check:TextView = customView.findViewById(R.id.check)
            val checkOne:TextView = customView.findViewById(R.id.checkone)
            var allData:String=""
            var checkInt = true;

            check.setOnClickListener {
                if(!checkInt){
                    check.text = "pm"
                    checkInt=!checkInt
                }
            }
            var checkOneInt = true;
            checkOne.setOnClickListener {
                if(!checkOneInt){
                    checkOne.text = "am"
                    checkOneInt = !checkOneInt
                }
            }
            addBtn.setOnClickListener {
                val text = editTxt.text.toString()
                if (text.isNotEmpty()){
                    allData += text
                    editTxt.setText("")
                    allData += "\n"
                }else{
                    Toast.makeText(context,"Enter something to choose", Toast.LENGTH_SHORT).show()
                }
                list.text = allData
                inputOptions= allData
            }

            decidebtn.setOnClickListener{
               val beginTime = hrsStart.text.toString()
                val endTime = hrsEnd.text.toString()
                if(beginTime.isNotEmpty() && endTime.isNotEmpty()){
                    binding.yourTasks.text = inputOptions
                    val question = "Generate a best time table of subjects $inputOptions from $beginTime ${check.text} to $endTime ${checkOne.text} hrs considering important factors first"
                    try{
                        getResponse(question){response->
                            requireActivity().runOnUiThread {
                                binding.aiResponse.text = response
                            }
                        }
                    }catch(e:Exception){
                        Log.d("Error","${e.message}")
                    }
                    binding.emptyLayout.visibility = View.GONE
                    binding.notEmptyLayout.visibility = View.VISIBLE
                }else{
                    Toast.makeText(context, "Kindly select your time",Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()

            }
        }
        binding.setAlarm.setOnClickListener {
            openTimePicker()
        }



        return binding.root
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name:CharSequence = "GodsDecision"
            val desctiption  = "Alarm Manager"
            val importance =NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("GodsDecision",name,importance)
            channel.description=desctiption
            val notificaionManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificaionManager.createNotificationChannel(channel)
        }
    }

    private fun openTimePicker() {
        val isSystem24Hrour = is24HourFormat(requireContext())
        val clockFormat = if(isSystem24Hrour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set Alarm")
            .build()
        picker.show(childFragmentManager,"TAG")
        picker.addOnPositiveButtonClickListener{
            val h = picker.hour
            val min = picker.minute
            alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
            OnToggleClicked(h,min)

        }
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
        picker.addOnCancelListener {
            picker.dismiss()
        }
    }

    private fun performTask() {
        var inputOptions:String = ""
        var inputHrs:String=""
        val builder = AlertDialog.Builder(context)
        val customView = LayoutInflater.from(context).inflate(R.layout.dialogue_layout_gpt,null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        val addBtn : Button = customView.findViewById(R.id.addBtn)
        val editTxt : EditText = customView.findViewById(R.id.getTask)
        var list: TextView = customView.findViewById(R.id.choiceList)
        val decidebtn: Button = customView.findViewById(R.id.decide)
        val hrsStart:EditText = customView.findViewById(R.id.startTime)
        val hrsEnd:EditText = customView.findViewById(R.id.endTime)
        val check:TextView = customView.findViewById(R.id.check)
        val checkOne:TextView = customView.findViewById(R.id.checkone)
        var allData:String="➢"

        var checkInt = true;
        check.setOnClickListener {
            if(!checkInt){
                check.text = "pm"
                checkInt=!checkInt
            }
            checkInt  = !checkInt
        }
        var checkOneInt = true;
        checkOne.setOnClickListener {
            if(!checkOneInt){
                checkOne.text = "am"
                checkOneInt = !checkOneInt
            }
            checkOneInt = !checkOneInt
        }
        addBtn.setOnClickListener {
            val text = editTxt.text.toString()
            if (text.isNotEmpty()){
                allData += text
                editTxt.setText("")
                allData += "\n➢"
            }else{
                Toast.makeText(context,"Enter something to choose", Toast.LENGTH_SHORT).show()
            }
            list.text = allData
            inputOptions= allData
        }

        decidebtn.setOnClickListener{
            val beginTime = hrsStart.text.toString()
            val endTime = hrsEnd.text.toString()
            if(beginTime.isNotEmpty() && endTime.isNotEmpty()){
                binding.yourTasks.text = inputOptions
                val question = "Generate a best time table of subjects $inputOptions from $beginTime ${check.text} to $endTime ${checkOne.text} hrs considering important factors first"
                try{
                    getResponse(question){response->
                        requireActivity().runOnUiThread {
                            binding.aiResponse.text = response
                        }
                    }
                }catch(e:Exception){
                    Log.d("Error","${e.message}")
                }
                binding.emptyLayout.visibility = View.GONE
                binding.notEmptyLayout.visibility = View.VISIBLE
            }else{
                Toast.makeText(context, "Kindly select your time",Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()

        }
    }

    fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey:String = "------Add your api key here--------------"
        val url="https://api.openai.com/v1/completions"
        val requestBody= JSONObject()
        try{
            requestBody.put("model","text-davinci-003")
            requestBody.put("prompt","$question")
            requestBody.put("max_tokens",4000)
            requestBody.put("temperature",0)
        }catch (e:Exception){
            Log.d("Error","${e.message}")
        }
        val body:RequestBody = RequestBody.create(JSON,requestBody.toString())
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error","API failed",e)
            }

            override fun onResponse(call: Call, response: Response) {
                var body=response.body?.string()
                if (body != null) {
                    Log.v("data",body)
                }
                else{
                    Log.v("data","empty")
                }
                try{
                    val jsonObject= JSONObject(body)
                    val jsonArray: JSONArray =jsonObject.getJSONArray("choices")
                    val textResult=jsonArray.getJSONObject(0).getString("text")
                    callback(textResult)
                }catch (e:Exception){
                    Log.d("Error","${e.message}")
                }
            }
        })

    }
    companion object{
        val JSON : MediaType = "application/json; charset=utf-8".toMediaType()
    }

    fun OnToggleClicked(hour:Int,min:Int) {
        var time: Long
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        val intent = Intent(context, AlarmReceiver::class.java)

        // we call broadcast using pendingIntent
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        time = calendar.getTimeInMillis() - calendar.getTimeInMillis() % 60000
        if (System.currentTimeMillis() > time) {
            time =
                if (Calendar.AM_PM === 0) time + 1000 * 60 * 60 * 12 else time + 1000 * 60 * 60 * 24
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent)
        Handler().postDelayed(object:Runnable{
            override fun run() {
                alarmManager.cancel(pendingIntent);
            }
        },1100)
    }

}