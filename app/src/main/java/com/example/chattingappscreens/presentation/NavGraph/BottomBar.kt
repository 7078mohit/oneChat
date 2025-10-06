package com.example.chattingappscreens.presentation.NavGraph

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.chattingappscreens.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(modifier : Modifier , navHostController: NavHostController ) {

    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    val bottomItemList = listOf(
        BottomNavItem.HomeBar,
        BottomNavItem.ProfileBar
    )


    NavigationBar(modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface) {
        bottomItemList.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = {
                    navHostController.navigate(route = item.route){
                        popUpTo(id = navHostController.graph.startDestinationId){
                            saveState =true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(item.label) },
                enabled = true,
                alwaysShowLabel = true,
                    //currentDestination == item.route,
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = MaterialTheme.colorScheme.primary
//                )

                icon ={
                   Icon(painter = painterResource( if (currentDestination == item.route) item.selectedImage else item.unselectedImage) , contentDescription = "Icon",
                      modifier = Modifier.size(24.dp))
                }
            )
        }
    }
}


sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedImage: Int,
    val unselectedImage: Int
) {
    object  HomeBar: BottomNavItem(route = Home.Contact.name , label = "Home" , selectedImage = R.drawable.homefilled, unselectedImage = R.drawable.home)
    object  ProfileBar : BottomNavItem(route = Home.Profile.name , label = "Profile" , selectedImage = R.drawable.profilefilled , unselectedImage = R.drawable.profile)
}