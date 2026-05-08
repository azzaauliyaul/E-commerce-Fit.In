package id.ac.pnm.e_commercefitin

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat

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
        val textViewDetailColor = findViewById<TextView>(R.id.textViewDetailColor)
        val btnBeli = findViewById<Button>(R.id.buttonBeli)

        val img: Int = intent.getIntExtra("img", 0)
        imageDetailProduct.setImageResource(img)
        val judul: String = intent.getStringExtra("judul").toString()?:""
        textViewDetailJudul.text = "$judul"
        val deskripsi: String = intent.getStringExtra("deskripsi").toString()?:""
        textViewDeskripsi.text = "$deskripsi"
        val harga = intent.getIntExtra("harga", 0)
        textViewDetailHarga.text = "Rp. $harga"
        val kategori: String = intent.getStringExtra("kategori").toString()?:""
        textViewDetailkategori.text = "$kategori"
        val color: String = intent.getStringExtra("color").toString()?:""
        textViewDetailColor.text = "$color"

        imageViewBack.setOnClickListener {
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(intent)
        }
        btnBeli.setOnClickListener {
            val intentChat = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/6282334500709?text=Halo,%20saya%20beli%20product${judul}"))
            startActivity(intentChat)
        }
        btnAddCart.setOnClickListener {
            val newCart = ItemCart(img, judul, harga, kategori)
            CartFragment.dataCart.add(newCart)
            Toast.makeText(this, "Product ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
        }
    }
}