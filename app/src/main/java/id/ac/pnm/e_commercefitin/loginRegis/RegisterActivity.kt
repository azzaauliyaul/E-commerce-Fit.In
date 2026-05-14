package id.ac.pnm.e_commercefitin.loginRegis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.database
import id.ac.pnm.e_commercefitin.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editTextUsername = findViewById<EditText>(R.id.editTextTextUsername)
        val editTextEmail = findViewById<EditText>(R.id.editTextTextEmailAddressRegis)
        val editTextAlamat = findViewById<EditText>(R.id.editTextAlamat)
        val editTextNoTelp = findViewById<EditText>(R.id.editTextPhone)
        val editTextPassword = findViewById<EditText>(R.id.editTextTextPasswordRegis)
        val btnRegister = findViewById<Button>(R.id.buttonRegister)
        val database = Firebase.database
        val users =  database.getReference("users")

        btnRegister.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val alamat = editTextAlamat.text.toString().trim()
            val noTelp = editTextNoTelp.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap melengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val akunBaru = Akun(username, email, alamat, noTelp, password)
            users.child(akunBaru.username).setValue(akunBaru)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}