package id.ac.pnm.e_commercefitin.DetailProduct

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.database
import id.ac.pnm.e_commercefitin.Cart.AppDatabase
import id.ac.pnm.e_commercefitin.Cart.CartEntity
import id.ac.pnm.e_commercefitin.Catalog.Catalog
import id.ac.pnm.e_commercefitin.MainActivity
import id.ac.pnm.e_commercefitin.R
import id.ac.pnm.e_commercefitin.loginRegis.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_product)
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
        val btnAddCart = findViewById<ImageView>(R.id.addCart)
        val textViewDetailkategori = findViewById<TextView>(R.id.textViewDetailCategory)
        val btnBeli = findViewById<Button>(R.id.buttonBeli)

        val productId = intent.getStringExtra("productId")?:""

        val database = Firebase.database
        val product = database.getReference("product")
        var currentProduct: Catalog? = null
        product.child(productId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()){
                val detailProduct = snapshot.getValue(Catalog::class.java)
                currentProduct = detailProduct
                detailProduct?.let {
                    textViewDetailJudul.text = it.name
                    textViewDeskripsi.text = it.deskripsi
                    textViewDetailHarga.text = "Rp. ${it.price ?: 0}"
                    textViewDetailkategori.text = it.category
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
        btnBeli.setOnClickListener {
            val intentChat = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/6282334500709?text=Halo,%20saya%20beli%20product"))
            startActivity(intentChat)
        }
        btnAddCart.setOnClickListener {
//            val newCart = ItemCart(img, judul, harga, kategori)
//            CartFragment.dataCart.add(newCart)
            currentProduct?.let { product ->

                lifecycleScope.launch(Dispatchers.IO) {

                    val user = UserDatabase
                        .getDatabase(this@DetailProductActivity)
                        .userDao()
                        .getUser()

                    val cartEntity = CartEntity(
                        uid = user?.uid ?: "",
                        productID = productId,
                        name = product.name ?: "",
                        price = product.price ?: 0,
                        category = product.category ?: "",
                        imageUrl = product.imageUrl ?: "",
                        isChecked = false
                    )

                    AppDatabase
                        .getDatabase(this@DetailProductActivity)
                        .cartDao()
                        .insertCart(cartEntity)

                    withContext(Dispatchers.Main) {

                        Toast.makeText(
                            this@DetailProductActivity,
                            "Product ditambahkan ke keranjang",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}