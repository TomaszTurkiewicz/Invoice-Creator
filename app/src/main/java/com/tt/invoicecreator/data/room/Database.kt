package com.tt.invoicecreator.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tt.invoicecreator.R

@Database(
    entities = [
        Item::class,
        Client::class,
        Invoice::class
    ],
    version = 3,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun getItemDao(): ItemDao
    abstract fun getClientDao(): ClientDao
    abstract fun getInvoiceDao(): InvoiceDao

    companion object{
        @Volatile
        private var INSTANCE:com.tt.invoicecreator.data.room.Database? = null

        fun getDatabase(context: Context):com.tt.invoicecreator.data.room.Database{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.tt.invoicecreator.data.room.Database::class.java,
                    context.getString(R.string.database)
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}