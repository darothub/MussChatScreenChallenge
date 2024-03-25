package com.darothub.musschatscreen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.darothub.musschatscreen.R

@Composable
fun TailImage() {
    val imagePainter: Painter = painterResource(id = R.drawable.read_icon)
    Image(
        painter = imagePainter,
        contentDescription = "read icon",
        modifier = Modifier.wrapContentSize()
            .size(15.dp),
        colorFilter = ColorFilter.tint(Color.White)
    )
}
