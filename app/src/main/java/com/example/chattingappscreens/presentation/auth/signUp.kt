package com.example.chattingappscreens.presentation.auth

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.presentation.NavGraph.Auth
import com.example.chattingappscreens.presentation.NavGraph.Home
import com.example.chattingappscreens.presentation.NavGraph.Route
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignUp(navHostController: NavHostController, modifier: Modifier , snackBarHostState: SnackbarHostState) {

    val viewMode: AuthViewModel = koinViewModel()

    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var privacyChecked by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val enabled =
        userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    val context = LocalContext.current
    var matchPassword by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    var profileAdded by remember { mutableStateOf(false) }
    var token by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()


    val scrollableState = rememberScrollState()

    val viewModel: AuthViewModel = koinViewModel()


    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()
    val googleState by viewModel.signingState.collectAsStateWithLifecycle()



    val launcher =  rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        val idToken = task.result.idToken
        if ( idToken != null) {
            token = idToken
        }


        if (result.resultCode == Activity.RESULT_CANCELED) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = "Google signUp Cancelled!",
                    duration = SnackbarDuration.Long
                )
            }
        }
    }


    LaunchedEffect(token) {
        if (token.isNotEmpty()){
            viewModel.signIngWithGoogle(idToken = token)
        }
    }




//    LaunchedEffect(googleState.success , signUpState.success) {
//
//    }


    LaunchedEffect(signUpState.error , googleState.error) {
        if (signUpState.error != null){
            snackBarHostState.showSnackbar(
                message = signUpState.error  ?: "Sign Up Failed",
                duration = SnackbarDuration.Short,
            )
        }
        if (googleState.error != null){
            snackBarHostState.showSnackbar(
                message = googleState.error ?: "Google Sign IN Failed",
                duration = SnackbarDuration.Short,
            )

        }
    }



    when {
        signUpState.loading  || googleState.loading -> {

            AnimateLottie(rawFile = R.raw.loadinganimationblue , text = "Loading...")
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center,
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(32.dp),
//                    strokeWidth = 6.dp,
//                    trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor,
//                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
//                )
//            }
        }

        !signUpState.success.isNullOrEmpty() || !googleState.success.isNullOrEmpty()  -> {
            AnimateLottie(R.raw.successanimation , text = "SignUp Successfully")
//            Toast.makeText(context , "SignUp Successfully" , Toast.LENGTH_LONG).show()
            LaunchedEffect(Unit) {
                delay(3000)
                navHostController.navigate(Home.Contact.name) {
                    popUpTo(Route.Auth.name) {
                        inclusive = true
                    }
                }
            }
        }


        else -> {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollableState, enabled = true)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = "Create an Account",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(60.dp))
                OutlinedTextField(
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = {
                        userName = it
                        nameError = if (it.isEmpty()) "Name cannot be Empty!" else null
                    },
                    shape = MaterialTheme.shapes.medium,
                    supportingText = if (nameError != null) {
                        { Text(nameError.orEmpty(), color = MaterialTheme.colorScheme.error) }
                    } else null,
                    placeholder = { Text(text = "Full Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    value = phone,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    onValueChange = {
                        phone = it
                        phoneError = if (it.length != 10) "Phone no must be 10 digit" else null
                    },
                    shape = MaterialTheme.shapes.medium,
                    supportingText = if (phoneError != null) {
                        { Text(phoneError.orEmpty(), color = MaterialTheme.colorScheme.error) }
                    } else null,
                    placeholder = { Text(text = "Phone Number") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (isValidEmail(it)) null else "Invalid Email!"
                    },
                    supportingText = if (emailError != null) {
                        { Text(emailError.orEmpty(), color = MaterialTheme.colorScheme.error) }
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
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = if (it.length >= 6) null else "Invalid Password!"
                    },
                    supportingText = if (passwordError != null) {
                        { Text(passwordError.orEmpty(), color = MaterialTheme.colorScheme.error) }
                    } else null,
                    visualTransformation = if (showPassword) {
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
                        if (showPassword) {
                            IconButton(onClick = { showPassword = false }) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "show password"
                                )
                            }
                        } else {
                            IconButton(onClick = { showPassword = true }) {
                                Icon(
                                    imageVector = Icons.Default.VisibilityOff,
                                    contentDescription = "hide password"
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        matchPassword = if (password != it) "Passwords do not match!" else null
                    },
                    shape = MaterialTheme.shapes.medium,
                    placeholder = { Text(text = "Confirm Password") },
                    supportingText = if (matchPassword != null) {
                        { Text(matchPassword.orEmpty(), color = MaterialTheme.colorScheme.error) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    ),
                    visualTransformation = if (showConfirmPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        if (showConfirmPassword) {
                            IconButton(onClick = { showConfirmPassword = false }) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "show password"
                                )
                            }
                        } else {
                            IconButton(onClick = { showConfirmPassword = true }) {
                                Icon(
                                    imageVector = Icons.Default.VisibilityOff,
                                    contentDescription = "hide password"
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(35.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(

                        checked = privacyChecked,
                        onCheckedChange = { privacyChecked = it },
                        enabled = enabled,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outlineVariant,
                        )
                    )
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = buildAnnotatedString {
                            append("I agree to the ")
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                            {
                                append("Terms & Conditions ")
                            }
                            append("and ")
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                append("PrivacyScreen Policy")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))



                Button(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    onClick = {

                        nameError = if (userName.isNotEmpty()) null else "Name cannot be Empty!"
                        phoneError = if (phone.length  == 10 ) null else "Phone no must be 10 digit"
                        emailError = if (isValidEmail(email)) null else "Invalid Email!"
                        passwordError =
                            if (password.length >= 6) null else "Password must be at least 6 chars!"
                        matchPassword =
                            if (password != confirmPassword) "Passwords do not match!" else null
                        val checkbox = if (privacyChecked) null else "Tick on the Terms & Condition"

                        val text =
                            emailError != null || passwordError != null || matchPassword != null || nameError != null  || phoneError != null


                        if (text) {
                            Toast.makeText(context, "fix all the errors", Toast.LENGTH_SHORT).show()
                        } else {
                            if (checkbox != null) {
                                Toast.makeText(context, checkbox, Toast.LENGTH_SHORT).show()
                            } else {
                                if (imageUri == null){
                                    showSheet = true
                                } else {
                                    if (profileAdded){
                                        val user = UserModel(
                                            name =  userName,
                                            phone = phone,
                                            email = email,
                                            password = password,
                                             profile = imageUri.toString()
                                        )
                                        viewMode.signUpWithEmail(user = user)
                                    }
                                    //signup hai ye

                                }
                            }
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Sign Up")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Or sign up with",
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
                                    }else{
                                        snackBarHostState.showSnackbar(
                                            message = "Google SignUp Cancelled!",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }catch (e : Exception){
                                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(context.getString(R.string.web_client_id))
                                        .requestEmail()
                                        .build()

                                    val getIntent  = GoogleSignIn.getClient(context,gso)
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
                    onClick = { navHostController.navigate(Auth.SignIn.name){
                        popUpTo(Auth.Signup.name){
                            inclusive = true
                        }
                    } }
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                append("Already have an account? ")
                            }

                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append("Sign In")
                            }
                        })
                }

                AddProfileScreen(onDismiss = {
                    showSheet = false
                }, showSheet = showSheet, onAdded = { uri ->
                    uri?.let {
                        imageUri = uri
                        profileAdded = true
                    }
                })


            }
        }
    }
}