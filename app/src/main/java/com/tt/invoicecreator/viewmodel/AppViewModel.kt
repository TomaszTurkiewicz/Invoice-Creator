package com.tt.invoicecreator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tt.invoicecreator.data.room.Client
import com.tt.invoicecreator.data.room.ClientDao
import com.tt.invoicecreator.data.room.Invoice
import com.tt.invoicecreator.data.room.InvoiceDao
import com.tt.invoicecreator.data.room.Item
import com.tt.invoicecreator.data.room.ItemDao
import com.tt.invoicecreator.data.room.OfflineClientRepository
import com.tt.invoicecreator.data.room.OfflineInvoiceRepository
import com.tt.invoicecreator.data.room.OfflineItemRepository
import com.tt.invoicecreator.data.roomV2.ClientDaoV2
import com.tt.invoicecreator.data.roomV2.InvoiceDaoV2
import com.tt.invoicecreator.data.roomV2.InvoiceItemDaoV2
import com.tt.invoicecreator.data.roomV2.ItemDaoV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(
    itemDao: ItemDao,
    clientDao: ClientDao,
    invoiceDao: InvoiceDao,
    itemDaoV2: ItemDaoV2,
    clientDaoV2: ClientDaoV2,
    invoiceItemDaoV2: InvoiceItemDaoV2,
    invoiceDaoV2: InvoiceDaoV2
) : ViewModel() {
    private val itemRepository:OfflineItemRepository = OfflineItemRepository(itemDao)
    private val clientRepository: OfflineClientRepository = OfflineClientRepository(clientDao)
    private val invoiceRepository: OfflineInvoiceRepository = OfflineInvoiceRepository(invoiceDao)

    val itemList : LiveData<List<Item>> = itemRepository.getAllItems().asLiveData()
    val clientList : LiveData<List<Client>> = clientRepository.getAllClients().asLiveData()
    val invoiceList : LiveData<List<Invoice>> = invoiceRepository.getAllInvoices().asLiveData()

    private val coroutine = CoroutineScope(Dispatchers.Main)
    private var invoice = Invoice()

    var paymentMethod:String = ""

    var calculateNumber = true

    fun getInvoice(): Invoice{
        return this.invoice
    }

    fun saveItem(item: Item){
        coroutine.launch {
            itemRepository.insertItem(item)
        }
    }

    fun saveClient(client: Client){
        coroutine.launch {
            clientRepository.insertClient(client)
        }
    }

    fun saveInvoice(){
        coroutine.launch {
            invoiceRepository.insertInvoice(invoice)
        }
    }

    fun cleanInvoice() {
        this.invoice = Invoice()
        this.calculateNumber = true
    }

}