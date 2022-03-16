package com.example.android.postit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.NavUtils
import com.example.android.postit.daos.UserDao
import com.example.android.postit.models.User
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<Button>(R.id.sign_up).setOnClickListener {
            signUp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.sign_up_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun signUp(){

        val name = findViewById<EditText>(R.id.username).text.toString()
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password_sign_up).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.password_Confirm).text.toString()
        auth = Firebase.auth

        if(name.isEmpty()){
            Toast.makeText(this,"please enter Username ",Toast.LENGTH_SHORT).show()
        }
        if(email.isEmpty()){
            Toast.makeText(this,"Enter Email ", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            Toast.makeText(this,"Enter Password ", Toast.LENGTH_SHORT).show()
        }
        else if(password.length <= 8){
            Toast.makeText(this,"password length must be greater than 8",Toast.LENGTH_SHORT).show()
        }
        else if(confirmPassword.isEmpty()){
            Toast.makeText(this,"Enter Password ", Toast.LENGTH_SHORT).show()
        }
        else if(password != confirmPassword){
            Toast.makeText(this,"Confirm Password should as Password", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email.trim(),password).addOnCompleteListener(this){
                    task ->
                if(task.isSuccessful){
                    val user=auth.currentUser
                    updateSignUpUI(user, name)
                }
                else{
                    if(task.exception.toString()=="com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."){
                        Toast.makeText(this,"Email address already in use", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "check your email and password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @DelicateCoroutinesApi
    fun updateSignUpUI(firebaseUser: FirebaseUser?, username: String) {
        if(firebaseUser != null) {
            val imageUrl = "https://firebasestorage.googleapis.com/v0/b/postit-2ea84.appspot.com/o/account_circle.png?alt=media&token=86fc2db5-b1a7-4646-8b21-374f163c9a88"
            val user = User(firebaseUser.uid, username, imageUrl)
            val userDao = UserDao()
            userDao.addUser(user)


            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        } else {
            findViewById<SignInButton>(R.id.signInButton).visibility = View.VISIBLE
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
        }
    }
}