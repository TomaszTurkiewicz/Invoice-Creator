package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.helpers.User
import com.tt.invoicecreator.ui.components.CustomCardView
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogAddMainUser(
    title:String,
    user:User,
    closeAlertDialog: () -> Unit,
    canBeDismissed: Boolean
) {
    val context = LocalContext.current

    val name = remember {
        mutableStateOf(user.userName?:"")
    }
    val addressLine1 = remember {
        mutableStateOf(user.userAddressLine1?:"")
    }
    val addressLine2 = remember {
        mutableStateOf(user.userAddressLine2?:"")
    }
    val city = remember {
        mutableStateOf(user.userCity?:"")
    }
    BasicAlertDialog(
        onDismissRequest = {
            if(canBeDismissed){
             closeAlertDialog()
            }
        }
    ) {
        CustomCardView {
            Column(
                modifier = Modifier
            )
            {
                TitleLargeText(
                    text = title,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally),
                )

                InputTextWithLabel(
                    labelText = "Name",
                    inputText = name.value
                ) {
                    name.value = it
                }

                InputTextWithLabel(
                    labelText = "Address line 1",
                    inputText = addressLine1.value
                ) {
                    addressLine1.value = it
                }
                InputTextWithLabel(
                    labelText = "Address line 2",
                    inputText = addressLine2.value
                ) {
                    addressLine2.value = it
                }
                InputTextWithLabel(
                    labelText = "city",
                    inputText = city.value
                ) {
                    city.value = it
                }

                Button(
                    enabled = name.value.isNotEmpty() && addressLine1.value.isNotEmpty() && addressLine2.value.isNotEmpty() && city.value.isNotEmpty(),
                    onClick = {
                        SharedPreferences.saveUserDetails(
                            context,
                            User(
                                userName = name.value,
                                userAddressLine1 = addressLine1.value,
                                userAddressLine2 = addressLine2.value,
                                userCity = city.value
                            )
                        )
                        closeAlertDialog()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = "SAVE"
                    )
                }
            }
        }
    }
}
