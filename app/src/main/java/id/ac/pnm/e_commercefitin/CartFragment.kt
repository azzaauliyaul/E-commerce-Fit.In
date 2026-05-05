package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCart)
        val textCount = view.findViewById<TextView>(R.id.textViewCount)
        val btnPesan = view.findViewById<Button>(R.id.buttonPesan)

        btnPesan.setOnClickListener {
            if (dataCart.isEmpty()) {
                Toast.makeText(requireContext(), "Tidak ada product di cart", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checked = dataCart.filter { it.isChecked }
            if (checked.isEmpty()) {
                Toast.makeText(requireContext(), "Checklist product terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val listNama = checked.joinToString { it.Name }
                val totalHarga = checked.sumOf { it.Price }
                val intentChat = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/6282334500709?text=Halo, saya beli product $listNama, dengan total: Rp. $totalHarga"))
                startActivity(intentChat)
            }


        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CartAdapter(dataCart) { total ->
            textCount.text = "Rp. $total"
        }
    }
    companion object{
        var dataCart: MutableList<ItemCart> = mutableListOf()
    }
}