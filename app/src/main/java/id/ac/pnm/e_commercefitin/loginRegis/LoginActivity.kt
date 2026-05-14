package id.ac.pnm.e_commercefitin.loginRegis

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
import id.ac.pnm.e_commercefitin.MainActivity
import id.ac.pnm.e_commercefitin.R

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
        val database = Firebase.database
        val users = database.getReference("users")

        buttonLogin.setOnClickListener {
            val email: String = editTextEmail.text.toString().trim()
            val password: String = editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Silahkan isi email dan password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else{
                users.orderByChild("email").equalTo(email).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val dbPassword = userSnapshot.child("password").value.toString()

                            if (dbPassword == password) {
                                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                                val currentUsername = userSnapshot.child("username").value.toString()
                                val intentLoginToMain = Intent(this, MainActivity::class.java)
                                intentLoginToMain.putExtra("currentUsername", currentUsername)
                                intentLoginToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intentLoginToMain)
                            } else {
                                Toast.makeText(
                                    this, "Password Salah, silahkan coba lagi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        register.setOnClickListener {
            val intentToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentToRegister)
        }
    }
}