package com.tt.invoicecreator.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tt.invoicecreator.data.AppUiState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.roomV2.ClientDaoV2
import com.tt.invoicecreator.data.roomV2.ClientV2
import com.tt.invoicecreator.data.roomV2.InvoiceDaoV2
import com.tt.invoicecreator.data.roomV2.InvoiceItemDaoV2
import com.tt.invoicecreator.data.roomV2.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.InvoiceV2
import com.tt.invoicecreator.data.roomV2.ItemDaoV2
import com.tt.invoicecreator.data.roomV2.ItemV2
import com.tt.invoicecreator.data.roomV2.OfflineClientRepositoryV2
import com.tt.invoicecreator.data.roomV2.OfflineInvoiceItemRepositoryV2
import com.tt.invoicecreator.data.roomV2.OfflineInvoiceRepositoryV2
import com.tt.invoicecreator.data.roomV2.OfflineItemRepositoryV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    itemDaoV2: ItemDaoV2,
    clientDaoV2: ClientDaoV2,
    invoiceItemDaoV2: InvoiceItemDaoV2,
    invoiceDaoV2: InvoiceDaoV2
) : ViewModel() {

    private val itemRepositoryV2:OfflineItemRepositoryV2 = OfflineItemRepositoryV2(itemDaoV2)
    private val clientRepositoryV2:OfflineClientRepositoryV2 = OfflineClientRepositoryV2(clientDaoV2)
    private val invoiceItemRepositoryV2: OfflineInvoiceItemRepositoryV2 = OfflineInvoiceItemRepositoryV2(invoiceItemDaoV2)
    private val invoiceRepositoryV2:OfflineInvoiceRepositoryV2 = OfflineInvoiceRepositoryV2(invoiceDaoV2)

    val itemListV2: LiveData<List<ItemV2>> = itemRepositoryV2.getAllItems().asLiveData()
    val clientListV2: LiveData<List<ClientV2>> = clientRepositoryV2.getAllClients().asLiveData()
    val invoiceItemListV2: LiveData<List<InvoiceItemV2>> = invoiceItemRepositoryV2.getAllInvoiceItems().asLiveData()
    val invoiceListV2: LiveData<List<InvoiceV2>> = invoiceRepositoryV2.getAllInvoices().asLiveData()

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private var invoiceV2 = InvoiceV2()

    private val coroutine = CoroutineScope(Dispatchers.Main)

    private val invoiceItems: ArrayList<InvoiceItemV2> = ArrayList()

    var paymentMethod:String = ""

    var calculateNumber = true


    fun addItemToInvoice(invoiceItemV2: InvoiceItemV2){
        invoiceItems.add(invoiceItemV2)
    }

    fun getInvoiceItemList():List<InvoiceItemV2>{
        return this.invoiceItems
    }

    fun getInvoiceV2(): InvoiceV2{
        return this.invoiceV2
    }

    fun saveItemV2(item: ItemV2){
        coroutine.launch {
            itemRepositoryV2.insertItem(item)
        }
    }

    fun saveClientV2(clientV2: ClientV2){
        coroutine.launch {
            clientRepositoryV2.insertClient(clientV2)
        }
    }

    fun saveInvoiceV2(){
        coroutine.launch {
            val id = invoiceRepositoryV2.insertInvoice(invoiceV2)

            invoiceItems.forEach{
                item ->
                item.invoiceId = id.toInt()
                invoiceItemRepositoryV2.insertInvoiceItem(item)
            }
        }
    }

    fun setModePro(context: Context, boolean: Boolean){
        SharedPreferences.savePROMode(context,boolean)
        _uiState.update { currentState ->
            currentState.copy(
                modePro = boolean
            )
        }
    }

    fun rewardedApLoaded(){
        _uiState.update { currentState ->
            currentState.copy(
                rewardedAppLoaded = true
            )
        }
    }

    fun rewardedAdWatched(){
        _uiState.update { currentState ->
            currentState.copy(
                rewardedAdWatched = true
            )
        }
    }

    fun cleanInvoiceV2() {
        this.invoiceV2 = InvoiceV2()
        this.invoiceItems.clear()
        this.calculateNumber = true
    }

    fun cleanAdFlags() {
        _uiState.update { currentState ->
            currentState.copy(
                rewardedAppLoaded = false,
                rewardedAdWatched = false
            )
        }
    }


}