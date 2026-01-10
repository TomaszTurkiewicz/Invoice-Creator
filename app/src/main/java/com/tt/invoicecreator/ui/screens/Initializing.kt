package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InvoiceAnimatedIcon
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun Initializing(
    viewModel: AppViewModel,
    animation: Boolean,
    ignoredOnComposing: (AppBarState) -> Unit,
    initializing: Boolean,
    onInitializationFinished: () -> Unit
) {

    val context = LocalContext.current

    val currentInitializing by rememberUpdatedState(initializing)

    var showRetryButton by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "INITIALIZING"
            )
        )
    }

//    LaunchedEffect(key1 = true) {
//        delay(7000L)
//
//        if (!currentInitializing) {
//            // Success: Navigate automatically
//            onInitializationFinished()
//        } else {
//            // Failed: Show the "Navigate Anyway" button and stop auto-polling
//            showRetryButton = true
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(animation){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // The Animated Icon
                InvoiceAnimatedIcon(
                    onTwoCyclesFinished = {
                        viewModel.finishAnimation()
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                TitleLargeText(text = "Pocket Invoice")
            }
        }
        else{
            if (currentInitializing) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TitleLargeText(text = "Loading is taking too long")
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomButton(
                        text = "CONTINUE ANYWAY",
                        onClick = { onInitializationFinished() }
                    )
                }

            }else{
                onInitializationFinished()
            }
        }

//        if (showRetryButton) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                TitleLargeText(text = "Loading is taking too long")
//                Spacer(modifier = Modifier.height(16.dp))
//                CustomButton(
//                    text = "CONTINUE ANYWAY",
//                    onClick = { onInitializationFinished() }
//                )
//            }
//
//        }else{
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                // The Animated Icon
//                InvoiceAnimatedIcon(
//                    onTwoCyclesFinished = {
//                        viewModel.finishAnimation()
//                    }
//                )
//                Spacer(modifier = Modifier.height(24.dp))
//                TitleLargeText(text = "Pocket Invoice")
//            }
//        }


//    if(!initializing){
//        onInitializationFinished()
//    }

    }
}
