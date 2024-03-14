package com.example.todo_list1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todo_list1.databinding.ActivitySignupBinding
import com.google.firebase.database.*

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        // Reference to the "Users" node in your database
        databaseReference = firebaseDatabase.reference.child("Users")

        // Button click listener for sign up
        binding.signUp.setOnClickListener {
            val username = binding.sUsername.text.toString()
            val password = binding.sPassword.text.toString()


            if (username.isNotEmpty() && password.isNotEmpty()){
                signUpUser(username, password)
            }else {
                Toast.makeText(this@Signup, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

       binding.loginRedirect.setOnClickListener {
           startActivity(Intent(this@Signup, Login::class.java))
       }
    }

    private fun signUpUser(username: String, password: String) {
        // Check if the username already exists
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        // If username doesn't exist, create a new user
                        val id = databaseReference.push().key
                        val userData = UserData(id, username, password)
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(this@Signup, "Signup Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Signup, Login::class.java))
                        finish()
                    } else {
                        // If username already exists, show error message
                        Toast.makeText(this@Signup, "Username already exists", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseerror: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@Signup, "Database Error: ${databaseerror.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
