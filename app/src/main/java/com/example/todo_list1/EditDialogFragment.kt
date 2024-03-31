package com.example.todo_list1

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditDialogFragment(private val listener: SubmitListener) : DialogFragment() {

    private lateinit var myRecord: DatabaseReference
    private lateinit var auth: FirebaseAuth

    interface SubmitListener {
        fun onSubmit(data1: String, data2: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        auth = FirebaseAuth.getInstance()
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.layout_edit_text, null)

        val editText1 = view.findViewById<EditText>(R.id.editText1)
        val editText2 = view.findViewById<EditText>(R.id.editText2)
        val submitBtn = view.findViewById<Button>(R.id.submitBtn)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        val dialog = builder.create()

        submitBtn.setOnClickListener {
            val task = editText1.text.toString()
            val description = editText2.text.toString()
            val currentUser = auth.currentUser

            if (currentUser != null) {
                val userId = currentUser.uid
                myRecord = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("todo_lists")
                val todoRecord = Item(task, description)
                myRecord.push().setValue(todoRecord)

                if (task.isNotEmpty() && description.isNotEmpty()) {
                    listener.onSubmit(task, description)
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Task Successfully listed", Toast.LENGTH_SHORT).show()
                } else {
                    // Show error message if EditTexts are empty
                    Toast.makeText(requireContext(), "Please enter data in both fields", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show error message if user is not authenticated
                Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        }

        return dialog
    }
}
