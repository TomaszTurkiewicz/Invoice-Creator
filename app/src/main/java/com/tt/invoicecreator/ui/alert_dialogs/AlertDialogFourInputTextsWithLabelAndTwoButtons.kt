package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogFourInputTextsWithLabelAndTwoButtons(
    title:String,
    labelOne: String,
    inputOne:String = "",
    labelTwo: String,
    inputTwo:String = "",
    labelThree: String,
    inputThree:String = "",
    labelFour: String,
    inputFour:String = "",
    buttonTextOne: String,
    buttonTextTwo: String = "",
    secondButtonEnabled: Boolean,
    onDismissRequest: () -> Unit,
    firstButtonAction: (firstLine:String, secondLine:String, thirdLine:String, fourthLine:String) -> Unit,
    secondButtonAction: () -> Unit
)
{
    val inputOneMut = remember {
        mutableStateOf(inputOne)
    }

    val inputTwoMut = remember {
        mutableStateOf(inputTwo)
    }

    val inputThreeMut = remember {
        mutableStateOf(inputThree)
    }

    val inputFourMut = remember {
        mutableStateOf(inputFour)
    }

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    )
    {
        CustomCardView(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            TitleLargeText(
                text = title,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            InputTextWithLabel(
                labelText = labelOne,
                inputText = inputOneMut.value
            )
            {
                inputOneMut.value = it
            }

            InputTextWithLabel(
                labelText = labelTwo,
                inputText = inputTwoMut.value
            )
            {
                inputTwoMut.value = it
            }

            InputTextWithLabel(
                labelText = labelThree,
                inputText = inputThreeMut.value
            )
            {
                inputThreeMut.value = it
            }

            InputTextWithLabel(
                labelText = labelFour,
                inputText = inputFourMut.value
            )
            {
                inputFourMut.value = it
            }

            CustomButton(
                enabled = inputOneMut.value.trim().isNotEmpty()
                        && inputTwoMut.value.trim().isNotEmpty()
                        && inputThreeMut.value.trim().isNotEmpty()
                        && inputFourMut.value.trim().isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                text = buttonTextOne,
                onClick = {
                    firstButtonAction(
                        inputOneMut.value.trim(),
                        inputTwoMut.value.trim(),
                        inputThreeMut.value.trim(),
                        inputFourMut.value.trim()
                    )
                    onDismissRequest()
                }
            )

            if(secondButtonEnabled){
                CustomButton(
                    enabled = inputOneMut.value.trim().isNotEmpty()
                            && inputTwoMut.value.trim().isNotEmpty()
                            && inputThreeMut.value.trim().isNotEmpty()
                            && inputFourMut.value.trim().isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 5.dp,
                            start = 5.dp,
                            end = 5.dp,
                            top = 30.dp),
                    text = buttonTextTwo,
                    makeItWarning = true,
                    onClick = {
                        secondButtonAction()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}