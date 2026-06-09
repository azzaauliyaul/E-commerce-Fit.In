package id.ac.pnm.e_commercefitin.Catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.pnm.e_commercefitin.R

class CatalogAdapter(var data: List<Catalog>, val onClickOpenDetailProductActivity: (Catalog)-> Unit):
    RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {
    var filterData: MutableList<Catalog> = data.toMutableList()
    var selectedCategory: String? = null
    var searchQuery: String = ""

    fun updateData(newData: List<Catalog>) {
        this.data = newData
        filter()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogViewHolder {
        val layout: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_catalog, parent, false)
        return CatalogViewHolder(layout)
    }
    fun filterCategory(category: String?) {
        selectedCategory = category
        filter()
    }

    fun filterSearch(query: String) {
        searchQuery = query
        filter()
    }

    fun filter() {
        filterData = data.filter { item ->
            val matchCategory =
                selectedCategory?.let { item.category == it } ?: true
            val matchSearch =
                item.name.contains(searchQuery, ignoreCase = true)
            matchCategory&&matchSearch
        }.toMutableList()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(
        holder: CatalogViewHolder,
        position: Int
    ) {
        val catalog: Catalog = filterData[position]
        holder.textViewPrice.text = "Rp. ${catalog.price ?: 0}"
        holder.textViewName.text = catalog.name
        holder.textViewDeskripsi.text = catalog.deskripsi
        holder.textViewCategory.text = catalog.category

        Glide.with(holder.itemView.context)
            .load(catalog.imageUrl)
            .placeholder(R.drawable.borderupload)
            .error(R.drawable.borderupload)
            .into(holder.image)
        holder.row.setOnClickListener { onClickOpenDetailProductActivity(catalog) }
    }
    override fun getItemCount(): Int = filterData.size

    class CatalogViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val searchView = row.findViewById<SearchView>(R.id.searchView)
        val image = row.findViewById<ImageView>(R.id.image_product)
        val textViewPrice = row.findViewById<TextView>(R.id.textViewPrice)
        val textViewName = row.findViewById<TextView>(R.id.textViewName)
        val textViewDeskripsi = row.findViewById<TextView>(R.id.textViewDeskripsi)
        val textViewCategory = row.findViewById<TextView>(R.id.textViewCategory)

    }
}