package com.dhaval.bookland

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dhaval.bookland.ui.theme.BooklandTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooklandTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()

        Scaffold(
            topBar = { TopBar() },
            floatingActionButton = { FAB() },
            bottomBar = { BottomBar(navController) },
        ) {
            NavigateScreens(navController = navController)
        }
    }

    @Composable
    fun TopBar() {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colors.onPrimary,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
        )
    }

    @Composable
    fun FAB() {
        val context = LocalContext.current

        FloatingActionButton(onClick = {
            context.startActivity(Intent(context, SearchActivity::class.java))
            overridePendingTransition(R.anim.slide_in, R.anim.zoom_out)
        }, backgroundColor = MaterialTheme.colors.secondary) {
            Icon(Icons.Filled.Add, "", tint = MaterialTheme.colors.onSecondary)
        }
    }

    @Composable
    fun BottomBar(navController: NavController) {
        val items = listOf(BottomNavItem.ToRead, BottomNavItem.Reading, BottomNavItem.Finished)

        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(id = item.icon), contentDescription = null) },
                    label = { Text(item.title) },
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.onSecondary.copy(.4f),
                    alwaysShowLabel = false,
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }

    @Composable
    fun NavigateScreens(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.ToRead.route) {
            composable(BottomNavItem.ToRead.route) {
                ToReadScreen()
            }
            composable(BottomNavItem.Reading.route) {
                ReadingScreen()
            }
            composable(BottomNavItem.Finished.route) {
                FinishedScreen()
            }
        }
    }
}