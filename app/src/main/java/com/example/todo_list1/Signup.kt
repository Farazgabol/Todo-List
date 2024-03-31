package com.example.todo_list1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todo_list1.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        // Reference to the "Users" node in your database
        databaseReference = firebaseDatabase.reference.child("Users")

        auth = FirebaseAuth.getInstance()
        // Button click listener for sign up
        binding.signUp.setOnClickListener {
            val username = binding.sUsername.text.toString()
            val password = binding.sPassword.text.toString()


            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
            }else {
                auth.createUserWithEmailAndPassword(username,password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@Signup, "Signup Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Signup, Login::class.java))
                            finish()
                            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Authentication field.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

                fun onCancelled(databaseerror: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@Signup, "Database Error: ${databaseerror.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

