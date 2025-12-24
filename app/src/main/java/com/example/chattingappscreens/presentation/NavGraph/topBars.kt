package com.example.chattingappscreens.presentation.NavGraph

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.chattingappscreens.R
import com.example.chattingappscreens.core.utils.exactTime
import com.example.chattingappscreens.data.modell.UserModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

@Composable
fun ChattingTopBar(
    profile: String,
    name: String,
    friendModel: UserModel,
    navHostController: NavHostController,
) {
    TopAppBar(
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable(
                            onClick = { navHostController.popBackStack() }),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    GlideImage(
                        model = profile,
                        contentDescription = "image",
                        loading = placeholder {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    trackColor = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(
                                width = 0.4.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop,

                        )
                }
                Spacer(modifier = Modifier.width(7.dp))
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.widthIn(100.dp, 200.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        text = name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (friendModel.isOnline) "online" else exactTime(
                            friendModel.lastSeen
                        ),
                        color = if (friendModel.isOnline) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onBackground,
                        fontSize = if (friendModel.isOnline) 13.sp else 10.sp,
                        fontWeight = if (friendModel.isOnline) FontWeight.Bold else FontWeight.SemiBold,

                        )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(navHostController: NavHostController, title: String, isBack: Boolean = true) {
    TopAppBar(
        navigationIcon = {
            if (isBack) {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    navHostController: NavHostController
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary
//            brush = Brush.horizontalGradient(
//                listOf(
//                    MaterialTheme.colorScheme.secondary,
//                    MaterialTheme.colorScheme.tertiary)
//            )
            ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = "One Chat", color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                //fontFamily = FontFamily.Default
            )
        },

        actions = {
            Box(
                modifier = Modifier
                    .size(height = 36.dp, width = 160.dp)
                    .padding(horizontal = 16.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .clickable(onClick = { navHostController.navigate(Out.Search.name) }),

                contentAlignment = Alignment.CenterStart

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                ) {
                    Text(
                        text = "Search...",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search",
                    )
                }
            }
        }
    )
}