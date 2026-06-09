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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import id.ac.pnm.e_commercefitin.loginRegis.Akun
import id.ac.pnm.e_commercefitin.loginRegis.LoginActivity
import id.ac.pnm.e_commercefitin.loginRegis.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

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
        imageViewProfile.setImageResource(R.drawable.account)

        val Logout = view.findViewById<TextView>(R.id.Logout)
        val username = view.findViewById<TextView>(R.id.textViewUsernameProfile)
        val email = view.findViewById<TextView>(R.id.textViewEmailProfile)
        val alamat = view.findViewById<TextView>(R.id.textViewAddressProfile)
        val noTelp = view.findViewById<TextView>(R.id.textViewPhoneNumberProfile)
        val password = view.findViewById<TextView>(R.id.textViewPasswordProfile)

        lifecycleScope.launch(Dispatchers.IO) {

            val user = UserDatabase
                .getDatabase(requireContext())
                .userDao()
                .getUser()

            withContext(Dispatchers.Main) {
                if (user != null){
                    username.text = user.username
                    email.text = user.email
                    alamat.text = user.alamat
                    noTelp.text = user.noTelp
                    password.text = user.password
                } else{
                    Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        imageViewLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {

                UserDatabase.getDatabase(requireContext())
                    .userDao()
                    .deleteUser()

                withContext(Dispatchers.Main) {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }
}