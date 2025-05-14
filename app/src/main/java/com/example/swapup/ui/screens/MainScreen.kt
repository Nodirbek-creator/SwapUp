package com.example.swapup.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.swapup.R
import com.example.swapup.data.model.User
import com.example.swapup.navigation.Routes
import com.example.swapup.navigation.Screen
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.ui.theme.SkyBlue
import com.example.swapup.viewmodel.BookViewModel
import com.example.swapup.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    content: @Composable ()-> Unit,
    navController: NavHostController,
    vm: MainViewModel,
) {
    val drawerState = DrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedIndex = vm.selectedIndex
    val currentUser = vm.currentUser
    val currentRoute = getCurrentRoute(navController) ?: ""
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerSheet(
                selectedIndex = selectedIndex,
                onClick = { route, index ->
                    vm.onIndexChange(index)
                    when(route){
                        "Telegram" -> {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(context.getString(R.string.TG_channel))
                                setPackage("org.telegram.messenger") // Forces opening in Telegram app
                            }

                            // Fallback in case Telegram app is not installed
                            try {
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                // Open in browser if Telegram is not installed
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/freestyle_path"))
                                context.startActivity(browserIntent)
                            }
                        }
                        "Share" ->   {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Check out our app!")
                            }

                            val chooser = Intent.createChooser(intent, "Share via")
                            context.startActivity(chooser)
                        }
                        Routes.Login.name -> {
                            navController.navigate(route)
                            vm.logout()
                        }
                        else -> {
                            navController.navigate(route)
                        }
                    }
                },
                onProfileClick = {navController.navigate(Routes.Profile.name)},
                user = currentUser
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.9f),
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Scaffold(
            topBar = {
                TopNavigationBar(
                    navController,
                    vm = vm,
                    onMenuClick = { scope.launch { drawerState.open()}},
                    onProfileClick = { navController.navigate(Routes.Profile.name) }
                )
            },
            bottomBar = { BottomNavigationBar(navController) },
            floatingActionButton = {
                if(currentRoute == Routes.Demand.name){
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Routes.CreateDemand.name)
                        },
                        containerColor = SkyBlue,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add",
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
                if(currentRoute == Routes.Offer.name){
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Routes.CreateOffer.name)
                        },
                        containerColor = SkyBlue,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add",
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            },
        ) { padding->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                content()
            }
        }
    }
}

@Composable
fun NavigationDrawerSheet(
    selectedIndex:Int,
    user: User?,
    onClick: (String, Int) -> Unit,
    onProfileClick: () -> Unit,
){
    val navigationItems = listOf(
        DrawerItem(
            title = stringResource(R.string.main_page),
            route = Routes.Home.name,
            icon = R.drawable.book
        ),
        DrawerItem(
            title = stringResource(R.string.search),
            route = Routes.Search.name,
            icon = R.drawable.search
        ),
        DrawerItem(
            title = stringResource(R.string.offer),
            route = Routes.Offer.name,
            icon = R.drawable.offer
        ),
        DrawerItem(
            title = stringResource(R.string.demand),
            route = Routes.Demand.name,
            icon = R.drawable.demand
        ),
        DrawerItem(
            title = stringResource(R.string.settings),
            route = Routes.Settings.name,
            icon = R.drawable.setting
        ),
        //divider
        DrawerItem(
            title = stringResource(R.string.telegram),
            route = "Telegram",
            icon = R.drawable.telegram
        ),
        DrawerItem(
            title = stringResource(R.string.share),
            route = "Share",
            icon = R.drawable.share
        ),
        //divider
        DrawerItem(
            title = stringResource(R.string.logout),
            route = Routes.Login.name,
            icon = R.drawable.exit
        )
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerShape = RectangleShape,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Column(
                modifier = Modifier.fillMaxWidth().background(DarkBlue).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(24.dp))
                IconButton(
                    onClick = {onProfileClick()},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFB1DAEF),
                        contentColor = DarkBlue
                    ),
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                    )
                }
                user?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = user.fullname,
                        fontWeight = FontWeight.W500,
                        fontSize = 20.sp,
                        color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = user.username,
                        fontWeight = FontWeight.W200,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.LightGray)
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)
            ) {
                navigationItems.forEachIndexed { index, drawerItem ->
                    NavigationDrawerItem(
                        label = { Text(drawerItem.title, fontWeight = FontWeight.W400, fontSize = 12.sp) },
                        icon = { Icon(painter = painterResource(drawerItem.icon), null, modifier = Modifier.size(24.dp)) },
                        onClick = {onClick(drawerItem.route, index)},
                        selected = index == selectedIndex,
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            selectedContainerColor = DarkBlue,
                            unselectedIconColor = DarkBlue,
                            unselectedTextColor = DarkBlue,
                            unselectedContainerColor = Color.White,
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    if(index == 4 || index == 6){
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }




        }
    }

}

data class DrawerItem(
    val title: String,
    val route: String,
    val icon: Int,
)

@Composable
fun TopNavigationBar(
    navController: NavHostController,
    vm: MainViewModel,
    onMenuClick:() -> Unit,
    onProfileClick:() -> Unit){
    val currentRoute = getCurrentRoute(navController) ?: ""
    val title = when(currentRoute){
        Screen.Home.route -> stringResource(R.string.main_page)
        Screen.Search.route -> stringResource(R.string.search)
        Screen.Offer.route -> stringResource(R.string.offer)
        Screen.Demand.route -> stringResource(R.string.demand)
        Screen.Settings.route -> stringResource(R.string.settings)
        Screen.Category.route -> vm.selectedCategory.typename ?: ""
        else -> ""
    }
    Row(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,) {
        if(currentRoute != Screen.Category.route){
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
        }
        else{
            IconButton(
                onClick = {navController.popBackStack()}
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = DarkBlue
                )
            }
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
    val screens = listOf(Screen.Home, Screen.Search, Screen.Offer, Screen.Demand, Screen.Settings)
    val currentRoute = getCurrentRoute(navController)

    BottomAppBar(
        containerColor = Color.White
    ) {
        screens.forEach { screen->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)) },
                onClick = { navController.navigate(screen.route)},
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBlue,
                    unselectedIconColor = Color.LightGray,
                    indicatorColor = Color.Transparent
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
