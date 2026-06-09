package id.ac.pnm.e_commercefitin.Cart

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(item: CartEntity)

    @Update
    suspend fun updateCart(item: CartEntity)

    @Delete
    suspend fun deleteCart(item: CartEntity)

    @Query("SELECT * FROM cart WHERE uid = :uid")
    fun getAllCart(uid: String): LiveData<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE productID = :productId AND uid = :uid")
    suspend fun getCartById(productId: String, uid: String): CartEntity?

    @Query("DELETE FROM cart WHERE uid = :uid")
    suspend fun clearCart(uid: String)
}