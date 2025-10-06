package com.example.chattingappscreens.presentation.auth


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.chattingappscreens.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProfileScreen(
    onDismiss:() -> Unit ,
    showSheet : Boolean ,
   onAdded:(Uri?)->Unit
){

    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val imageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent() ) {
        uri ->
        uri?.let {
            imageUri = uri
        }

        }


    if (showSheet){
        ModalBottomSheet(onDismissRequest = { },
            sheetState = rememberModalBottomSheetState(),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    text = "Add Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier
                    .height(200.dp)){

                    if (imageUri!=null){
                        AsyncImage(
                            model = imageUri  ,
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
                    }
                    else{
                        Image(painter = painterResource(R.drawable.addprofile), contentDescription = "profile",
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
                        onClick = {imageLauncher.launch("image/*")}){
                        Icon(imageVector = Icons.Outlined.Edit ,
                            contentDescription = "plus",
                            modifier = Modifier.size(30.dp))
                    }
                }

                TextButton(onClick = {
                    imageUri = null
                },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Remove")

                }

                TextButton(onClick = {
                    onAdded(imageUri)
                    onDismiss()
                },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
                    border = BorderStroke(width = 0.5.dp , color = MaterialTheme.colorScheme.outlineVariant),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Add")

                }
            }
        }
    }
}
