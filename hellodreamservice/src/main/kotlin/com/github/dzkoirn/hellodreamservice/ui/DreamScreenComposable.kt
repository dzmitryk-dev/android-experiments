package com.github.dzkoirn.hellodreamservice.ui


import android.graphics.Color
import android.widget.TextClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import java.util.TimeZone

@Composable
@Preview
fun DreamScreenContent(imageBitmap: ImageBitmap) {
    Box(modifier = Modifier.fillMaxHeight()) {
        Image(
            painter = BitmapPainter(image = imageBitmap),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxHeight())
        Column(
            modifier = Modifier.padding(all = 8.dp).align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "Hello World")
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

//    Image(painter = , contentDescription = )
}

@Composable
private fun TextClock(
    timeZone: TimeZone = TimeZone.getDefault(),
    textColor: Int = Color.BLACK,
    textSize: Float = 16.sp.value,
    timeFormat24Hour: String,
    timeFormat12Hour: String = timeFormat24Hour
) {
    AndroidView(factory = { context ->
        TextClock(context).apply {
            this.timeZone = timeZone.id
            this.format24Hour = timeFormat24Hour
            this.format12Hour = timeFormat12Hour
            this.setTextColor(textColor)
            this.textSize = textSize
        }
    })
}