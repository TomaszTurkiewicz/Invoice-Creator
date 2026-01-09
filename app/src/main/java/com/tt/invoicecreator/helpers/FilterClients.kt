package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2

object FilterClients {
    fun getInUse(
        clientList: List<ClientV2>?
    ): List<ClientV2>? {
        return clientList?.filter {
            it.clientInUse
        }
    }

    fun withInvoices(
        clientList: List<ClientV2>?,
        invoiceList: List<InvoiceV2>?
    ): List<ClientV2>? {
        return clientList?.filter {
            val clientInvoices = invoiceList?.filter { invoice ->
                invoice.client.clientId == it.clientId
            }
            clientInvoices?.isEmpty() == false
        }
    }

    fun filterClients(
        clientsInUse: List<ClientV2>?,
        value: String
    ): List<ClientV2>? {
        return if (value.isEmpty()) {
            clientsInUse
        } else {
            clientsInUse?.filter {
                it.clientName.lowercase().contains(value.lowercase()) ||
                        it.clientAddress1.lowercase().contains(value.lowercase()) ||
                        it.clientAddress2.lowercase().contains(value.lowercase()) ||
                        it.clientCity.lowercase().contains(value.lowercase())
            }
        }
    }
}
