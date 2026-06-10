    package id.ac.pnm.e_commercefitin.Cart

    import android.content.Context
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase

    @Database(entities = [CartEntity::class], version = 2)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun cartDao(): CartDao

        companion object{
            @Volatile
            var INSTANCE: AppDatabase?= null
            fun getDatabase(applicationContext: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        applicationContext, AppDatabase::class.java, "cart"
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }