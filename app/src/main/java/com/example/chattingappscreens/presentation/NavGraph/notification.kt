package com.example.chattingappscreens.presentation.NavGraph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.chattingappscreens.viewmodel.NotificationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Notification(
    state: NotificationState?,
    onTap: () -> Unit,
    onBack: () -> Unit
) {
    if (state == null) return

    val offset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(3000)
        onBack()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 10.dp, end = 10.dp)
            .offset(y = offset.value.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {},
                    onDragEnd = {
                        if (offset.value < -40f){
                            onBack()
                        }else{
                            coroutineScope.launch {
                                offset.animateTo(0f, animationSpec = spring())
                                }

                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    val newOffset = offset.value + dragAmount.y
                    coroutineScope.launch {
                        offset.snapTo(newOffset.coerceAtMost(0f))
                    }
                }
//                detectTapGestures(
//                    onTap = {
//                        onTap()                // for tap
//                    }
//                )
            },
        contentAlignment = Alignment.TopCenter,

        //   .background(color = Color.Transparent)

    ) {
        Card(
           onClick = onTap,
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        // .weight(2f)
                        .heightIn(min = 20.dp, max = 70.dp)
                ) {
                    Text(
                        text = state.title ?: "New Message",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = state.body ?: "You have New Message",
                        fontSize = 12.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                GlideImage(
                    model = state.image
                        ?: "https://firebasestorage.googleapis.com/v0/b/chatting-app-91488.firebasestorage.app/o/Profile%2FImage_1762517439288_.jpg?alt=media&token=a25a4dff-fae5-47b9-a347-4e8d30854b54",
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

