package id.ac.pnm.e_commercefitin.DetailProduct

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import id.ac.pnm.e_commercefitin.Catalog.Catalog
import id.ac.pnm.e_commercefitin.MainActivity
import id.ac.pnm.e_commercefitin.R
import id.ac.pnm.e_commercefitin.Update.UpdateProductActivity

class DetailProductAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_product_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imageViewBack = findViewById<ImageView>(R.id.imageViewBack)
        val imageDetailProduct = findViewById<ImageView>(R.id.image_Detailproduct)
        val textViewDetailJudul = findViewById<TextView>(R.id.textViewDetailName)
        val textViewDeskripsi = findViewById<TextView>(R.id.textViewDeskripsi)
        val textViewDetailHarga = findViewById<TextView>(R.id.textViewDetailPrice)
        val btnEdit = findViewById<Button>(R.id.buttonEditProduct)
        val textViewDetailkategori = findViewById<TextView>(R.id.textViewDetailCategory)
        val btnDelete = findViewById<ImageView>(R.id.deleteProduct)

        val productId = intent.getStringExtra("productId")?:""
        var currentImageUrl = ""
        val database = Firebase.database
        val product = database.getReference("product")

        product.child(productId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()){
                val detailProduct = snapshot.getValue(Catalog::class.java)
                detailProduct?.let {
                    textViewDetailJudul.text = it.name
                    textViewDeskripsi.text = it.deskripsi
                    textViewDetailHarga.text = "Rp. ${it.price ?: 0}"
                    textViewDetailkategori.text = it.category
                    currentImageUrl = it.imageUrl
                    Glide.with(this)
                        .load(it.imageUrl)
                        .placeholder(R.drawable.borderupload)
                        .error(R.drawable.borderupload)
                        .into(imageDetailProduct)
                }
            }
        }
        imageViewBack.setOnClickListener {
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(intent)
        }
        btnEdit.setOnClickListener {
            val intentToEdit = Intent(this, UpdateProductActivity::class.java)
            intentToEdit.putExtra("product_id", productId)
            startActivity(intentToEdit)
        }
        btnDelete.setOnClickListener {
            val storage = Firebase.storage
            if (currentImageUrl.isNotEmpty()){
                val product_images = storage.getReferenceFromUrl(currentImageUrl)
                product_images.delete().addOnSuccessListener {
                    product.child(productId).removeValue().addOnSuccessListener {
                        Toast.makeText(this, "Produk berhasil dihapus!", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Gagal menghapus gambar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}