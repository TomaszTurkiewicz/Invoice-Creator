package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.cards.CustomCardView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogCurrencyPicker(
    onCurrencySelected: (Currency) -> Unit,
    onDismissRequest: () -> Unit
) {

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    )
    {
        CustomCardView(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val currencies = Currency.entries

            currencies.chunked(2).forEach { rowCurrencies ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowCurrencies.forEach { currency ->
                        Box(
                            modifier = Modifier.weight(1f)
                        )
                        {
                            CustomButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 5.dp,
                                        end = 5.dp
                                    ),
                                enabled = true,
                                onClick = {
                                    onCurrencySelected(currency)
                                    onDismissRequest()
                                },
                                text = "${currency.name} - ${currency.symbol}"
                            )
                        }
                    }
                    if(rowCurrencies.size == 1){
                        Box(modifier = Modifier.weight(1f)) {

                        }
                    }
                }
            }
        }
    }
}


// TODO in first place