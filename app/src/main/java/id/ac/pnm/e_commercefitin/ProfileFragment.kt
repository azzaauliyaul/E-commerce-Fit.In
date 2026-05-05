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
        val Logout = view.findViewById<TextView>(R.id.Logout)
        val username = view.findViewById<TextView>(R.id.textViewUsernameProfile)
        val email = view.findViewById<TextView>(R.id.textViewEmailProfile)
        val alamat = view.findViewById<TextView>(R.id.textViewAddressProfile)
        val noTelp = view.findViewById<TextView>(R.id.textViewPhoneNumberProfile)
        val password = view.findViewById<TextView>(R.id.textViewPasswordProfile)

        val akun = dataTitipan

        fun viewProfile(akun: Akun){
            imageViewProfile.setImageResource(R.drawable.profile)
            username.text = "${akun.username}"
            email.text = "${akun.email}"
            alamat.text = "${akun.alamat}"
            noTelp.text = "${akun.noTelp}"
            password.text = "${akun.password}"
        }
        if (akun != null) {
            viewProfile(akun)
        } else{
            Toast.makeText(requireContext(),"Data Kosong", Toast.LENGTH_SHORT).show()
        }
        imageViewLogout.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    companion object{
        var dataTitipan: Akun? = null
    }
}