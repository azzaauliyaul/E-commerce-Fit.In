package id.ac.pnm.e_commercefitin.loginRegis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val alamat: String = "",
    val noTelp: String = "",
    val password: String = "",
    val role: String = ""
)