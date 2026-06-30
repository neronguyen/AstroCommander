package io.github.neronguyen.astrocommander

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.neronguyen.astrocommander.ui.theme.AstroCommanderTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AstroCommanderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingRoute(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingRoute(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    GreetingScreen(
        name = name,
        modifier = modifier
    )
}

@Composable
fun GreetingScreen(
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "Hello $name!")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AstroCommanderTheme {
        GreetingScreen(name = "Android")
    }
}
