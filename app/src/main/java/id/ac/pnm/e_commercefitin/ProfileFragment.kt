package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import id.ac.pnm.e_commercefitin.loginRegis.Akun
import id.ac.pnm.e_commercefitin.loginRegis.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val imageViewLogout = view.findViewById<ImageView>(R.id.imageViewLogout)
        val imageViewProfile = view.findViewById<ImageView>(R.id.imageView)
        val Logout = view.findViewById<TextView>(R.id.Logout)
        val username = view.findViewById<TextView>(R.id.textViewUsernameProfile)
        val email = view.findViewById<TextView>(R.id.textViewEmailProfile)
        val alamat = view.findViewById<TextView>(R.id.textViewAddressProfile)
        val noTelp = view.findViewById<TextView>(R.id.textViewPhoneNumberProfile)
        val password = view.findViewById<TextView>(R.id.textViewPasswordProfile)

        val database = Firebase.database
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val users = database.getReference("users")
        if (currentUser != null) {
            val uId = currentUser.uid
            users.child(uId).get().addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    val akun = snapshot.getValue(Akun::class.java)
                    akun?.let {
                        username.text = it.username
                        email.text = it.email
                        alamat.text = it.alamat
                        noTelp.text = it.noTelp
                        password.text = it.password
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Gagal memuat, silahkan login kembali", Toast.LENGTH_SHORT).show()
        }

        imageViewLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}