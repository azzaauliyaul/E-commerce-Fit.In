package id.ac.pnm.e_commercefitin
data class ItemCart(
    val Image: Int,
    val Name: String,
    val Price: Int,
    val Category: String,
    var isChecked: Boolean = false
)