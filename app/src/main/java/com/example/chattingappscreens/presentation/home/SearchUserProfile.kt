package com.example.chattingappscreens.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.chattingappscreens.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchUserProfile(){
    val image = "https://firebasestorage.googleapis.com/v0/b/chatting-app-91488.firebasestorage.app/o/Profile%2FImage_1758200675376_.jpg?alt=media&token=2a2c96e5-c423-4607-b69e-d3b1c1ff64bc"
    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp , vertical = 12.dp),
            verticalAlignment = CenterVertically
        ) {

            if (!image.isEmpty()){
                GlideImage(
                    model = image ,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            else{
                Image(
                    painter = painterResource(R.drawable.profilefilled) ,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
                    Text(modifier = Modifier,
                        text = "name",
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.weight(1f))

            ElevatedButton(
                onClick ={},
                modifier = Modifier.wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )) {
                Text(text = "message" , fontSize = 18.sp , )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preeee(){
    SearchUserProfile()
}