package id.ac.pnm.e_commercefitin

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import java.util.UUID

class AddFragment : Fragment() {
    private var imageUri: Uri? = null
    private lateinit var imagePreview: ImageView
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            Toast.makeText(requireContext(), "Gambar berhasil dipilih!", Toast.LENGTH_SHORT).show()
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.borderupload)
                .error(R.drawable.borderupload)
                .into(imagePreview)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextName = view.findViewById<TextView>(R.id.editTextName)
        val editTextPrice = view.findViewById<TextView>(R.id.editTextPrice)
        val editTextCategory = view.findViewById<TextView>(R.id.editTextCategory)
        val editTextDescription = view.findViewById<TextView>(R.id.editTextDescription)
        val btnUploadImage = view.findViewById<LinearLayout>(R.id.uploadImageCard)
        imagePreview = view.findViewById(R.id.ivPreview)
        val btnUploadProduct = view.findViewById<Button>(R.id.btnUpload)
        val database = Firebase.database
        val productDb = database.getReference("product")
        val storage = Firebase.storage
        val product_images = storage.getReference("product_images")

        btnUploadImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnUploadProduct.setOnClickListener {
            val productName = editTextName.text.toString().trim()
            val productPriceStr = editTextPrice.text.toString().trim()
            val productCategory = editTextCategory.text.toString().trim()
            val productDescription = editTextDescription.text.toString().trim()
            if (productName.isEmpty() || productPriceStr.isEmpty() ||
                productCategory.isEmpty() || productDescription.isEmpty()) {
                Toast.makeText(requireContext(), "Harap mengisi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
            val productPrice = productPriceStr.toIntOrNull()
            btnUploadProduct.isEnabled = false
            Toast.makeText(requireContext(), "Sedang menambahkan produk...", Toast.LENGTH_SHORT).show()
            val uniqueImageName = "${UUID.randomUUID()}.jpg"
            val fileRef = product_images.child(uniqueImageName)

            fileRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrlString = uri.toString()
                        val productId = productDb.push().key
                        val product = Catalog(productId.toString(), productName, productPrice, productDescription, productCategory, imageUrlString)
                        if (productId != null) {
                            productDb.child(productId).setValue(product)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Produk berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                    editTextName.text = ""
                                    editTextPrice.text = ""
                                    editTextCategory.text = ""
                                    editTextDescription.text = ""
                                    imageUri = null
                                    btnUploadProduct.isEnabled = true
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireContext(), "Gagal menyimpan data ke database: ${e.message}", Toast.LENGTH_LONG).show()
                                    btnUploadProduct.isEnabled = true
                                }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Gagal mengunggah gambar: ${e.message}", Toast.LENGTH_LONG).show()
                    btnUploadProduct.isEnabled = true
                }
        }
    }
}