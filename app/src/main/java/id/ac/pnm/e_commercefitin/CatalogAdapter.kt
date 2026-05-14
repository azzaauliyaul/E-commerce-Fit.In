package id.ac.pnm.e_commercefitin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CatalogAdapter(val data: List<Catalog>, val onClickOpenDetailProductActivity: (Catalog)-> Unit):
    RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {
    var filterData: MutableList<Catalog> = data.toMutableList()
    var selectedCategory: Category? = null
    var searchQuery: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogViewHolder {
        val layout: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_catalog, parent, false)
        return CatalogViewHolder(layout)
    }
    fun filterCategory(category: Category?) {
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
                selectedCategory?.let { item.Category == it } ?: true
            val matchSearch =
                item.Name.contains(searchQuery, ignoreCase = true)
            matchCategory&&matchSearch
        }.toMutableList()

        notifyDataSetChanged()
    }


    override fun onBindViewHolder(
        holder: CatalogViewHolder,
        position: Int
    ) {
        val catalog: Catalog = filterData[position]
        holder.image.setImageResource(catalog.Image)
        holder.textViewPrice.text = "Rp. ${catalog.Price}"
        holder.textViewName.text = catalog.Name
        holder.textViewDeskripsi.text = catalog.Deskripsi
        holder.textViewCategory.text = catalog.Category.toString()
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