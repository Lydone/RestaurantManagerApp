package com.lydone.restaurantmanagerapp.ui.table

import android.app.DownloadManager
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.lydone.restaurantmanagerapp.R
import com.lydone.restaurantmanagerapp.data.Table
import com.lydone.restaurantmanagerapp.ui.AlertDialog
import com.lydone.restaurantmanagerapp.ui.DefaultLazyColumn
import com.lydone.restaurantmanagerapp.ui.FullscreenCircularProgressIndicator
import com.lydone.restaurantmanagerapp.ui.TwoPaneScreen

@Composable
fun TablesRoute(viewModel: TablesViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState().value
    val downloadManager = LocalContext.current.getSystemService(DownloadManager::class.java)
    var deleteTableDialog by remember { mutableStateOf<Table?>(null) }
    Screen(
        state = state,
        onTableQrClick = { downloadQrCode(downloadManager, it) },
        onAddTableClick = viewModel::addTable,
        onDeleteTableClick = { deleteTableDialog = it },
        onNumberChange = viewModel::changeNumber,
    )
    deleteTableDialog?.let { table ->
        AlertDialog(
            stringResource(R.string.delete_table_placeholder, table.number),
            onConfirm = { viewModel.deleteTable(table) },
            onDismiss = { deleteTableDialog = null },
        )
    }
}

private fun downloadQrCode(downloadManager: DownloadManager, table: Table) =
    downloadManager.enqueue(
        DownloadManager.Request(table.qrCodeUrl.toUri())
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "QR Table ${table.number}"
            )
    )

@Composable
private fun Screen(
    state: TablesViewModel.State,
    onTableQrClick: (Table) -> Unit,
    onAddTableClick: () -> Unit,
    onDeleteTableClick: (Table) -> Unit,
    onNumberChange: (String) -> Unit,
) = TwoPaneScreen(
    leftContent = { Tables(state, onTableQrClick, onDeleteTableClick) },
    rightContent = { AddTable(state, onNumberChange, onAddTableClick) },
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddTable(
    state: TablesViewModel.State,
    onNumberChange: (String) -> Unit,
    onAddTableClick: () -> Unit
) = Column(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp)) {
    OutlinedTextField(
        value = state.number,
        onValueChange = onNumberChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.table_number)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
    Button(onAddTableClick, Modifier.fillMaxWidth(), enabled = state.number.isNotEmpty()) {
        Text(stringResource(R.string.add_table))
    }
}

@Composable
private fun Tables(
    state: TablesViewModel.State,
    onTableQr: (Table) -> Unit,
    onTableDelete: (Table) -> Unit,
) {
    if (state.tables != null) {
        DefaultLazyColumn {
            items(state.tables) { table ->
                Table(
                    table = table,
                    onQrClick = { onTableQr(table) },
                    onDeleteClick = { onTableDelete(table) })
            }
        }
    } else {
        FullscreenCircularProgressIndicator()
    }
}

@Composable
private fun Table(table: Table, onQrClick: () -> Unit, onDeleteClick: () -> Unit) = OutlinedCard {
    Row(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp), Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.table_placeholder, table.number),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(onQrClick) {
            Icon(painterResource(R.drawable.baseline_qr_code_2_24), contentDescription = null)
        }
        IconButton(onDeleteClick) { Icon(Icons.Default.Delete, contentDescription = null) }
    }
}