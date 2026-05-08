package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.database

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

        fun register(): Akun {
            return Akun(
                editTextUsername.text.toString(),
                editTextEmail.text.toString(),
                editTextAlamat.text.toString(),
                editTextNoTelp.text.toString(),
                editTextPassword.text.toString()
            )
        }

        btnRegister.setOnClickListener {
            val akunBaru = register()
//            ProfileFragment.dataTitipan = akunBaru
            users.push().setValue(akunBaru)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}