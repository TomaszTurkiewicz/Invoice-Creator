package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2

@Composable
fun InvoiceItemComponent(
    modePro:Boolean,
    itemInvoiceList:List<InvoiceItemV2>,
    onCLick:()->Unit,
    onEditClicked:(index:Int, itemInvoiceV2:InvoiceItemV2)->Unit
)
{
    if (!modePro)
    {
        if (itemInvoiceList.isEmpty()) {
            BodyLargeTextWithCustomButton(
                buttonText = "Add item to invoice",
                onClick = {
                    onCLick()
                }
            )
        } else
        {
            InvoiceItemPosition(
                itemInvoiceV2 = itemInvoiceList[0],
                border = false,
                onEditClicked = {
                    onEditClicked(0,itemInvoiceList[0])
                }
            )
        }
    }
    else
    {
        Column() {
            if(itemInvoiceList.size<15){
                BodyLargeTextWithCustomButton(
                    buttonText = "Add item to invoice",
                    onClick = {
                        onCLick()
                    }
                )
            }
            itemInvoiceList.forEachIndexed { index, itemInvoice ->
                InvoiceItemPosition(
                    itemInvoiceV2 = itemInvoice,
                    border = true,
                    onEditClicked = {
                        onEditClicked(index,itemInvoice)
                    }
                )
            }
        }
    }
}