package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CatalogAdapter(getCatalog(), ::openDetailProduct)
        val textViewUsername = view.findViewById<TextView>(R.id.textViewUsernameProfile)
        textViewUsername.text = "Hey there"
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCatalog)
        recyclerView.adapter = adapter
        val filterCategory = view.findViewById<ImageView>(R.id.filterCategory)
        val filterColor = view.findViewById<ImageView>(R.id.filterColor)



        filterCategory.setOnClickListener { anchor ->
            val popup = PopupMenu(requireContext(), anchor)
            popup.menuInflater.inflate(R.menu.toolbar_category, popup.menu)
            // event klik item
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.semua -> adapter.filterCategory(null)
                R.id.jaket -> adapter.filterCategory(Category.Jaket)
                R.id.kemeja -> adapter.filterCategory(Category.Kemeja)
                R.id.knit -> adapter.filterCategory(Category.Knit)
                R.id.cardigan -> adapter.filterCategory(Category.Cardigan)
                R.id.dasi -> adapter.filterCategory(Category.Dasi)
                R.id.celana -> adapter.filterCategory(Category.Celana)
                else -> false
            }
            true
        }
        popup.show()
    }

        filterColor.setOnClickListener { anchor ->
            val popup = PopupMenu(requireContext(), anchor)
            popup.menuInflater.inflate(R.menu.toolbar_color, popup.menu)
            // event klik item
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.Semua -> adapter.filterColor(null)
                    R.id.Hitam -> adapter.filterColor(Color.Hitam)
                    R.id.Putih -> adapter.filterColor(Color.Putih)
                    R.id.Cream -> adapter.filterColor(Color.Cream)
                    R.id.Hijau -> adapter.filterColor(Color.Hijau)
                    R.id.Biru -> adapter.filterColor(Color.Biru)
                    else -> false
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
    fun getCatalog(): List<Catalog> {
        val data = mutableListOf<Catalog>()
        data.add(Catalog(
            R.drawable.celana,
            "Cattwin Reworked pants",
            250000,
            "Celana baggy jeans dengan bahan yang ringan",
            Color.Hitam,
            Category.Celana
        ))
        data.add(Catalog(
            R.drawable.knit,
            "Totoro day's",
             200000,
            "knitt dirajut dengan benang woll dan motif unik",
            Color.Hijau,
            Category.Knit
        ))
        data.add(Catalog(
            R.drawable.jaket,
            "Memorable autumn in collage",
            250000,
            "jaket boxy terbuat dari bahan kulit tebal",
            Color.Cream,
            Category.Jaket
        ))
        data.add(Catalog(
            R.drawable.kemeja,
            "Fragmenta caeli",
            200000,
            "Kemeja berbahan fanell dengan motif yang unik",
            Color.Hijau,
            Category.Kemeja
        ))
        data.add(Catalog(
            R.drawable.dasi,
            "Dasi Mix Collection",
            50000,
            "Dasi memiliki panjang 30cm dengan motif bunga",
            Color.Biru,
            Category.Dasi
        ))
        data.add(Catalog(
            R.drawable.cardigan,
            "Doctorium cardigan",
            150000,
            "cardigan terbuat dari bahan wol dengan ketebalan ..",
            Color.Cream,
            Category.Cardigan
        ))
        return data
    }

     fun openDetailProduct(catalog: Catalog) {
        val intentMainToDetail = Intent(requireContext(), DetailProductActivity::class.java)
        intentMainToDetail.putExtra("judul", catalog.Name)
        intentMainToDetail.putExtra("img", catalog.Image)
        intentMainToDetail.putExtra("harga", catalog.Price)
        intentMainToDetail.putExtra("deskripsi", catalog.Deskripsi)
        intentMainToDetail.putExtra("kategori", catalog.Category.name)
        intentMainToDetail.putExtra("color", catalog.Color.name)
        startActivity(intentMainToDetail)
    }
}