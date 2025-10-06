package com.example.chattingappscreens.core.common

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chattingappscreens.R


    @Composable
    fun AnimateLottie(rawFile: Int, text: String) {

        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(rawFile))

        val animateLottieComposition by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
            speed = 1f,
            restartOnPlay = true
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                LottieAnimation(
                    modifier = Modifier.size(220.dp),
                    composition = composition,
                    contentScale = ContentScale.Inside,
                    alignment = Alignment.Center,
                    progress = animateLottieComposition                // me agr progress pass na kru to image animate ni hogi bas aise hi  static image bn jayegi
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

//@Composable
//fun Loadinglogin(){
//
//    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading2))
//
//    val animateLottieComposition by animateLottieCompositionAsState(composition =  composition , isPlaying = true , iterations = LottieConstants.IterateForever , speed = 1f , restartOnPlay = true )
//
//    Box(modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center) {
//            LottieAnimation(
//                modifier = Modifier.size(220.dp),
//                composition = composition,
//                contentScale = ContentScale.Inside,
//                alignment = Alignment.Center,
//                progress = animateLottieComposition                // ye hm na bhi de or is val ke andr jo work kiya hai hm wo is lottie me define kre to same work krega ok
//            )
//            Spacer(modifier =Modifier.height(8.dp))
//            Text("Loading..." , fontWeight = FontWeight.SemiBold , fontSize = 30.sp, color = MaterialTheme.colorScheme.onSurface , style = MaterialTheme.typography.titleMedium)
//        }
//    }
//
//}
//

//
//@Composable
//fun LoadingSearch(){
//
//    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loadinganimationblue))
//
//    val animateLottieComposition by animateLottieCompositionAsState(composition =  composition , isPlaying = true , iterations = LottieConstants.IterateForever , speed = 1f , restartOnPlay = true )
//
//    Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
//        horizontalArrangement = Arrangement.Center
//        ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center) {
//            LottieAnimation(
//                modifier = Modifier.size(100.dp),
//                composition = composition,
//                contentScale = ContentScale.Inside,
//             //   alignment = Alignment.Center,
//                progress = animateLottieComposition                // ye hm na bhi de or is val ke andr jo work kiya hai hm wo is lottie me define kre to same work krega ok
//            )
//
//            Text("Loading..." , fontWeight = FontWeight.SemiBold , fontSize = 30.sp, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
//        }
//    }
//}
//

//
//@Composable
//fun emptycontact(){
//
//    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty))
//
//    val animateLottieComposition by animateLottieCompositionAsState(composition =  composition , isPlaying = true , iterations = LottieConstants.IterateForever , speed = 1f , restartOnPlay = true )
//
//    Box(modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center) {
//            LottieAnimation(
//                modifier = Modifier.size(220.dp),
//                composition = composition,
//                contentScale = ContentScale.Inside,
//                alignment = Alignment.Center,
//                progress = animateLottieComposition                // ye hm na bhi de or is val ke andr jo work kiya hai hm wo is lottie me define kre to same work krega ok
//            )
//            Spacer(modifier =Modifier.height(8.dp))
//            Text("Friends not found" , fontWeight = FontWeight.SemiBold , fontSize = 30.sp, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
//        }
//    }
//
//}}
//
//
//
