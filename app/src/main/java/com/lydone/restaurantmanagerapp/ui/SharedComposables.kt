package com.lydone.restaurantmanagerapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
fun AlertDialog(title: String, onDismiss: () -> Unit, onConfirm: () -> Unit) = AlertDialog(
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
    title = { Text(title) }
)