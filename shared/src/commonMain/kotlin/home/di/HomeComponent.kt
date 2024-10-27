package home.di

import androidx.compose.runtime.Composable


typealias Home = @Composable () -> Unit

interface HomeComponent {
    abstract val home: Home


}