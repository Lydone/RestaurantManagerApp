package com.lydone.restaurantmanagerapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(
    items: List<String>,
    selectedItemIndex: Int?,
    label: String,
    onItemSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it }, modifier) {
        OutlinedTextField(
            value = if (selectedItemIndex != null) items[selectedItemIndex] else "",
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    painterResource(
                        if (expanded) {
                            R.drawable.baseline_arrow_drop_up_24
                        } else {
                            R.drawable.baseline_arrow_drop_down_24
                        }
                    ),
                    contentDescription = null,
                )
            }
        )
        ExposedDropdownMenu(expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelect(index)
                        expanded = false
                    }
                )
            }

        }
    }
}