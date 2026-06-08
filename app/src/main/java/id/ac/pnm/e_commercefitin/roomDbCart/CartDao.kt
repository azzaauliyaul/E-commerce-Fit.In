package id.ac.pnm.e_commercefitin.roomDbCart

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

    @Query("SELECT * FROM cart")
    fun getAllCart(): LiveData<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE productID = :id")
    suspend fun getCartById(id: String): CartEntity?

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}