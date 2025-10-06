package com.example.chattingappscreens.presentation.chatting

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@SuppressLint("RememberInComposition")
@Composable
fun Editdialog(
    title: String,
    data: List<DialogHint>,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {

        ElevatedCard(modifier = Modifier.wrapContentSize()
            .clickable(
                indication = null,
                interactionSource =  MutableInteractionSource(),
                onClick = {}                                         // isme kuch nahi rkhenge ab isek control work krenge
            ),) {
        Column(modifier = Modifier.width(200.dp)
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,){

            Text(title , fontWeight = FontWeight.Bold , fontSize = 18.sp)

            Spacer(modifier = Modifier.height(24.dp))
            data.forEach {
                if (it != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(it.label , fontWeight = FontWeight.SemiBold)
                        IconButton(onClick = it.onClick ) {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = "delete",
                                modifier = Modifier.background(
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = CircleShape
                                ).padding(4.dp)
                            )
                        }
                    }
                }

            }
        }

        }
    }
}



data class  DialogHint(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)


