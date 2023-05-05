package com.lydone.restaurantmanagerapp.ui.table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lydone.restaurantmanagerapp.data.Table
import com.lydone.restaurantmanagerapp.data.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TablesViewModel @Inject constructor(private val tableRepository: TableRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(State(null, ""))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch { loadTables() }
    }

    fun deleteTable(table: Table) = viewModelScope.launch {
        tableRepository.deleteTable(table.number)
        loadTables()
    }

    fun changeNumber(value: String) {
        if (value.toIntOrNull() != null || value.isEmpty()) {
            _state.update { it.copy(number = value) }
        }
    }

    fun addTable() = viewModelScope.launch {
        tableRepository.addTable(state.value.number.toInt())
        loadTables()
    }

    private suspend fun loadTables() {
        _state.update { it.copy(tables = null) }
        _state.update { it.copy(tables = tableRepository.getTables()) }
    }

    data class State(val tables: List<Table>?, val number: String)
}