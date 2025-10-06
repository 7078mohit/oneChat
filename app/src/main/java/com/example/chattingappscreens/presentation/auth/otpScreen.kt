package com.example.chattingappscreens.presentation.auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(){

    val totalTime = 60
    val onTimerFinish :() -> Unit           //ye ham parameters me lenge or call when the timeEnd
    var timeLeft by remember { mutableIntStateOf(totalTime) }
    val context = LocalContext.current
    val otpValues = remember{ mutableStateListOf( *Array(4){""}) }
    val focusRequester = List(4) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit){
        focusRequester.first().requestFocus()
        while (timeLeft > 0 ){
            delay(1000L)      //1s h ye
            timeLeft--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(12.dp),
                onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "arrowBack")
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "Verification Code",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            textAlign = TextAlign.Center,
            text = "We send the Verification OTP code \nto your Email input to forget Password",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.outline
                        )
                    ){
                        append("End time ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    ){
                        append("$timeLeft sec")
                    }
                }

            )

            TextButton(onClick = {},
                enabled = timeLeft == 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
               ) {
                Text(
                    text = "Resend Again?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 45.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            otpValues.forEachIndexed { index, value ->
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier

                        .size(54.dp)
                        .focusRequester(focusRequester[index])
                        .onKeyEvent { keyEvent ->
                            if(keyEvent.key == Key.Backspace){
                                if(otpValues[index].isEmpty() && index > 0 ){
                                    otpValues[index] = ""
                                    focusRequester[index - 1].requestFocus()
                                }
                                else{
                                    otpValues[index] = ""
                                }
                                true }

                            else{
                                false
                            }

                        },
                    shape = MaterialTheme.shapes.medium,
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1){
                            otpValues[index] = newValue
                            if (newValue.isNotEmpty()){
                                if (index < otpValues.size - 1){
                                    focusRequester[index + 1].requestFocus()
                                }
                                else{
                                    keyboardController?.hide()
                                }
                            }
                        }
                        else{
                            if(index < otpValues.size - 1){
                                focusRequester[index + 1].requestFocus()
                            }
                        }

                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    )

                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Verify")
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}
//
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun Preview() {
//
//}