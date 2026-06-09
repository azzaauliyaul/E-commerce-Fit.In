package id.ac.pnm.e_commercefitin.Update

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.database
import id.ac.pnm.e_commercefitin.Catalog.Catalog
import id.ac.pnm.e_commercefitin.MainActivity
import id.ac.pnm.e_commercefitin.R

class UpdateProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextPrice = findViewById<EditText>(R.id.editTextPrice)
        val editTextCategory = findViewById<EditText>(R.id.editTextCategory)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val btnUpload = findViewById<Button>(R.id.btnUpload)
        val imageViewBack = findViewById<ImageView>(R.id.imageViewBack)
        val imagePreview = findViewById<ImageView>(R.id.ivPreview)

        val productId = intent.getStringExtra("product_id")?:""
        val database = Firebase.database
        val product = database.getReference("product")

        product.child(productId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()){
                val detailProduct = snapshot.getValue(Catalog::class.java)
                detailProduct?.let {
                    editTextName.setText(it.name)
                    editTextDescription.setText(it.deskripsi)
                    editTextPrice.setText(it.price.toString())
                    editTextCategory.setText(it.category)
                    Glide.with(this)
                        .load(it.imageUrl)
                        .placeholder(R.drawable.borderupload)
                        .error(R.drawable.borderupload)
                        .into(imagePreview)
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
        btnUpload.setOnClickListener {
            val updatedPriceStr = editTextPrice.text.toString().trim()
            val updatedCategory = editTextCategory.text.toString().trim()
            val updatedDescription = editTextDescription.text.toString().trim()

            if (updatedPriceStr.isEmpty() ||
                updatedCategory.isEmpty() || updatedDescription.isEmpty()) {
                Toast.makeText(this, "Harap mengisi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updatedPrice = updatedPriceStr.toIntOrNull()
            if (updatedPrice == null) {
                Toast.makeText(this, "Harga harus berupa angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            btnUpload.isEnabled = false
            Toast.makeText(this, "Menyimpan perubahan...", Toast.LENGTH_SHORT).show()

            val updatedFields = mapOf(
                "price" to updatedPrice,
                "category" to updatedCategory,
                "deskripsi" to updatedDescription
            )
            product.child(productId).updateChildren(updatedFields)
                .addOnSuccessListener {
                    Toast.makeText(this, "Produk berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal memperbarui", Toast.LENGTH_LONG).show()
                    btnUpload.isEnabled = true
                }
        }
    }
}