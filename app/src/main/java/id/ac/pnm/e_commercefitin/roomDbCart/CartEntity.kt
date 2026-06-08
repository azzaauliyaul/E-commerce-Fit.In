package id.ac.pnm.e_commercefitin.roomDbCart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey
    val productID: String = "",
    val name: String = "",
    val price: Int? = null,
    val deskripsi: String = "",
    val category: String = "",
    val imageUrl: String = "",
    var isChecked: Boolean = false
)
