package id.ac.pnm.e_commercefitin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import id.ac.pnm.e_commercefitin.loginRegis.Akun

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val database = Firebase.database
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val users =  database.getReference("users")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        val navController = findNavController(R.id.navHost)
        val menu = bottomNavigationView.menu
        if (currentUser != null) {
            val uId = currentUser.uid
            users.child(uId).child("role").get().addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    val role = snapshot.value?.toString() ?: "user"
                    if (role == "admin") {
                        menu.removeItem(R.id.cartFragment)
                    } else {
                        menu.removeItem(R.id.addFragment)
                    }
                    bottomNavigationView.setupWithNavController(navController)
                }
            }
        }
    }
}