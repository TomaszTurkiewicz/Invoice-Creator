package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ClientCardViewV2(
    clientName: String,
    clientAddressLine1: String,
    clientAddressLine2: String,
    clientCity: String,
    showCardView: () -> Boolean,
    onClick: () -> Unit
) {
    CustomCardView(
        modifier = Modifier
            .clickable {
                onClick()
            }
    ){
        if(showCardView()){
            Column() {
                TitleLargeText(
                    text = clientName,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp, bottom = 5.dp))
                BodyLargeText(
                    text = clientAddressLine1,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp, bottom = 5.dp))
                BodyLargeText(
                    text = clientAddressLine2,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp, bottom = 5.dp))
                BodyLargeText(
                    text = clientCity,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp, bottom = 10.dp))
            }
        }
        else{
            TitleLargeText(
                text = "Client not chosen yet",
                modifier = Modifier
                    .padding(10.dp))
        }
    }
}
