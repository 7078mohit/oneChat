package com.example.chattingappscreens.presentation.Profile

import android.net.Uri
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.chattingappscreens.R
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EditProfileScreen(snackbarHostState: SnackbarHostState , navHostController: NavHostController) {

    val authViewModel : AuthViewModel = koinInject()
    val uid by authViewModel.uidPreference.collectAsState()
    val namePreference by authViewModel.namePreference.collectAsState()
    val imagePreference by authViewModel.profilePreference.collectAsState()
    val phonePreference by authViewModel.phonePreference.collectAsState()

    val updateProfileImageState by authViewModel.updateUserProfileState.collectAsState()
    val updateUserDetailState by authViewModel.updateUserState.collectAsState()
    var name by remember { mutableStateOf<String?>(null) }
    var phone by remember { mutableStateOf<String?>(null) }
    var profile by remember { mutableStateOf<String?>(null) }
    var readOnly by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri ->
        if (uri != null){
            profile = uri.toString()
        }
    }

    LaunchedEffect(updateUserDetailState.isError != null || updateProfileImageState.isError != null) {
        if(updateUserDetailState.isError != null){
            snackbarHostState.showSnackbar(message = "Edit Details Failed!!" , duration = SnackbarDuration.Long)
        }
        if (updateProfileImageState.isError != null){
            snackbarHostState.showSnackbar(message = "Edit Profile Failed!!" , duration = SnackbarDuration.Long)

        }
    }

    LaunchedEffect(updateUserDetailState.isSuccess == true || updateProfileImageState.isSuccess == true) {
        if (updateUserDetailState.isSuccess == true){
            Toast.makeText(context , "Details Updated" , Toast.LENGTH_SHORT).show()
            name = null
            phone = null
            profile = null
            readOnly = true

        }
        if (updateProfileImageState.isSuccess == true){
            Toast.makeText(context , "Profile Updated" , Toast.LENGTH_SHORT).show()
            profile = null
        }
    }



    Column(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.height(130.dp)) {
                    GlideImage(
                        model = profile ?: imagePreference,
                        contentScale = ContentScale.Crop,
                        contentDescription = "image",
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),

                        onClick = {
                            launcher.launch("image/*")
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 4.dp,
                                bottom = 10.dp)
                            .align(
                                Alignment.BottomEnd
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clip(RoundedCornerShape(18.dp))


                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit, contentDescription = "image",
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (!profile.isNullOrEmpty()){
                Button(
                    onClick = {
                        authViewModel.updateUserProfile(profile ?: "")
                              },
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ){
                    if (updateProfileImageState.isLoading == true) {
                        CircularProgressIndicator(
                            trackColor = MaterialTheme.colorScheme.surface,
                            modifier=Modifier.size(24.dp)
                        )
                    }
                    else {
                        Text(text = "Update Image")
                    }

                }
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        profile = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceDim,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ){
                    Text("Cancel")
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(text = "Name", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                readOnly = readOnly,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                value = name ?: namePreference,
                onValueChange = { name = it },
//                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Phone No", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                readOnly = readOnly,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                value = phone ?: phonePreference,
                onValueChange = { phone = it },
//                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )
            Spacer(modifier = Modifier.height(40.dp))

            if(!readOnly){
                Button(
                    onClick = {
                        authViewModel.updateUser(
                            uid = uid,
                            name = name  ?: namePreference,
                            phoneNo = phone ?: phonePreference
                        )
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (updateUserDetailState.isLoading == true){
                        CircularProgressIndicator(
                            trackColor = MaterialTheme.colorScheme.surface,
                            modifier=Modifier.size(24.dp)
                        )
                    }
                    else{
                    Text("Save")
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        name = null
                        phone = null
                        readOnly = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceDim,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                ){
                    Text("Cancel")
                }
            }
            else {
                Button(
                    onClick = { readOnly = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Edit details")

                }
            }

        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun editprof() {

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri

            }
        }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "Add Profile",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .height(200.dp)
        ) {

            if (imageUri != null) {
                GlideImage(
                    model = "",
                    contentDescription = "profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.addprofile),
                    contentDescription = "profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                )
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .border(1.dp, color = MaterialTheme.colorScheme.outline),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                onClick = { imageLauncher.launch("image/*") }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "plus",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        TextButton(
            onClick = {
                imageUri = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Cancel")

        }

        TextButton(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            border = BorderStroke(width = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Add")

        }
    }
}

