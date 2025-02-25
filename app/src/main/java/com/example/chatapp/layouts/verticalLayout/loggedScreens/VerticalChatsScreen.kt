package com.example.chatapp.layouts.verticalLayout.loggedScreens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.Dtos.contact.Contact
import com.example.chatapp.fakeContacts
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.ChatsUiState
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun copyToClipboard(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("password", text)
    clipboardManager.setPrimaryClip(clip)
}

@Composable
fun VerticalChatsScreen(
    chatsUiState: ChatsUiState,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {

        TextButton(onClick = {
            scope.launch {
                val token = Firebase.messaging.token.await()

                copyToClipboard(context,token)
            }
        }) {
            Text(text = "Copy Device Token")
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatsUiState.contacts) { contact ->
                ContactCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clip(RoundedCornerShape(20))
                        .clickable {

                        },
                    contact = contact
                )
            }
        }

    }
}

@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    contact: Contact
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(60.dp)
                .clip(CircleShape),
            model = contact.image,
            contentDescription = "contact image",
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = contact.name,
                style = typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = contact.lastMessage,
                style = typography.bodyMedium,
                color = colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold
            )

        }
    }

}

@Preview
@Composable
private fun previewPhoneChats() {
    ChatAppTheme(
        darkTheme = false
    ) {
        VerticalChatsScreen(
            chatsUiState = ChatsUiState(
                contacts = fakeContacts
            )
        )
    }
}

