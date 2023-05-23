package com.lydone.restaurantmanagerapp.ui.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lydone.restaurantmanagerapp.data.Account
import com.lydone.restaurantmanagerapp.data.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(private val repository: AccountRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(State(null, "", "", "", Account.Role.WAITER))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadAccounts()
        }
    }

    fun deleteAccount(account: Account) = viewModelScope.launch {
        try {
            repository.deleteAccount(account.login)
            loadAccounts()
        } catch (e: Exception) {
            Log.w("TAG", e)
        }

    }

    fun changeName(value: String) = _state.update { it.copy(name = value) }

    fun changeLogin(value: String) = _state.update { it.copy(login = value) }

    fun changePassword(value: String) = _state.update { it.copy(password = value) }

    fun changeRole(value: Account.Role) = _state.update { it.copy(role = value) }

    fun addAccount() = viewModelScope.launch {
        try {
            repository.addAccount(with(state.value) { Account(name, login, password, role) })
            _state.update { it.copy(name = "", login = "", password = "") }
            loadAccounts()
        } catch (e: Exception) {
            Log.w("TAG", e)
        }
    }

    private suspend fun loadAccounts() {
        _state.update { it.copy(accounts = null) }
        try {
            _state.update { it.copy(accounts = repository.getAccounts()) }
        } catch (e: Exception) {
            Log.w("TAG", e)
        }
    }

    data class State(
        val accounts: List<Account>?,
        val name: String,
        val login: String,
        val password: String,
        val role: Account.Role,
    ) {
        val roles = Account.Role.values()
        val canAddAccount = name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty()
    }
}