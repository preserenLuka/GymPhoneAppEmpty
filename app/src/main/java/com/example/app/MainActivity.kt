package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.ThemeOption
import com.example.app.settings.SettingsScreen
import com.example.app.home.HomeScreen
import com.example.app.library.LibraryScreen
import com.example.app.stats.StatsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Global app state
            var currentTheme by rememberSaveable { mutableStateOf(ThemeOption.LIGHT) }
            var isLoggedIn by rememberSaveable { mutableStateOf(false) }

            AppTheme(themeOption = currentTheme, dynamicColor = false) {
                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginSuccess = { isLoggedIn = true }
                    )
                } else {
                    MainScaffold(
                        currentTheme = currentTheme,
                        onThemeChange = { newTheme -> currentTheme = newTheme },
                        onLogout = { isLoggedIn = false } // samo nazaj na login
                    )
                }
            }
        }
    }
}

/**
 * Main scaffold with rounded dark bar and center circular "+" FAB.
 */
@Composable
fun MainScaffold(
    currentTheme: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit,
    onLogout: () -> Unit
) {
    var currentDestination by rememberSaveable { mutableStateOf(BottomDestination.HOME) }

    val colorScheme = MaterialTheme.colorScheme

    // Bottom bar barva glede na temo
    val barColor = when (currentTheme) {
        ThemeOption.LIGHT -> Color(0xFF2A2A2A)
        ThemeOption.DARK -> Color(0xFF1A1A1A)
        ThemeOption.NIGHT_BLUE -> Color(0xFF050B16)   // nočna modra
        ThemeOption.JADE_GREEN -> Color(0xFF031813)   // temno jade ozadje
    }

    val fabColor = colorScheme.primary

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

            // Bottom bar height is smaller now, FAB sits inside it.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp) // PERFECT height for all items including FAB
            ) {

                // Actual bar background
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(88.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 18.dp,
                                topEnd = 18.dp
                            )
                        )
                        .background(barColor)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomBarItem(
                            destination = BottomDestination.HOME,
                            selected = currentDestination == BottomDestination.HOME,
                            onClick = { currentDestination = BottomDestination.HOME }
                        )

                        BottomBarItem(
                            destination = BottomDestination.LIBRARY,
                            selected = currentDestination == BottomDestination.LIBRARY,
                            onClick = { currentDestination = BottomDestination.LIBRARY }
                        )

                        // Center FAB slot (same width as others)
                        Box(
                            modifier = Modifier
                                .width(74.dp)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            FloatingActionButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier
                                    .size(48.dp)
                                    .offset(y = (-11).dp),   // <-- PERFECT ALIGNMENT FIX
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Add")
                            }
                        }

                        BottomBarItem(
                            destination = BottomDestination.STATS,
                            selected = currentDestination == BottomDestination.STATS,
                            onClick = { currentDestination = BottomDestination.STATS }
                        )

                        BottomBarItem(
                            destination = BottomDestination.SETTINGS,
                            selected = currentDestination == BottomDestination.SETTINGS,
                            onClick = { currentDestination = BottomDestination.SETTINGS }
                        )
                    }
                }
            }
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (currentDestination) {
                BottomDestination.HOME -> HomeScreen()
                BottomDestination.LIBRARY -> LibraryScreen()
                BottomDestination.STATS -> StatsScreen()
                BottomDestination.SETTINGS -> SettingsScreen(
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange,
                    onLogoutClick = onLogout
                )
            }
        }
    }
}

/**
 * One bottom bar item. Icon + text in a 74dp-wide box.
 */
@Composable
fun BottomBarItem(
    destination: BottomDestination,
    selected: Boolean,
    onClick: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .width(74.dp)
            .height(66.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = destination.label,
                tint = if (selected) primary else Color.White
            )
            Text(
                text = destination.label,
                color = if (selected) primary else Color.White
            )
        }
    }
}

enum class BottomDestination(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Filled.Home),
    LIBRARY("Library", Icons.Filled.List),
    STATS("Stats", Icons.Filled.ShowChart),
    SETTINGS("Settings", Icons.Filled.Settings),
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppTheme(themeOption = ThemeOption.LIGHT, dynamicColor = false) {
        MainScaffold(
            currentTheme = ThemeOption.LIGHT,
            onThemeChange = {},
            onLogout = {}
        )
    }
}
