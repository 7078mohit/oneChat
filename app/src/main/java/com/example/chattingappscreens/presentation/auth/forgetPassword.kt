package com.example.chattingappscreens.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chattingappscreens.core.common.isValidEmail

@Composable
fun ForgetPassword() {

    var emailForget by remember { mutableStateOf("") }
    var emailForgetError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current


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
            text = "Forget password",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            textAlign = TextAlign.Center,
            text = "Always keep your account secure\n and don't forget to update it",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(60.dp))
        OutlinedTextField(
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            value = emailForget,
            onValueChange = {
                emailForget = it
                emailForgetError = if (isValidEmail(it)) null else "Invalid Email!"
            },
            supportingText = if (emailForgetError != null) {
                { Text(emailForgetError.orEmpty(), color = MaterialTheme.colorScheme.error) }
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
        Button(

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {

                emailForgetError = if (isValidEmail(emailForget)) null else "Invalid Email!"
                if (emailForgetError != null) {
                    // Toast.makeText(context,"Error: ${nameError.orEmpty()}${checkbox.orEmpty()}${emailError.orEmpty()}${passwordError.orEmpty()}${matchPassword.orEmpty()}" ,Toast.LENGTH_LONG).show()
                    Toast.makeText(context, emailForgetError.orEmpty(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Otp sent successful!", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Send Otp")
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Prev() {
    ForgetPassword()
}