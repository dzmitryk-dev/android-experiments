package com.github.dzkoirn.hellodreamservice.ui

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.TimeZone

@Composable
fun DreamScreenContent(imageBitmap: ImageBitmap) {
    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
        Image(
            painter = BitmapPainter(image = imageBitmap),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxHeight().fillMaxWidth())
        Column(
            modifier = Modifier.padding(all = 8.dp).align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End
        ) {
            TextClock(
                timeZone = TimeZone.getDefault(),
                textColor = Color.WHITE,
                textSize = 32.sp.value,
                timeFormat24Hour = "HH:mm"
            )
            TextClock(
                timeZone = TimeZone.getDefault(),
                textColor = Color.WHITE,
                textSize = 16.sp.value,
                timeFormat24Hour = "d EEEE"
            )
        }
    }
}

