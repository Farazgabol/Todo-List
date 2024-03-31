package com.example.todo_list1



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.todo_list1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users")

        binding.login.setOnClickListener {
            val username = binding.email.text.toString()
            val password = binding.password.text.toString()


            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "Signin successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
        var txtSignup = findViewById<TextView>(R.id.txtSignup)
        txtSignup.setOnClickListener {
            intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }
    }
}

