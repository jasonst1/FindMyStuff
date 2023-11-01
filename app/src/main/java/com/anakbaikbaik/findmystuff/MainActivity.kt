@file:OptIn(ExperimentalMaterial3Api::class)

package com.anakbaikbaik.findmystuff

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        modifier = Modifier.padding(top = 70.dp)
    ) {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Composable
fun topBar() {
    TopAppBar(title = {
        Text(
            text = "FindMyStuff",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
    })
}

@Composable
fun MessageCard(message: Message) {
    Column (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Black)
    ) {
        Row (
            modifier = Modifier.padding(8.dp)
        ) {
            Column (
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.monyet),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                )
            }

            Column (
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Column (
                    modifier = Modifier.padding(8.dp)
                ){
                    Text(
                        text = message.author,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = message.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ConversationPreview() {
    Conversation(SampleData.conversationSample)
}
