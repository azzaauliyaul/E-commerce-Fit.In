package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import id.ac.pnm.e_commercefitin.Catalog.Catalog
import id.ac.pnm.e_commercefitin.Catalog.CatalogAdapter
import id.ac.pnm.e_commercefitin.DetailProduct.DetailProductActivity
import id.ac.pnm.e_commercefitin.DetailProduct.DetailProductAdmin

class HomeFragment : Fragment() {

    private lateinit var adapter: CatalogAdapter
    private lateinit var internetStatus: TextView
    private lateinit var connectivityManager: ConnectivityManager
    private val productList = mutableListOf<Catalog>()
    private val networkCallback =
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: android.net.Network) {

                activity?.runOnUiThread {

                    internetStatus.visibility = View.GONE
                }
            }

            override fun onLost(network: android.net.Network) {

                activity?.runOnUiThread {

                    internetStatus.visibility = View.VISIBLE
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        internetStatus =
            view.findViewById(R.id.textInternetStatus)

        connectivityManager =
            requireContext().getSystemService(
                ConnectivityManager::class.java
            )

        adapter = CatalogAdapter(productList, ::openDetailProduct)
        val textViewUsername = view.findViewById<TextView>(R.id.textViewUsernameProfile)
        textViewUsername.text = "Hey there"
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCatalog)
        recyclerView.adapter = adapter
        val filterCategory = view.findViewById<ImageView>(R.id.filterCategory)

        getCatalog()

        filterCategory.setOnClickListener { anchor ->
            val popup = PopupMenu(requireContext(), anchor)
            val daftarKategoriUnik = productList
                .map { it.category }
                .filter { it.isNotEmpty() }
                .distinct()
                .sorted()
            popup.menu.add(0, 0, 0, "Semua Produk")
            daftarKategoriUnik.forEachIndexed { index, namaKategori ->
                popup.menu.add(0, index + 1, index + 1, namaKategori)
            }
            popup.setOnMenuItemClickListener { item ->
                if (item.itemId == 0) {
                    adapter.filterCategory(null)
                } else {
                    val selectedCategory = item.title.toString()
                    adapter.filterCategory(selectedCategory)
                }
                true
            }
            popup.show()
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterSearch(newText ?: "")
                return true
            }
        })



    }

    override fun onResume() {
        super.onResume()

        connectivityManager.registerDefaultNetworkCallback(
            networkCallback
        )

        internetStatus.visibility =
            if (isInternetAvailable())
                View.GONE
            else
                View.VISIBLE
    }
    override fun onPause() {
        super.onPause()

        connectivityManager.unregisterNetworkCallback(
            networkCallback
        )
    }

    fun getCatalog(){
        val database = Firebase.database
        val productFromDb = database.getReference("product")
        productFromDb.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Catalog::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                adapter.updateData(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
     fun openDetailProduct(catalog: Catalog) {
         val auth = Firebase.auth
         val database = Firebase.database
         val currentUSer = auth.currentUser
         val users = database.getReference("users")
         if (currentUSer != null) {
             val uId = currentUSer.uid
             users.child(uId).child("role").get().addOnSuccessListener { snapshot ->
                 if(snapshot.exists()) {
                     val role = snapshot.value?.toString() ?: "user"
                     if (role == "admin") {
                         val intentMainToDetail = Intent(requireContext(), DetailProductAdmin::class.java)
                         intentMainToDetail.putExtra("productId", catalog.productID)
                         startActivity(intentMainToDetail)
                     } else {
                         val intentMainToDetail = Intent(requireContext(), DetailProductActivity::class.java)
                         intentMainToDetail.putExtra("productId", catalog.productID)
                         startActivity(intentMainToDetail)
                     }
                 }
             }
         }
    }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager =
            requireContext().getSystemService(
                ConnectivityManager::class.java
            )

        val network =
            connectivityManager.activeNetwork ?: return false

        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }
}