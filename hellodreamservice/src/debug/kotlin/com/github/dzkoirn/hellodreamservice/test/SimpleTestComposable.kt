package com.github.dzkoirn.hellodreamservice.test

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SimpleTestComposabe() {
    Text(text = "Hello World", color = Color.Green)
}