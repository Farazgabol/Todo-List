package com.example.todo_list1

import MyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var data: MutableList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        data = mutableListOf()
        recyclerView = findViewById(R.id.recyclerView)
        adapter = MyAdapter(data)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            showEditTextDialog()
        }
    }

    private fun showEditTextDialog() {
        val dialog = EditDialogFragment(object : EditDialogFragment.SubmitListener {
            override fun onSubmit(task: String, description: String) {


                data.add(Item(task, description))
                adapter.notifyDataSetChanged()
            }
        })
        dialog.show(supportFragmentManager, "EditDialogFragment")
    }
}
