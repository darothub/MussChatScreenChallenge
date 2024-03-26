package com.darothub.musschatscreen.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Salam",
) = CustomTextField(
    text = value,
    onValueChange = onValueChange,
    modifier = modifier,
    placeholderText = placeholder
)