package com.darothub.musschatscreen.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField(
    modifier: Modifier,
    text: String,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.bodySmall.fontSize,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier
            .border(
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(10.dp),
        value = text,
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = Color.Black.copy(alpha = 0.3f),
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }
            }
        }
    )
}