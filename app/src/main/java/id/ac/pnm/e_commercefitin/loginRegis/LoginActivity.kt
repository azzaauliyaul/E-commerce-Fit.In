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
    import androidx.lifecycle.lifecycleScope
    import com.google.firebase.Firebase
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.auth
    import com.google.firebase.database.database
    import id.ac.pnm.e_commercefitin.MainActivity
    import id.ac.pnm.e_commercefitin.R
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext

    class LoginActivity : AppCompatActivity() {

        private lateinit var auth: FirebaseAuth

        override fun onStart() {
            super.onStart()
            lifecycleScope.launch(Dispatchers.IO) {
                val user = UserDatabase.getDatabase(this@LoginActivity).userDao().getUser()
                if (user != null) {
                    withContext(Dispatchers.Main) {
                        val intentLoginToMain = Intent(this@LoginActivity, MainActivity::class.java)
                        intentLoginToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intentLoginToMain)
                        finish()
                    }
                }
            }
        }

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
            auth = Firebase.auth

            buttonLogin.setOnClickListener {
                val email: String = editTextEmail.text.toString().trim()
                val password: String = editTextPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Silahkan isi email dan password", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else{
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){

                            val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                            Firebase.database.reference
                                .child("users")
                                .child(uid)
                                .get()
                                .addOnSuccessListener { snapshot ->

                                    val user = UserEntity(
                                        uid = uid,
                                        username = snapshot.child("username").getValue(String::class.java) ?: "",
                                        email = snapshot.child("email").getValue(String::class.java) ?: "",
                                        alamat = snapshot.child("alamat").getValue(String::class.java) ?: "",
                                        noTelp = snapshot.child("noTelp").getValue(String::class.java) ?: "",
                                        password = snapshot.child("password").getValue(String::class.java) ?: "",
                                        role = snapshot.child("role").getValue(String::class.java) ?: ""
                                    )

                                    lifecycleScope.launch(Dispatchers.IO) {

                                        UserDatabase
                                            .getDatabase(this@LoginActivity)
                                            .userDao()
                                            .insertUser(user)

                                        withContext(Dispatchers.Main) {

                                            val intentLoginToMain = Intent(this@LoginActivity, MainActivity::class.java)
                                            intentLoginToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intentLoginToMain)
                                            finish()
                                        }
                                    }
                                }


                        } else{
                            Toast.makeText(this, "Login Gagal. Harap coba lagi.", Toast.LENGTH_SHORT).show()
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