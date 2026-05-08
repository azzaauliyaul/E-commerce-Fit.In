package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editTextEmail: EditText = findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword: EditText = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val register = findViewById<TextView>(R.id.textViewRegister)
//        val akunLogin = ProfileFragment.dataTitipan
        val database = Firebase.database
        val users =  database.getReference("users")
        val usersFromDb = users.child("email")

        buttonLogin.setOnClickListener {
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()

            usersFromDb.get().addOnSuccessListener {
                val passwordFromDb = it.child("password").toString()

                if (password==passwordFromDb){
                    val intentLoginToMain = Intent(this, MainActivity::class.java)
                    intentLoginToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentLoginToMain)
                } else{
                    Toast.makeText(this, "password salah, silahkan coba lagi", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "email tidak ada", Toast.LENGTH_SHORT).show()
            }
//
//            if(email == akunLogin?.email && password == akunLogin.password){
//                val intentLogin2Main = Intent(this, MainActivity::class.java)
//                intentLogin2Main.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intentLogin2Main)
//            } else {
//                Toast.makeText(this, "Email atau password salah, silahkan coba lagi", Toast.LENGTH_SHORT).show()
//            }
        }
        register.setOnClickListener {
            val intentToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentToRegister)
        }

    }
}