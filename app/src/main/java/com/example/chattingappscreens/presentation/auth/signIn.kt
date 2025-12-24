package com.example.chattingappscreens.presentation.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.chattingappscreens.R
import com.example.chattingappscreens.core.common.AnimateLottie
import com.example.chattingappscreens.core.common.GoogleHelper
import com.example.chattingappscreens.core.common.isValidEmail
import com.example.chattingappscreens.presentation.NavGraph.Auth
import com.example.chattingappscreens.presentation.NavGraph.Home
import com.example.chattingappscreens.presentation.NavGraph.Route
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@Composable
fun SignIn(navHostController: NavHostController, modifier: Modifier , snackBarHostState: SnackbarHostState) {


    val viewModel: AuthViewModel = koinViewModel()

    val signingState by viewModel.signingState.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    var emailLogin by remember { mutableStateOf("") }
    var passwordLogin by remember { mutableStateOf("") }
    var showPasswordLogin by remember { mutableStateOf(false) }
    var emailErrorLogin by remember { mutableStateOf<String?>(null) }
    var passwordErrorLogin by remember { mutableStateOf<String?>(null) }
    var token by remember { mutableStateOf("") }
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            val idToken = task.result.idToken
            if (idToken != null) {
                token = idToken
            }

            if (result.resultCode == Activity.RESULT_CANCELED){
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        message = "Google signIn Cancelled!",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }




    LaunchedEffect(token){
        if (token.isNotEmpty()){
            viewModel.signIngWithGoogle(idToken = token)
        }
    }


//    LaunchedEffect(signingState.success, signInState.success) {
//
//    }

    LaunchedEffect(signingState.error, signInState.error) {
        if (signingState.error != null) {
            snackBarHostState.showSnackbar(
                message = signingState.error ?: "Google Signing Failed!"
            )
        }
        if (signInState.error != null) {
            snackBarHostState.showSnackbar(
                message = signInState.error ?: "SignIn Failed!"
            )
        }
    }


    when {
        signInState.loading || signingState.loading -> {
            AnimateLottie(rawFile = R.raw.loadinganimationblue , text = "Loading...")
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.siz e(32.dp),
//                    strokeWidth = 6.dp,
//                    trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor,
//                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
//                )
//            }
        }

        !signingState.success.isNullOrEmpty() || !signInState.success.isNullOrEmpty() -> {
//            Toast.makeText(context, "SignIn Successfully", Toast.LENGTH_SHORT).show()
            AnimateLottie(rawFile = R.raw.successanimation , text = "SignIn Successfully")
            LaunchedEffect(Unit) {
                delay(3000)
                navHostController.navigate(Home.Contact.name) {
                    popUpTo(Route.Auth.name) {
                        inclusive = true
                    }
                }
                //added
                viewModel.resetSignInState()
            }
        }


        else -> {

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(100.dp))
                    Text(
                        text = "Login to your Account",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    OutlinedTextField(
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        value = emailLogin,
                        onValueChange = {
                            emailLogin = it
                            emailErrorLogin = if (isValidEmail(it)) null else "Invalid Email!"
                        },
                        supportingText = if (emailErrorLogin != null) {
                            {
                                Text(
                                    emailErrorLogin.orEmpty(),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        } else null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = MaterialTheme.shapes.medium,
                        placeholder = { Text(text = "Email") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        maxLines = 1,
                        value = passwordLogin,
                        onValueChange = {
                            passwordLogin = it
                            passwordErrorLogin = if (it.length >= 6) null else "Invalid Password!"
                        },
                        supportingText = if (passwordErrorLogin != null) {
                            {
                                Text(
                                    passwordErrorLogin.orEmpty(),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        } else null,
                        visualTransformation = if (showPasswordLogin) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = MaterialTheme.shapes.medium,
                        placeholder = { Text(text = "Password") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        ),
                        trailingIcon = {
                            if (showPasswordLogin) {
                                IconButton(onClick = { showPasswordLogin = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = "show password"
                                    )
                                }
                            } else {
                                IconButton(onClick = { showPasswordLogin = true }) {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = "hide password"
                                    )
                                }
                            }
                        }
                    )



                    Spacer(modifier = Modifier.height(16.dp))
                    Button(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium,
                        onClick = {

                            emailErrorLogin =
                                if (isValidEmail(emailLogin)) null else "Invalid Email!"
                            passwordErrorLogin =
                                if (passwordLogin.length >= 6) null else "Invalid Password!"
                            val text = emailErrorLogin != null || passwordErrorLogin != null
                            if (text) {
                                // Toast.makeText(context,"Error: ${nameError.orEmpty()}${checkbox.orEmpty()}${emailError.orEmpty()}${passwordError.orEmpty()}${matchPassword.orEmpty()}" ,Toast.LENGTH_LONG).show()
                                Toast.makeText(context, "Please fix the errors", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                viewModel.signInWithEmail(
                                    email = emailLogin,
                                    password = passwordLogin
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Sign In")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Or sign in with",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        IconButton(
                            modifier = Modifier
                                .size(26.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val idToken = GoogleHelper.getIdToken(context)
                                        if (idToken != null) {
                                            token = idToken
                                        } else {
                                                snackBarHostState.showSnackbar(
                                                    message = "SignIn With Google Cancelled!",
                                                    duration = SnackbarDuration.Short
                                                )
                                        }
                                    }catch (e : Exception){               // ye gso sab yahi bnana hai turan butto ke sath
                                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestIdToken(context.getString(R.string.web_client_id))
                                            .requestEmail()
                                            .build()

                                        val getIntent = GoogleSignIn.getClient(context, gso)
                                        val intent = getIntent.signInIntent
                                        launcher.launch(intent)
                                    }
                                }
                            }) {

                            Icon(
                                painter = painterResource(R.drawable.google),
                                contentDescription = "google",
                                tint = Color.Unspecified
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        onClick = {
                            navHostController.navigate(Auth.Signup.name){
                                popUpTo(Auth.SignIn.name){
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    append("Don't have an account? ")
                                }

                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    append("Sign Up")
                                }
                            })
                    }
                }
            }
        }
    }

//@Preview(showBackground = true , showSystemUi = true)
//@Composable
//fun preview(){
//    SignIn()
//}
