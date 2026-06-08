package id.ac.pnm.e_commercefitin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.pnm.e_commercefitin.roomDbCart.AppDatabase
import id.ac.pnm.e_commercefitin.roomDbCart.CartEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(val data: MutableList<CartEntity>, val onCheckedChange: (Int) -> Unit): RecyclerView.Adapter<CartAdapter.CartViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val layout: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(layout)
    }

    var total = 0
    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        val dataCart = data[position]
        holder.textViewName.text = dataCart.name
        holder.textViewPrice.text = "Rp. ${dataCart.price}"
        holder.textViewCategory.text = dataCart.category
        holder.checkbox.isChecked = dataCart.isChecked

        Glide.with(holder.itemView.context)
            .load(dataCart.imageUrl)
            .placeholder(R.drawable.borderupload)
            .error(R.drawable.borderupload)
            .into(holder.image)

        // Listener
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            dataCart.isChecked = isChecked

            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase
                    .getDatabase(holder.itemView.context)
                    .cartDao()
                    .updateCart(dataCart)
            }

            val total = data.filter { it.isChecked }.sumOf { it.price ?: 0 }
            onCheckedChange(total)
        }

        holder.btnDelete.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                AppDatabase
                    .getDatabase(holder.itemView.context)
                    .cartDao()
                    .deleteCart(dataCart)

            }

            Toast.makeText(
                holder.itemView.context,
                "Product dihapus dari keranjang",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<CartEntity>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class CartViewHolder(val row: View) : RecyclerView.ViewHolder(row){
        val image = row.findViewById<ImageView>(R.id.imageViewProduct)
        val textViewPrice = row.findViewById<TextView>(R.id.textViewPrice)
        val textViewName = row.findViewById<TextView>(R.id.textViewName)
        val textViewCategory = row.findViewById<TextView>(R.id.textViewCategory)
        val btnDelete = row.findViewById<ImageView>(R.id.Delete)
        val checkbox = row.findViewById<CheckBox>(R.id.checkBox)
    }
}