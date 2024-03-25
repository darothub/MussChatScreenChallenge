package com.darothub.musschatscreen.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.darothub.musschatscreen.R
import com.darothub.musschatscreen.presentation.ui.theme.MussChatScreenTheme

@Composable
fun TopAppBarCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.12f)
            .shadow(10.dp)
            .background(Color.White)

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back Arrow",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 5.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .padding(end = 5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.more_horiz),
                contentDescription = "More",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(30.dp)
                    .padding(end = 6.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview(){
    MussChatScreenTheme {
        TopAppBarCard()
    }
}