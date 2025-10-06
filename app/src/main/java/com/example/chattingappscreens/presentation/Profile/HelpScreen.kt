package com.example.chattingappscreens.presentation.Profile

import android.webkit.WebView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.chattingappscreens.R
import com.example.chattingappscreens.core.utils.IntentUtils
import com.example.chattingappscreens.presentation.NavGraph.Out

@Composable
fun HelpScreen(navHostController: NavHostController, rootNavHost: NavHostController) {

    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp , horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {navHostController.popBackStack()}) {
                Icon(Icons.Default.ArrowBack , contentDescription = "back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Help centre",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(12.dp).verticalScroll(rememberScrollState())) {
            Text(
                text = "Contact",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 150.dp, max = 400.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(12.dp)
                    ),

                ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {                                         // isko navhost me get krke webwiew me pass kr denge
                            IntentUtils.sendEmail(context =context , text = text)
                            text = ""
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.surface
                    ),
                    placeholder = { Text(text = "Message to help centre..") }
                )
            }
            Spacer(modifier =Modifier.weight(1f))

            Text(
                text = "Privacy policy",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            ElevatedButton(
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                   // rootNavHost.currentBackStackEntry?.savedStateHandle?.set(key = "url" , value = context.getString(R.string.privacy_policy))

                    rootNavHost.navigate(Out.Webview.name)
                }
            ){
                Icon(imageVector = Icons.Default.Policy , contentDescription = "message", tint = MaterialTheme.colorScheme.surface, modifier = Modifier.size(30.dp))

            }
        }
    }
}


