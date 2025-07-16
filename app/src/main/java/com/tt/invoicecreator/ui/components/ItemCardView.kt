package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ItemCardView(
    viewModel: AppViewModel,
    onClick: ()-> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
        )
        {
            Column {
                Row {
                    Text(
                        text = viewModel.getInvoice().item.itemName,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(5.dp)
                    )
                    Text(
                        text = viewModel.getInvoice().itemCount.toString(),
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .padding(5.dp)
                    )
                    Text(
                        text = "x",
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .padding(5.dp)
                    )
                    Text(
                        text = viewModel.getInvoice().item.itemValue.toString(),
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                }
                Row {
                    Text(
                        text = "discount",
                        fontWeight = FontWeight.W100,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Text(
                        text = ":",
                        fontWeight = FontWeight.W100,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Text(
                        text = viewModel.getInvoice().itemDiscount.toString(),
                        fontWeight = FontWeight.W100,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
                Text(
                    text = viewModel.getInvoice().comment,
                    fontWeight = FontWeight.W100,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
        }
    }
}