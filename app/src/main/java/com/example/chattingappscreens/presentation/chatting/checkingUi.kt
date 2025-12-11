package com.example.chattingappscreens.presentation.chatting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.chattingappscreens.R
import com.example.chattingappscreens.core.utils.dateFormatter
import com.google.common.math.LinearTransformation.vertical

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true , showSystemUi = true)
@Composable
fun CheckingUi() {


}


@Composable
fun UserChatShimmer(){
    val listOfSides = listOf(
        false,
        false,
        false,
        false,
        true,
        true,
        false,
        true,
        true,
        false,
        true
    )
    val shimmerColors = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0)
    )

    val brush = Brush.linearGradient(shimmerColors, start = Offset.Zero , end = Offset.Infinite)


    Column(modifier = Modifier.fillMaxSize()) {
        listOfSides.forEach {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = if (it) Arrangement.Start else Arrangement.End
            ) {
                Spacer(
                    modifier = Modifier.width(120.dp).height(40.dp)
                        .background(brush = brush, shape = RoundedCornerShape(18.dp))
                )

            }
        }
    }

}


@Composable
fun HomeUsersShimmer() {
    val shimmerColors = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0)
    )

    val brush = Brush.linearGradient(shimmerColors, start = Offset.Zero , end = Offset.Infinite)

    Row(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 16.dp , vertical = 8.dp),
        verticalAlignment = CenterVertically
    ){
        Spacer(modifier = Modifier.size(50.dp).background(brush = brush, shape = CircleShape))
        Spacer(modifier = Modifier.height(18.dp))
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start) {
            Spacer(
                modifier = Modifier.fillMaxWidth().height(18.dp)
                    .background(brush = brush, shape = RoundedCornerShape(12.dp))
            )
            Spacer(
                modifier = Modifier.width(130.dp).height(18.dp)
                    .background(brush = brush, shape = RoundedCornerShape(12.dp))
            )

        }
    }
}