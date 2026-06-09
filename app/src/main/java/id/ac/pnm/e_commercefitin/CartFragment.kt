package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.pnm.e_commercefitin.Cart.CartAdapter
import id.ac.pnm.e_commercefitin.Cart.AppDatabase
import id.ac.pnm.e_commercefitin.loginRegis.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.recyclerViewCart)

        val textCount =
            view.findViewById<TextView>(R.id.textViewCount)

        val btnPesan =
            view.findViewById<Button>(R.id.buttonPesan)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val adapter =
            CartAdapter(mutableListOf()) { total ->
                textCount.text = "Rp. $total"
            }

        recyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {

            val user = UserDatabase
                .getDatabase(requireContext())
                .userDao()
                .getUser()


            if (user == null) return@launch

            launch(Dispatchers.Main) {
            AppDatabase
                .getDatabase(requireContext())
                .cartDao()
                .getAllCart(user.uid)
                .observe(viewLifecycleOwner) { cartList ->

                    adapter.updateData(cartList)

                    val total = cartList
                        .filter { it.isChecked }
                        .sumOf { it.price ?: 0 }

                    textCount.text = "Rp. $total"
                }
            }
        }
        btnPesan.setOnClickListener {

            val currentData =
                adapter.data

            if (currentData.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Tidak ada product di cart",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val checked =
                currentData.filter { it.isChecked }

            if (checked.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Checklist product terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val listNama =
                checked.joinToString { it.name }

            val totalHarga =
                checked.sumOf { it.price ?: 0 }

            val intentChat = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://wa.me/6282334500709?text=Halo, saya beli product $listNama, dengan total: Rp. $totalHarga"
                )
            )

            startActivity(intentChat)
        }
    }
}