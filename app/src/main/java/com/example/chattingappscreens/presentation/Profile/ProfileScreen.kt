package com.example.chattingappscreens.presentation.Profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.chattingappscreens.R
import com.example.chattingappscreens.presentation.NavGraph.Auth
import com.example.chattingappscreens.presentation.NavGraph.Home
import com.example.chattingappscreens.presentation.NavGraph.Route
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.example.chattingappscreens.viewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState,
    homeNavHostController: NavHostController,
    modifier: Modifier,
) {

    val themeViewModel: ThemeViewModel = koinViewModel()
    val viewModel: AuthViewModel = koinViewModel()

    val logOutState by viewModel.logoutState.collectAsState()

    val context = LocalContext.current

    //prefernce ka data get
    val name = viewModel.namePreference.collectAsState().value
    val email = viewModel.emailPreference.collectAsState().value
    val profile = viewModel.profilePreference.collectAsState().value
    val phone = viewModel.phonePreference.collectAsState().value
    val isOnline = viewModel.isOnlinePreference.collectAsState().value
    val lastSeen = viewModel.lastSeenPreference.collectAsState().value

    LaunchedEffect(logOutState.isSuccess == true) {
        if (logOutState.isSuccess == true) {
            navHostController.navigate(Auth.Welcome.name) {
                popUpTo(Route.Home.name) {
                    inclusive = true
                }
                popUpTo(Route.Out.name) {
                    inclusive = true
                }
            }
            Toast.makeText(context, "Logout Successfully", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(logOutState.isError != null) {
        if (logOutState.isError != null) {
            snackBarHostState.showSnackbar(
                message = logOutState.isError ?: "logout User failed!! ",
                duration = SnackbarDuration.Long
            )
        }
    }

    if (logOutState.isLoading == true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                trackColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(24.dp)
            )
        }
    }


    ProfileScreenContent(
        name = name,
        email = email,
        phone = phone,
        isOnline = isOnline,
        lastSeen = lastSeen,
        profile = profile,
        viewModel,
        navHostController = homeNavHostController,
        isLoading = logOutState.isLoading ?: false,
        context = context,
        themeViewModel
    )
}


@Composable
fun ProfileScreenContent(
    name: String,
    email: String,
    phone: String,
    isOnline: Boolean,
    lastSeen: String,
    profile: String,
    viewModel: AuthViewModel,
    navHostController: NavHostController,
    isLoading: Boolean,
    context: Context,
    themeViewModel: ThemeViewModel
) {
    val enableThemeDialog = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }



    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
            //  .background(color = MaterialTheme.colorScheme.surface),
            , verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Profile row
            ProfileRow(name = name, phone = phone, profileImage = profile, emailId = email)

            //menuCard
            MenuCards(
                menuItems = AccountRelatedItem,
                onitemClick = { navHostController.navigate(it.route) })
            MenuCards(menuItems = AppSettingItem, onitemClick = { enableThemeDialog.value = true })
            ShowDropDownMenu(
                isEnabled = enableThemeDialog.value ,
                onDismissRequest = {
                    enableThemeDialog.value = false
                },
                onDarkMode = {
                    themeViewModel.setDarkMode()
                    enableThemeDialog.value = false
                },
                onLightMode = {
                    themeViewModel.setLightMode()
                    enableThemeDialog.value = false
                }
            )
            MenuCards(
                menuItems = AppSupportItem,
                onitemClick = { item ->
                    when {
                        item.title == "About" -> {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/7078mohit/oneChat")
                            )
                            context.startActivity(intent)
                        }

                        item.title == "Help" -> {
                            navHostController.navigate(
                                Home.Help.name
                            )
                        }
                    }
                })

            Spacer(modifier = Modifier.weight(1f))
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "logout",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                    )

                    Text(
                        modifier = Modifier.widthIn(100.dp, 200.dp),
                        color = MaterialTheme.colorScheme.error,
                        text = "Logout",
                        fontSize = 14.sp,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))


                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            tint = MaterialTheme.colorScheme.outline,
                            contentDescription = "Go"
                        )
                    }
                    ShowDialogue(
                        show = showDialog,
                        onConfirm = {
                            viewModel.logOut()
                        },
                        onCancel = { showDialog = false },
                        isLoading = isLoading
                    )

                }
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }

}


data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val description: String? = null,
    val route: String,
)

val AccountRelatedItem = listOf(
    ProfileMenuItem(
        icon = Icons.Outlined.Person,
        title = "Edit Profile",
        description = "Name, photo, and about",
        route = Home.EditProfile.name,
    )
)

val AppSupportItem = listOf(
    ProfileMenuItem(
        icon = Icons.Outlined.HelpOutline,
        title = "Help",
        description = "Help center, contact us, privacy policy",
        route = Home.Help.name,
    ),
    ProfileMenuItem(
        icon = Icons.Outlined.Info,
        title = "About",
        description = "Github ",
        route = ""
    )
)


val AppSettingItem = listOf(

    ProfileMenuItem(
        icon = Icons.Outlined.LightMode,
        title = "Theme",
        description = "LightMode & DarkMode",
        route = "",
    ),
)

val ThemeMenuItem = listOf(
    ProfileMenuItem(
        icon = Icons.Outlined.LightMode,
        title = "Light Mode",
        description = "",
        route = "",
    ),
    ProfileMenuItem(
        icon = Icons.Outlined.DarkMode,
        title = "Dark Mode",
        description = "",
        route = "",

        )
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileRow(name: String, profileImage: String, emailId: String, phone: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlideImage(
                model = profileImage ?: null,
                failure = placeholder {
                    Image(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(R.drawable.exclamation),
                        contentScale = ContentScale.Fit,
                        contentDescription = ""
                    )
                },
                loading = placeholder {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            trackColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (profileImage != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    ),
            )

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    modifier = Modifier.widthIn(100.dp, 200.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = emailId,
                    fontSize = 12.sp,
                    // color manage when backend connect
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = if (phone.isNotEmpty()) "Phone: $phone" else "phone: empty!",
                    modifier = Modifier.widthIn(min = 200.dp, max = 350.dp),
                    fontSize = 12.sp,
                    // color manage when backend connect
                    style = MaterialTheme.typography.labelMedium
                )

            }
        }
    }
}


@Composable
fun MenuCards(menuItems: List<ProfileMenuItem>, onitemClick: (ProfileMenuItem) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {

        menuItems.forEachIndexed { index, data ->

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = data.icon,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(28.dp)
                    //  .clip(CircleShape),
                )
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        modifier = Modifier.widthIn(100.dp, 200.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        text = data.title,
                        fontSize = 14.sp,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (data.description != null) {
                        Text(
                            text = data.description,
                            fontSize = 8.sp,
                            // color manage when backend connect
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    onitemClick(data)
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = "Go",
                    )
                }
            }
            if (index != menuItems.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp), thickness = 0.8.dp
                )
            }
        }
    }

}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ShowDialogue(
    show: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    isLoading: Boolean
) {

    if (show) {
        AlertDialog(

            onDismissRequest = onCancel,
            icon = { Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = "logout") },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    if (isLoading) CircularProgressIndicator(
                        trackColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(20.dp)
                    ) else Text("Logout")
                }
            },
            dismissButton = {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(1f)
                ) { Text("Cancel") }
            },
            title = {
                Text(
                    text = "Logout", style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to logout from you account?",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            shape = RoundedCornerShape(12.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            iconContentColor = MaterialTheme.colorScheme.error,

            )
    }
}


@Composable
fun ShowDropDownMenu(
    isEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onLightMode: () -> Unit,
    onDarkMode: () -> Unit,
){
    DropdownMenu(
        expanded = isEnabled,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.wrapContentSize().background(color = Color.Transparent, shape = RoundedCornerShape(24.dp)),
    ){
        MenuCards(
            menuItems = ThemeMenuItem,
            onitemClick = { item ->
                when {
                    item.title == "Dark Mode" -> {
                        onDarkMode.invoke()
                    }

                    item.title == "Light Mode" -> {
                        onLightMode.invoke()
                    }
                }
            }

        )
    }

}
