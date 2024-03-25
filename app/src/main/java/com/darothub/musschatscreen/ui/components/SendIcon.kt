package com.darothub.musschatscreen.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun SendIcon(modifier: Modifier, onClick: () -> Unit){
    Icon(
        Icons.Filled.Send,
        contentDescription = "Send Icon",
        modifier = modifier
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(percent = 100)
            )
            .padding(8.dp),
        tint = Color.White
    )
}