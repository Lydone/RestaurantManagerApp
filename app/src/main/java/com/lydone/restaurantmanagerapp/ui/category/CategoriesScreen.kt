package com.lydone.restaurantmanagerapp.ui.category

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lydone.restaurantmanagerapp.R
import com.lydone.restaurantmanagerapp.data.Category
import com.lydone.restaurantmanagerapp.ui.AlertDialog
import com.lydone.restaurantmanagerapp.ui.DefaultLazyColumn
import com.lydone.restaurantmanagerapp.ui.FullscreenCircularProgressIndicator
import com.lydone.restaurantmanagerapp.ui.TwoPaneScreen

@Composable
fun CategoriesRoute(viewModel: CategoriesViewModel = hiltViewModel()) {
    var deleteCategoryDialog by remember { mutableStateOf<Category?>(null) }
    Screen(
        state = viewModel.state.collectAsState().value,
        onDeleteCategory = { deleteCategoryDialog = it },
        onNameChange = viewModel::changeName,
        onAddCategory = viewModel::addCategory,
    )
    deleteCategoryDialog?.let { category ->
        AlertDialog(
            title = stringResource(R.string.delete_category_placeholder, category.name),
            onDismiss = { deleteCategoryDialog = null },
            onConfirm = { viewModel.deleteCategory(category) },
            text = stringResource(R.string.delete_category_warning),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    state: CategoriesViewModel.State,
    onDeleteCategory: (Category) -> Unit,
    onNameChange: (String) -> Unit,
    onAddCategory: () -> Unit,
) {
    TwoPaneScreen(
        leftContent = { Categories(state, onDeleteCategory) },
        rightContent = {
            Column(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
                )
                Button(onAddCategory, Modifier.fillMaxWidth(), enabled = state.name.isNotEmpty()) {
                    Text(stringResource(R.string.add_category))
                }
            }
        }
    )
}

@Composable
private fun Categories(
    state: CategoriesViewModel.State,
    onDeleteCategory: (Category) -> Unit
) {
    if (state.categories != null) {
        DefaultLazyColumn {
            items(state.categories) { Category(it, onDelete = { onDeleteCategory(it) }) }
        }
    } else {
        FullscreenCircularProgressIndicator()
    }
}

@Composable
private fun Category(category: Category, onDelete: () -> Unit) = OutlinedCard {
    Row(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp), Alignment.CenterVertically) {
        Text(category.name, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
        IconButton(onDelete) { Icon(Icons.Default.Delete, contentDescription = null) }
    }
}