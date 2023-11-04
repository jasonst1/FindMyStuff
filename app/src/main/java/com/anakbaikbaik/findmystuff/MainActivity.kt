@file:OptIn(ExperimentalMaterial3Api::class)

package com.anakbaikbaik.findmystuff

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anakbaikbaik.findmystuff.ui.theme.White
import com.anakbaikbaik.findmystuff.ui.theme.warnaUMN
import com.anakbaikbaik.findmystuff.ui.theme.PrimaryTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    topBar = { topBar() },
                    content = {
                        Conversation(SampleData.conversationSample)
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* Handle FAB click here */ },
                            modifier = Modifier // Adjust alignment as needed
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.add),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

object SampleData {
    // Extended sample conversation data
    val conversationSample = listOf(
        Message("Alice", "Hey, how's it going?"),
        Message("Bob", "I'm good, how about you?"),
        Message("Carol", "Hi everyone, what are you discussing?"),
        Message("David", "We're talking about the upcoming team outing. Any suggestions?"),
        Message("Alice", "How about a beach BBQ this Saturday?"),
        Message("Bob", "Sounds great! I'll bring the grill."),
        Message("Carol", "I can prepare some salads and bring drinks."),
        Message("David", "I'll handle the music and games."),
        Message("Emily", "Perfect, I'll send out the invites!"),
        Message("Frank", "Shall we bring anything else?"),
        Message("Alice", "Think of anything you'd like for a picnic. Let's make a list."),
        Message("Bob", "I'll bring some beach volleyballs and a frisbee."),
        Message("Carol", "Don't forget sunscreen and hats, everyone."),
        Message("David", "Looking forward to it! See you all on Saturday!"),
        // Add as many messages as needed to create a realistic conversation
    )
}

data class Message(val author: String, val body: String)

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn (
        modifier = Modifier.padding(top = 64.dp)
    ) {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Composable
fun topBar() {
    TopAppBar(
        title = {
            Text(
                stringResource(id = R.string.app_name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = warnaUMN)
    )
}
@Composable
fun MessageCard(message: Message) {
    Column (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp
            )
            .border(1.dp, Color.Black)
    ) {
        Row (
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column (
                modifier = Modifier
                    .background(color = warnaUMN)
            ) {
                Image(
                    painter = painterResource(R.drawable.monyet),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row {
            Column (
                modifier = Modifier.padding(15.dp),
            ) {
                Text(
                    text = message.author,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )
            }
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                PrimaryTextButton(
                    text = stringResource(id = R.string.editButton),
                ) {
                    // ERROR HANDLING FOR EMPTY INPUTFIELD.NAME
//                onButtonClick()
                }
            }
            Column {
                RedTextButton(
                    text = stringResource(id = R.string.deleteButton)
                ) {
                    // ERROR HANDLING FOR EMPTY INPUTFIELD.NAME
//                onButtonClick()
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun ConversationPreview() {
//    Conversation(SampleData.conversationSample)
//}