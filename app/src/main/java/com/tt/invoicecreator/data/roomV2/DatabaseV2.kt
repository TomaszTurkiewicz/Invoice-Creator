package com.tt.invoicecreator.data.roomV2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tt.invoicecreator.R

@Database(
    entities = [
        ItemV2::class,
        ClientV2::class,
        InvoiceItemV2::class,
        InvoiceV2::class
    ],
    version = 3,
    exportSchema = false
)


abstract class DatabaseV2 : RoomDatabase(){
    abstract fun getItemDaoV2() : ItemDaoV2
    abstract fun getClientDaoV2() : ClientDaoV2
    abstract fun getInvoiceItemDaoV2() : InvoiceItemDaoV2
    abstract fun getInvoiceDaoV2(): InvoiceDaoV2

    companion object{
        @Volatile
        private var INSTANCE_V2:DatabaseV2? = null

        fun getDatabaseV2(context: Context):DatabaseV2{
            return INSTANCE_V2 ?: synchronized(this){
                val instanceV2 = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseV2::class.java,
                    context.getString(R.string.databaseV2)
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE_V2 = instanceV2
                instanceV2
            }
        }
    }
}