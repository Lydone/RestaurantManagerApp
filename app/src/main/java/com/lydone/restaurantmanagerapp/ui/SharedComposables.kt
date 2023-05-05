package com.lydone.restaurantmanagerapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lydone.restaurantmanagerapp.R

@Composable
fun TwoPaneScreen(
    leftContent: @Composable BoxScope.() -> Unit,
    rightContent: @Composable BoxScope.() -> Unit
) = Row {
    Box(Modifier.weight(1f)) { leftContent() }
    Divider(
        Modifier
            .fillMaxHeight()
            .width(DividerDefaults.Thickness)
    )
    Box(Modifier.weight(1f)) { rightContent() }
}

@Composable
fun AlertDialog(title: String, onDismiss: () -> Unit, onConfirm: () -> Unit, text: String? = null) =
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) { Text(stringResource(R.string.yes)) }
        },
        dismissButton = { TextButton(onDismiss) { Text(stringResource(R.string.no)) } },
        title = { Text(title) },
        text = text?.let { { Text(it) } }
    )

@Composable
fun FullscreenCircularProgressIndicator() = Box(Modifier.fillMaxSize()) {
    CircularProgressIndicator(Modifier.align(Alignment.Center))
}

@Composable
fun DefaultLazyColumn(
    content: LazyListScope.() -> Unit
) = LazyColumn(
    Modifier.padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    content = content,
)