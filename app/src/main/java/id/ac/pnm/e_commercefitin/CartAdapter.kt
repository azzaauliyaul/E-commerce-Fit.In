package id.ac.pnm.e_commercefitin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(val data: MutableList<ItemCart>, val onCheckedChange: (Int) -> Unit): RecyclerView.Adapter<CartAdapter.CartViewHolder>(){
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
        holder.image.setImageResource(dataCart.Image)
        holder.textViewName.text = dataCart.Name
        holder.textViewPrice.text = "Rp. ${dataCart.Price}"
        holder.textViewCategory.text = dataCart.Category
        holder.checkbox.isChecked = dataCart.isChecked

        // Listener
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            dataCart.isChecked = isChecked

            val total = data.filter { it.isChecked }.sumOf { it.Price }
            onCheckedChange(total)
        }

        holder.btnDelete.setOnClickListener {
            val cart = holder.bindingAdapterPosition
            data.remove(dataCart)
            notifyItemRemoved(cart)
            notifyItemRangeChanged(cart, data.size)
            Toast.makeText(holder.itemView.context, "Product dihapus dari keranjang", Toast.LENGTH_SHORT).show()
            val total = data.filter { it.isChecked }.sumOf { it.Price }
            onCheckedChange(total)
        }
    }

    override fun getItemCount(): Int {
        return data.size
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