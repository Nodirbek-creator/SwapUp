package com.example.handybook.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.handybook.db.DataManager
import com.example.handybook.navigation.Screen
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue

@Composable
fun MainScreen(
    content: @Composable ()-> Unit,
    navController: NavHostController,
    dataManager: DataManager) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){
                    Box(
                        modifier = Modifier.background(DarkBlue)
                    ){
                        Column(
                            modifier = Modifier.padding(start = 16.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {},
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = SkyBlue,
                                    contentColor = DarkBlue
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Aliyev Ali",
                                fontWeight = FontWeight.W500,
                                fontSize = 20.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "abcd@gmail.com",
                                fontWeight = FontWeight.W200,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopNavigationBar(
                    navController,
                    onMenuClick = {},
                    onProfileClick = {}
                )
            },
            bottomBar = { BottomNavigationBar(navController) },
            contentWindowInsets = WindowInsets.systemBars
        ) { padding->
            Column(modifier = Modifier.padding(padding)) {
                content()
            }
        }
    }
}

@Composable
fun TopNavigationBar(
    navController: NavHostController,
    onMenuClick:() -> Unit,
    onProfileClick:() -> Unit){
    val currentRoute = getCurrentRoute(navController)
    val title = when(currentRoute!!){
        Screen.Home.route -> "Bosh Sahifa"
        Screen.Search.route -> "Qidiruv"
        Screen.Articles.route -> "Maqolalar"
        Screen.Saved.route -> "Saqlanganlar"
        Screen.Settings.route -> "Sozlamalar"
        else -> ""
    }
    Row(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,) {
        IconButton(
            onClick = {onMenuClick()}
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = DarkBlue
            )
        }
        Text(
            text = title,
            fontWeight = FontWeight.W600,
            fontSize = 24.sp,
            color = DarkBlue
        )
        IconButton(
            onClick = {onProfileClick()},
            colors = IconButtonDefaults.filledIconButtonColors(
                contentColor = DarkBlue,
                containerColor = SkyBlue
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController){
    val screens = listOf(Screen.Home, Screen.Search, Screen.Articles, Screen.Saved, Screen.Settings)
    val currentRoute = getCurrentRoute(navController)

    BottomAppBar {
        screens.forEach { screen->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                icon = { Icon(painter = painterResource(screen.icon), contentDescription = null) },
                onClick = {navController.navigate(screen.route)},
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBlue,
                    unselectedIconColor = Color.LightGray
                )
            )
        }
    }
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
