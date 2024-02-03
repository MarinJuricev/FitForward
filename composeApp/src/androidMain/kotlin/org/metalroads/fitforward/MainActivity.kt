package org.metalroads.fitforward

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import design.FitForwardTheme
import home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FitForwardTheme {
                HomeScreen()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}