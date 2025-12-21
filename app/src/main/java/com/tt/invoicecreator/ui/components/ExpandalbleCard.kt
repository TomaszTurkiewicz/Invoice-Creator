package com.tt.invoicecreator.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.TitleMediumText

@Composable
fun ExpandableCard(
    title: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    content: @Composable () -> Unit
)
{
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f, label = "rotation"
    )

    CustomCardView(
        modifier = Modifier
            .padding(8.dp)
    )
    {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // HEADER (Always Visible)
            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
//                    .clickable { onExpandClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    TitleMediumText(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { onExpandClick() },
                    modifier = Modifier.Companion.rotate(rotationState)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp)
                ) {
                    content()
                }
            }

        }
    }
}