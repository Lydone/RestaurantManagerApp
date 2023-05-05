package com.lydone.restaurantmanagerapp.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lydone.restaurantmanagerapp.data.Category
import com.lydone.restaurantmanagerapp.data.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val menuRepository: MenuRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(State(null, ""))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch { loadCategories() }
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        menuRepository.deleteCategory(category.id)
        loadCategories()
    }

    fun addCategory() = viewModelScope.launch {
        menuRepository.addCategory(state.value.name)
        _state.update { it.copy(name = "") }
        loadCategories()
    }

    fun changeName(value: String) = _state.update { it.copy(name = value) }

    private suspend fun loadCategories() {
        _state.update { it.copy(categories = null) }
        _state.update { it.copy(categories = menuRepository.getCategories()) }
    }

    data class State(
        val categories: List<Category>?,
        val name: String,
    )
}