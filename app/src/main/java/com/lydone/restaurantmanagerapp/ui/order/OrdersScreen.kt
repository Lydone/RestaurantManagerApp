package com.lydone.restaurantmanagerapp.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lydone.restaurantmanagerapp.R
import com.lydone.restaurantmanagerapp.data.Order
import com.lydone.restaurantmanagerapp.data.Table
import com.lydone.restaurantmanagerapp.ui.AlertDialog
import com.lydone.restaurantmanagerapp.ui.DefaultLazyColumn
import com.lydone.restaurantmanagerapp.ui.ExposedDropdownMenu
import com.lydone.restaurantmanagerapp.ui.FullscreenCircularProgressIndicator
import com.lydone.restaurantmanagerapp.ui.TwoPaneScreen
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OrderRoute(viewModel: OrderViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState().value
    var deleteEntryDialog by remember { mutableStateOf<Order.Entry?>(null) }
    var deleteOrderDialog by remember { mutableStateOf<Order?>(null) }
    TwoPaneScreen(
        leftContent = {
            Orders(
                state = state,
                onTableSelect = viewModel::changeTable,
                onOrderClick = viewModel::setCurrentOrder,
                onDeleteOrderClick = { deleteOrderDialog = it },
            )
        },
        rightContent = {
            if (state.currentOrder != null) {
                DefaultLazyColumn {
                    items(state.currentOrder.entries) { entry ->
                        Entry(entry = entry, onDeleteClick = { deleteEntryDialog = entry })
                    }
                }
            }
        }
    )
    deleteEntryDialog?.let { entry ->
        AlertDialog(
            title = stringResource(R.string.delete_entry_placeholder, entry.dish.name),
            onDismiss = { deleteEntryDialog = null },
            onConfirm = { viewModel.deleteEntry(entry) })
    }
    deleteOrderDialog?.let { order ->
        AlertDialog(
            title = stringResource(R.string.delete_order_placeholder, order.id),
            onDismiss = { deleteOrderDialog = null },
            onConfirm = { viewModel.deleteOrder(order) },
            text = "Это приведет к удалению заказа целиком. Если необходимо удалить конкретное блюдо, воспользуйтесь кнопкой удаления напротив блюда."
        )
    }
}

@Composable
private fun Orders(
    state: OrderViewModel.State,
    onTableSelect: (Table) -> Unit,
    onOrderClick: (Order) -> Unit,
    onDeleteOrderClick: (Order) -> Unit,
) = Column {
    val tables = state.tables
    if (state.table != null && tables != null) {
        ExposedDropdownMenu(
            items = tables.map { table ->
                stringResource(R.string.table_placeholder, table.number)
            },
            selectedItemIndex = tables.indexOf(state.table),
            label = stringResource(R.string.table_number),
            onItemSelect = { onTableSelect(tables[it]) },
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
    }
    if (state.orders != null) {
        LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
            items(state.orders) { order ->
                Order(
                    order = order,
                    selected = order == state.currentOrder,
                    onClick = { onOrderClick(order) },
                    onDeleteClick = { onDeleteOrderClick(order) },
                )
            }
        }
    } else {
        FullscreenCircularProgressIndicator()
    }
}

@Composable
private fun Order(order: Order, selected: Boolean, onClick: () -> Unit, onDeleteClick: () -> Unit) =
    Box(
        Modifier
            .clickable(onClick = onClick)
            .then(
                if (selected) {
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                } else {
                    Modifier
                }
            )
    ) {
        Row(Modifier.padding(16.dp)) {
            Column(Modifier.weight(1f), Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.order_placeholder, order.id))
                Text(
                    if (order.closeInstant != null) {
                        stringResource(
                            R.string.order_closed_placeholder,
                            format(order.closeInstant)
                        )
                    } else {
                        stringResource(R.string.active_order)
                    }
                )
            }
            IconButton(onDeleteClick) { Icon(Icons.Default.Delete, contentDescription = null) }
        }
        Divider(Modifier.align(Alignment.BottomCenter))
    }

@Composable
private fun Entry(entry: Order.Entry, onDeleteClick: () -> Unit) = OutlinedCard {
    Row(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp), Alignment.CenterVertically) {
        Column(Modifier.weight(1f), Arrangement.spacedBy(4.dp)) {
            Text(entry.dish.name, style = MaterialTheme.typography.titleLarge)
            Text(
                entry.comment ?: stringResource(
                    R.string.status_instant_placeholder,
                    stringResource(entry.status),
                    format(entry.instant)
                )
            )
        }
        IconButton(onDeleteClick) { Icon(Icons.Default.Delete, contentDescription = null) }
    }
}

@Composable
private fun format(instant: Instant) =
    DateTimeFormatter.ofPattern(stringResource(R.string.date_time_pattern))
        .format(instant.toJavaInstant().atZone(ZoneId.systemDefault()))

@Composable
private fun stringResource(status: Order.Entry.Status) = stringResource(
    when (status) {
        Order.Entry.Status.QUEUE -> R.string.status_queue
        Order.Entry.Status.COOKING -> R.string.status_cooking
        Order.Entry.Status.READY -> R.string.status_ready
    }
)
