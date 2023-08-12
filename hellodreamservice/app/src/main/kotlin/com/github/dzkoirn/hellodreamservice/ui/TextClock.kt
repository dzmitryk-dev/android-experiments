package com.github.dzkoirn.hellodreamservice.ui

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import java.util.TimeZone


@Composable
internal fun TextClock(
    timeZone: TimeZone = TimeZone.getDefault(),
    textColor: Int = Color.BLACK,
    textSize: Float = 16.sp.value,
    timeFormat24Hour: String,
    timeFormat12Hour: String = timeFormat24Hour
) {
    AndroidView(factory = { context ->
        android.widget.TextClock(context).apply {
            this.timeZone = timeZone.id
            this.format24Hour = timeFormat24Hour
            this.format12Hour = timeFormat12Hour
            this.setTextColor(textColor)
            this.textSize = textSize
        }
    })
}