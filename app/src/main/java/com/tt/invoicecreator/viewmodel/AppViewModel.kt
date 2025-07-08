package com.tt.invoicecreator.viewmodel

import androidx.lifecycle.ViewModel
import com.tt.invoicecreator.data.room.ItemDao

class AppViewModel(
    private val itemDao: ItemDao
) : ViewModel() {
}