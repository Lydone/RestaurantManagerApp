package com.lydone.restaurantmanagerapp.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lydone.restaurantmanagerapp.data.Order
import com.lydone.restaurantmanagerapp.data.OrderRepository
import com.lydone.restaurantmanagerapp.data.Table
import com.lydone.restaurantmanagerapp.data.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    tableRepository: TableRepository,
    private val orderRepository: OrderRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        State(
            table = null,
            tables = null,
            orders = null,
            currentOrderId = null,
        )
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val tables = tableRepository.getTables()
            val table = tables.first()
            _state.update {
                it.copy(
                    table = table,
                    tables = tables,
                    orders = orderRepository.getOrders(table.number)
                )
            }
        }
    }

    fun changeTable(table: Table) {
        _state.update { it.copy(table = table, orders = null) }
        viewModelScope.launch {
            _state.update { it.copy(orders = orderRepository.getOrders(table.number)) }
        }
    }

    fun setCurrentOrder(order: Order) {
        _state.update { it.copy(currentOrderId = order.id) }
    }

    fun deleteOrder(order: Order) {
        // TODO
    }

    fun deleteEntry(entry: Order.Entry) {
        viewModelScope.launch {
            orderRepository.deleteOrderEntry(entry.id)
            _state.update { it.copy(orders = orderRepository.getOrders(it.table!!.number)) }
        }
    }

    data class State(
        val table: Table?,
        val tables: List<Table>?,
        val orders: List<Order>?,
        private val currentOrderId: Int?
    ) {

        val currentOrder = orders?.firstOrNull { it.id == currentOrderId }
    }
}