package com.world4tech.godsdecision.fragments

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.world4tech.godsdecision.R
import com.world4tech.godsdecision.adapter.MyAdapter
import com.world4tech.godsdecision.model.Decisions
import com.world4tech.godsdecision.model.viewModel.DecisionsViewModel
import java.text.SimpleDateFormat
import java.util.Date


class HomeFragment : Fragment() {
    private lateinit var itemList:ArrayList<String>
    private lateinit var viewModel :DecisionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(DecisionsViewModel::class.java)

        val newRecyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val adapter = MyAdapter()
        var decision=0
        newRecyclerView.adapter = adapter
        //swipe delete function
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val adapterPosition = viewHolder.adapterPosition
                    val deletedTask: Decisions = adapter.getTaskAt(adapterPosition)
                    viewModel.delDecisions(deletedTask)
                    return
                }
                val taskHolder: MyAdapter.myViewHolder = viewHolder as MyAdapter.myViewHolder
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }).attachToRecyclerView(newRecyclerView)




        newRecyclerView.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(this)[DecisionsViewModel::class.java]
        viewModel.allDecision.observe(viewLifecycleOwner, Observer { list -> adapter.setData(list)
            decision = list.size
//            UpdateUi(decision)
        })





        val btn:ImageView = view.findViewById(R.id.fab)
        val addWish:ConstraintLayout = view.findViewById(R.id.blankLayout)
        val grantedLayout:ConstraintLayout = view.findViewById(R.id.notBlankLayout)
        val decisionText:TextView = view.findViewById(R.id.Option)
        val reconsider:ImageView = view.findViewById(R.id.reconsider)
        val timetable: ImageView = view.findViewById(R.id.timeTable)
        itemList = arrayListOf()

        //Date && time
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val currentDate = sdf.format(Date())
//        println("Date and time current now is: $currentDate")

        btn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val customView = LayoutInflater.from(context).inflate(R.layout.dialogue_layout,null)
            builder.setView(customView)
            val dialog = builder.create()
            dialog.show()
            val addBtn :Button = customView.findViewById(R.id.addBtn)
            val editTxt :EditText = customView.findViewById(R.id.getTask)
            var list:TextView = customView.findViewById(R.id.choiceList)
            val decidebtn:Button = customView.findViewById(R.id.decide)
            var allData:String=""
            var finalData:String=""
            addBtn.setOnClickListener {
               val text = editTxt.text.toString()
                if (text.isNotEmpty()){
                    allData += text
                    itemList.add(text)
                    editTxt.setText("")
                    allData += "\n"
                }else{
                    Toast.makeText(context,"Enter something to choose",Toast.LENGTH_SHORT).show()
                }
                list.text = allData
//                println("Value of store messaed in  allData is: $allData")
            }
            decidebtn.setOnClickListener{
                var size:Int = itemList.size
                var arry : ArrayList<Int> = arrayListOf()
                for(i in 0..<size){
                    arry.add(i)
                };
                for(i in 0..<arry.size){
                    try{
                        val randomno = arry.random()

                        finalData += itemList[randomno]
                        finalData += "\n"
                        arry.remove(randomno)
                    }catch (e:Exception){
                        Log.d("TAG","Error received is: ${e.message}")
                    }
                }
                val totalData = Decisions(0,currentDate,finalData)
                viewModel.addDecisions(totalData)
                decisionText.text = finalData
                addWish.visibility = View.GONE
                grantedLayout.visibility = View.VISIBLE
                dialog.dismiss()
            }
        }
        reconsider.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val customView = LayoutInflater.from(context).inflate(R.layout.dialogue_layout,null)
            builder.setView(customView)
            val dialog = builder.create()
            dialog.show()
            val addBtn :Button = customView.findViewById(R.id.addBtn)
            val editTxt :EditText = customView.findViewById(R.id.getTask)
            var list:TextView = customView.findViewById(R.id.choiceList)
            val decidebtn:Button = customView.findViewById(R.id.decide)
            var allData:String=""
            var finalData:String=""
            addBtn.setOnClickListener {
                val text = editTxt.text.toString()
                if (text.isNotEmpty()){
                    allData += text
                    itemList.add(text)
                    editTxt.setText("")
                    allData += "\n"
                }else{
                    Toast.makeText(context,"Enter something to choose",Toast.LENGTH_SHORT).show()
                }
                list.text = allData
//                println("Value of store messaed in  allData is: $allData")
            }
            decidebtn.setOnClickListener{
                var size:Int = itemList.size
                var arry : ArrayList<Int> = arrayListOf()
                for(i in 0..<size){
                    arry.add(i)
                };
                for(i in 0..<arry.size){
                    try{
                        val randomno = arry.random()

                        finalData += itemList[randomno]
                        finalData += "\n"
                        arry.remove(randomno)
                    }catch (e:Exception){
                        Log.d("TAG","Error received is: ${e.message}")
                    }
                }
                val totalData = Decisions(0,currentDate,finalData)
                viewModel.addDecisions(totalData)
                decisionText.text = finalData
                addWish.visibility = View.GONE
                grantedLayout.visibility = View.VISIBLE
                dialog.dismiss()
            }
        }
        timetable.setOnClickListener {
            val recordFragment =RecordFragment()
            makeCurrentFragment(recordFragment)
        }


        return view
    }
    private fun makeCurrentFragment(fragment: Fragment)  = requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(R.id.fr_wrapper,fragment)
        commit()
    }

}