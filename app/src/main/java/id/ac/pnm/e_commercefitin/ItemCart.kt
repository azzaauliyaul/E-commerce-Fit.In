package id.ac.pnm.e_commercefitin

data class ItemCart(
    val imageUrl: String,
    val name: String,
    val price: Int,
    val category: String,
    var isChecked: Boolean = false
)