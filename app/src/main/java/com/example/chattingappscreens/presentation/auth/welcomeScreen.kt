package com.example.chattingappscreens.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chattingappscreens.presentation.NavGraph.Auth
import com.example.chattingappscreens.presentation.NavGraph.Home
import com.example.chattingappscreens.presentation.NavGraph.Route

@Composable
fun WelcomeScreen(navHostController: NavHostController, modifier: Modifier) {

    Column(
        modifier = modifier.fillMaxSize()
            .padding(horizontal = 16.dp)
    ){

        Spacer(modifier = Modifier.height(75.dp))

        Text(
            text = "Welcome",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Lets get started",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(170.dp))

        Text(
            text = "Existing user/Get started",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape =  MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = {navHostController.navigate(Auth.SignIn.name){
                popUpTo(Auth.Welcome.name){
                    inclusive = true
                }
            } }
        ) {
            Text("Sign in" , style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically,
           ) {
            Text(
                text = "New user?",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier =Modifier.width(2.dp))
                    Text(
                        text = "Create new account",
                         style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable(onClick = {
                            navHostController.navigate(Auth.Signup.name){
                                popUpTo(Auth.Welcome.name){
                                    inclusive = true
                                }
                            }
                        }),
                        color = MaterialTheme.colorScheme.primary,
                    )

            }
        }
    }


