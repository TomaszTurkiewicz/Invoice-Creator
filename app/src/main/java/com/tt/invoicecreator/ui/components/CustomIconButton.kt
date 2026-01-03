package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    containmentColor: Color = MaterialTheme.myColors.primaryDark,
    contentDescription: String? = null
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier.size(40.dp), // Forces the button to be square-ish
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containmentColor,
            contentColor = MaterialTheme.myColors.material.background
        ),
        // Zero padding allows the icon to center perfectly without text constraints
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}