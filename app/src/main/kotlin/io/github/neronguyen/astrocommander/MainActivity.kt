package io.github.neronguyen.astrocommander

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import io.github.neronguyen.astrocommander.ui.theme.AstroCommanderTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AstroCommanderTheme {
                val backStack = rememberNavBackStack(Route.Home)
                val decorators = listOf<NavEntryDecorator<NavKey>>(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                )
                val provider = entryProvider<NavKey> {
                    entry<Route.Home> {
                        HomeRoute(
                            onItemClick = { item -> backStack.add(Route.Detail(item)) }
                        )
                    }

                    entry<Route.Detail> { key ->
                        DetailRoute(
                            item = key.item,
                            onBack = { backStack.removeAt(backStack.size - 1) })
                    }
                }

                val entries = rememberDecoratedNavEntries(
                    backStack = backStack,
                    entryDecorators = decorators,
                    entryProvider = provider
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDisplay(
                        entries = entries,
                        onBack = { backStack.removeLastOrNull() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
