package com.tt.invoicecreator.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Item::class,
        Client::class,
        Invoice::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun getItemDao(): ItemDao

}