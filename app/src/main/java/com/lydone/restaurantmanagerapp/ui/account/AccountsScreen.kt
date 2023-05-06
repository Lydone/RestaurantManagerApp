package com.lydone.restaurantmanagerapp.ui.account

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lydone.restaurantmanagerapp.R
import com.lydone.restaurantmanagerapp.data.Account
import com.lydone.restaurantmanagerapp.ui.AlertDialog
import com.lydone.restaurantmanagerapp.ui.DefaultLazyColumn
import com.lydone.restaurantmanagerapp.ui.FullscreenCircularProgressIndicator
import com.lydone.restaurantmanagerapp.ui.TwoPaneScreen

@Composable
fun AccountsRoute(viewModel: AccountsViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState().value
    var deleteAccountDialog by remember { mutableStateOf<Account?>(null) }
    TwoPaneScreen(
        leftContent = {
            AccountsScreen(state, onDeleteAccountClick = { deleteAccountDialog = it })
        },
        rightContent = {
            AddAccount(
                state = state,
                onNameChange = viewModel::changeName,
                onLoginChange = viewModel::changeLogin,
                onPasswordChange = viewModel::changePassword,
                onRoleChange = viewModel::changeRole,
                onAddAccountClick = viewModel::addAccount,
            )
        }
    )
    deleteAccountDialog?.let { account ->
        AlertDialog(
            title = stringResource(R.string.delete_account_placeholder, account.login),
            onDismiss = { deleteAccountDialog = null },
            onConfirm = { viewModel.deleteAccount(account) }
        )
    }
}

@Composable
private fun AccountsScreen(
    state: AccountsViewModel.State,
    onDeleteAccountClick: (Account) -> Unit,
) {
    if (state.accounts != null) {
        DefaultLazyColumn {
            items(state.accounts) {
                Account(account = it, onDeleteClick = { onDeleteAccountClick(it) })
            }
        }
    } else {
        FullscreenCircularProgressIndicator()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddAccount(
    state: AccountsViewModel.State,
    onNameChange: (String) -> Unit,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRoleChange: (Account.Role) -> Unit,
    onAddAccountClick: () -> Unit,
) = Column(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp)) {
    OutlinedTextField(
        value = state.name,
        onValueChange = onNameChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.account_name)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(KeyboardCapitalization.Words),
    )
    OutlinedTextField(
        value = state.login,
        onValueChange = onLoginChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.login)) },
        singleLine = true,
    )
    OutlinedTextField(
        value = state.password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.pin_code)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
    )
    RolePicker(state.role, onRoleChange = onRoleChange)
    Button(onAddAccountClick, Modifier.fillMaxWidth(), enabled = state.canAddAccount) {
        Text(stringResource(R.string.add_account))
    }
}

@Composable
private fun Account(account: Account, onDeleteClick: () -> Unit) = OutlinedCard {
    Row(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp), Alignment.CenterVertically) {
        Column(Modifier.weight(1f), Arrangement.spacedBy(4.dp)) {
            Text(account.name, style = MaterialTheme.typography.titleLarge)
            Text(stringResource(account.role), style = MaterialTheme.typography.bodyLarge)
            Text(account.login, style = MaterialTheme.typography.labelLarge)
        }
        IconButton(onDeleteClick) { Icon(Icons.Default.Delete, contentDescription = null) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RolePicker(
    role: Account.Role,
    onRoleChange: (Account.Role) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = stringResource(role),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text(stringResource(R.string.role)) },
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
            Account.Role.values().forEach { role ->
                DropdownMenuItem(
                    text = { Text(stringResource(role)) },
                    onClick = {
                        onRoleChange(role)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun stringResource(role: Account.Role) = stringResource(
    when (role) {
        Account.Role.WAITER -> R.string.role_waiter
        Account.Role.COOK -> R.string.role_cook
        Account.Role.MANAGER -> R.string.role_manager
    }
)