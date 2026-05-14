package id.ac.pnm.e_commercefitin
data class Catalog(
    val Image: Int,
    val Name: String,
    val Price: Int,
    val Deskripsi: String,
    val Category: Category
)
enum class Category{
    Jaket, Knit, Dasi, Kemeja, Celana, Cardigan
}