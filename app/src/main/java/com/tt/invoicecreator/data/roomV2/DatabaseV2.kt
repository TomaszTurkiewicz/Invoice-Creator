package com.tt.invoicecreator.data.roomV2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.roomV2.dao.ClientDaoV2
import com.tt.invoicecreator.data.roomV2.dao.InvoiceDaoV2
import com.tt.invoicecreator.data.roomV2.dao.InvoiceItemDaoV2
import com.tt.invoicecreator.data.roomV2.dao.ItemDaoV2
import com.tt.invoicecreator.data.roomV2.dao.PaidDaoV2
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2

@Database(
    entities = [
        ItemV2::class,
        ClientV2::class,
        InvoiceItemV2::class,
        InvoiceV2::class,
        PaidV2::class
    ],
    version = 12,
    exportSchema = false
)


abstract class DatabaseV2 : RoomDatabase(){
    abstract fun getItemDaoV2() : ItemDaoV2
    abstract fun getClientDaoV2() : ClientDaoV2
    abstract fun getInvoiceItemDaoV2() : InvoiceItemDaoV2
    abstract fun getInvoiceDaoV2(): InvoiceDaoV2

    abstract fun getPaidDaoV2(): PaidDaoV2
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