package com.example.todo_list1


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todo_list1.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users")

        binding.login.setOnClickListener {
            val username = binding.email.text.toString()
            val password = binding.password.text.toString()


            if (username.isNotEmpty() && password.isNotEmpty()){
                loginUser(username, password)
            }else {
                Toast.makeText(this@Login, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpRedirect.setOnClickListener {
            startActivity(Intent(this@Login, Signup::class.java))
            finish()
        }
    }

    private fun loginUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.exists()){
                    for (userSnapshot in datasnapshot.children){
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData !=null && userData.password == password){
                            Toast.makeText(this@Login, "Login Successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Login,MainActivity::class.java))
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@Login, "Login field", Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(databaseerror: DatabaseError) {
                Toast.makeText(this@Login, "Database Error: ${databaseerror.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}