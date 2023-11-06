package com.anakbaikbaik.findmystuff.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//UI Element for displaying a title
@Composable
fun OnBackgroundTitleText(text: String, modifier: Modifier) {
    TitleText(text = text, color =
    MaterialTheme.colorScheme.onBackground)
}
//Here, we use the titleLarge style from the typography
@Composable
fun TitleText(text: String, color: Color) {
    Text(text = text, style =
    MaterialTheme.typography.titleLarge, color = color)
}
//UI Element for displaying an item list
@Composable
fun OnBackgroundItemText(text: String) {
    ItemText(text = text, color =
    MaterialTheme.colorScheme.onBackground)
}
//Here, we use the bodySmall style from the typography
@Composable
fun ItemText(text: String, color: Color) {
    Text(text = text, style =
    MaterialTheme.typography.bodySmall, color = color)
}
//UI Element for displaying a button
@Composable
fun PrimaryTextButton(text: String, onClick: () -> Unit) {
    editButton(
        text = text,
        textColor = Color.White,
        onClick = onClick
    )
}

@Composable
fun GreenTextButton(text: String, onClick: () -> Unit) {
    acceptButton(
        text = text,
        textColor = Color.White,
        onClick = onClick
    )
}

@Composable
fun RedTextButton(text: String, onClick: () -> Unit) {
    delButton(
        text = text,
        textColor = Color.White,
        onClick = onClick
    )
}
//Here, we use the labelMedium style from the typography
@Composable
fun editButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Color.Black,
                contentColor = textColor
            )
    ) {
        Text(text = text, style =
        MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun delButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Merah,
                contentColor = textColor
            )
    ) {
        Text(text = text, style =
        MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun acceptButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Hijau,
                contentColor = textColor
            )
    ) {
        Text(text = text, style =
        MaterialTheme.typography.labelMedium)
    }
}
