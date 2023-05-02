package com.lydone.restaurantmanagerapp.ui.dish

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lydone.restaurantmanagerapp.Application
import com.lydone.restaurantmanagerapp.data.AddDishRequest
import com.lydone.restaurantmanagerapp.data.Category
import com.lydone.restaurantmanagerapp.data.Dish
import com.lydone.restaurantmanagerapp.data.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val menuRepository: MenuRepository
) : AndroidViewModel(context as Application) {
    private var dishes: List<Dish>? = null
    private val _state = MutableStateFlow(
        State(
            searchText = "",
            dishes = dishes,
            image = null,
            name = "",
            description = "",
            price = null,
            weight = null,
            categories = null,
            category = null,
        )
    )
    val state = _state.asStateFlow()

    init {
        updateData()
    }

    fun updateData() {
        viewModelScope.launch {
            launch { loadDishes() }
            launch {
                val categories = menuRepository.getCategories()
                _state.update {
                    it.copy(
                        categories = categories,
                        category = categories.firstOrNull(),
                    )
                }
            }
        }
    }

    private fun filterDishes(text: String) =
        dishes?.filter { it.name.contains(text, ignoreCase = true) }

    fun changeSearch(value: String) {
        _state.update { it.copy(searchText = value, dishes = filterDishes(value)) }
    }

    fun addToStopList(dish: Dish) = viewModelScope.launch {
        menuRepository.addToStopList(dish.id)
        loadDishes()
    }

    fun removeFromStopList(dish: Dish) = viewModelScope.launch {
        menuRepository.removeFromStopList(dish.id)
        loadDishes()
    }

    fun deleteDish(dish: Dish) = viewModelScope.launch {
        menuRepository.deleteDish(dish.id)
        loadDishes()
    }

    fun changeImage(value: Uri?) = _state.update { it.copy(image = value) }
    fun changeName(value: String) = _state.update { it.copy(name = value) }
    fun changeDescription(value: String) = _state.update { it.copy(description = value) }
    fun changePrice(value: String) = _state.update {
        it.copy(price = if (value.isEmpty()) null else value.toIntOrNull() ?: it.price)
    }

    fun changeWeight(value: String) = _state.update {
        it.copy(weight = if (value.isEmpty()) null else value.toIntOrNull() ?: it.weight)
    }

    fun changeCategory(value: Category) = _state.update { it.copy(category = value) }

    fun addDish() = viewModelScope.launch {
        val state = _state.value
        val imageBase64: String
        getApplication<Application>().contentResolver.openInputStream(_state.value.image!!).use {
            imageBase64 = Base64.encodeToString(it!!.readBytes(), Base64.NO_WRAP)
        }
        menuRepository.addDish(
            AddDishRequest(
                imageBase64,
                state.name,
                state.description,
                state.price!!,
                state.weight!!,
                state.category!!.id,
            )
        )
        _state.update {
            it.copy(
                image = null,
                name = "",
                description = "",
                price = null,
                weight = null
            )
        }
        loadDishes()
    }

    private suspend fun loadDishes() {
        dishes = menuRepository.getDishes().sortedBy(Dish::name)
        _state.update { it.copy(dishes = filterDishes(it.searchText)) }
    }

    data class State(
        val searchText: String,
        val dishes: List<Dish>?,
        val image: Uri?,
        val name: String,
        val description: String,
        val price: Int?,
        val weight: Int?,
        val categories: List<Category>?,
        val category: Category?
    ) {
        val canAddDish = image != null && name.isNotEmpty() && description.isNotEmpty() &&
                price != null && weight != null && category != null
    }
}