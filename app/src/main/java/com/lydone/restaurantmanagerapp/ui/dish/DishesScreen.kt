package com.lydone.restaurantmanagerapp.ui.dish

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lydone.restaurantmanagerapp.R
import com.lydone.restaurantmanagerapp.data.Category
import com.lydone.restaurantmanagerapp.data.Dish
import com.lydone.restaurantmanagerapp.ui.AlertDialog
import com.lydone.restaurantmanagerapp.ui.TwoPaneScreen

@Composable
fun DishesRoute(viewModel: DishesViewModel = hiltViewModel()) {
    var deleteDishDialog by remember { mutableStateOf<Dish?>(null) }
    Screen(
        state = viewModel.state.collectAsState().value,
        onSearchChange = viewModel::changeSearch,
        onAddDishToStopList = viewModel::addToStopList,
        onRemoveDishFromStopList = viewModel::removeFromStopList,
        onDeleteDish = { deleteDishDialog = it },
        onNameChange = viewModel::changeName,
        onDescriptionChange = viewModel::changeDescription,
        onPriceChange = viewModel::changePrice,
        onWeightChange = viewModel::changeWeight,
        onCategoryChange = viewModel::changeCategory,
        onImageChange = viewModel::changeImage,
        onAddDish = viewModel::addDish
    )
    deleteDishDialog?.let { dish ->
        AlertDialog(title = stringResource(R.string.delete_dish_placeholder, dish.name),
            onDismiss = { deleteDishDialog = null },
            onConfirm = { viewModel.deleteDish(dish) }
        )
    }
    LaunchedEffect(Unit) {viewModel.updateData() }
}

@Composable
private fun Screen(
    state: DishesViewModel.State,
    onSearchChange: (String) -> Unit,
    onAddDishToStopList: (Dish) -> Unit,
    onRemoveDishFromStopList: (Dish) -> Unit,
    onDeleteDish: (Dish) -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onImageChange: (Uri?) -> Unit,
    onAddDish: () -> Unit,
) = TwoPaneScreen(
    leftContent = {
        Dishes(
            search = state.searchText,
            dishes = state.dishes,
            onSearchChange = onSearchChange,
            onAddDishToStopList = onAddDishToStopList,
            onRemoveDishFromStopList = onRemoveDishFromStopList,
            onDeleteDish = onDeleteDish,
        )
    },
    rightContent = {
        AddDish(
            state = state,
            onImageChange = onImageChange,
            onNameChange = onNameChange,
            onDescriptionChange = onDescriptionChange,
            onPriceChange = onPriceChange,
            onWeightChange = onWeightChange,
            onCategoryChange = onCategoryChange,
            onAddDish = onAddDish
        )
    }
)


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Dishes(
    search: String,
    dishes: List<Dish>?,
    onSearchChange: (String) -> Unit,
    onAddDishToStopList: (Dish) -> Unit,
    onRemoveDishFromStopList: (Dish) -> Unit,
    onDeleteDish: (Dish) -> Unit
) = Column(Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
    TextField(
        value = search,
        onValueChange = onSearchChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.label_search)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
    )
    if (dishes != null) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dishes, key = { it.id }) { dish ->
                Dish(
                    dish = dish,
                    onAddToStopList = { onAddDishToStopList(dish) },
                    onRemoveFromStopList = { onRemoveDishFromStopList(dish) },
                    onDelete = { onDeleteDish(dish) },
                )
            }
        }
    } else {
        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddDish(
    state: DishesViewModel.State,
    onImageChange: (Uri?) -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onAddDish: () -> Unit,
) = Column(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ImagePicker(state.image, onImageChange)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val textKeyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            )
            val numberKeyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            )
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.name)) },
                keyboardOptions = textKeyboardOptions,
                singleLine = true,
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.description)) },
                keyboardOptions = textKeyboardOptions,
            )
            OutlinedTextField(
                value = state.price?.toString().orEmpty(),
                onValueChange = onPriceChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.price)) },
                keyboardOptions = numberKeyboardOptions,
                singleLine = true,
            )
            OutlinedTextField(
                value = state.weight?.toString().orEmpty(),
                onValueChange = onWeightChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.weight)) },
                keyboardOptions = numberKeyboardOptions,
                singleLine = true,
            )
            CategoryPicker(state.category, state.categories, onCategoryChange)
        }
    }
    Button(onClick = onAddDish, Modifier.fillMaxWidth(), enabled = state.canAddDish) {
        Text(stringResource(R.string.add_dish))
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CategoryPicker(
    category: Category?,
    categories: List<Category>?,
    onCategoryChange: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = category?.name.orEmpty(),
            onValueChange = {},
            Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text(stringResource(R.string.category)) },
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
            categories.orEmpty().forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategoryChange(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ImagePicker(image: Uri?, onImagePick: (Uri?) -> Unit) {
    val photoPickerLauncher = rememberLauncherForActivityResult(PickVisualMedia(), onImagePick)
    val modifier = Modifier
        .size(150.dp)
        .clickable(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly)) })
    if (image != null) {
        AsyncImage(image, contentDescription = null, modifier, contentScale = ContentScale.Crop)
    } else {
        ChooseImagePlaceholder(modifier)
    }
}

@Composable
private fun ChooseImagePlaceholder(modifier: Modifier) = Column(
    modifier.background(MaterialTheme.colorScheme.primaryContainer),
    Arrangement.Center,
    Alignment.CenterHorizontally,
) {
    Icon(
        painterResource(R.drawable.baseline_image_24),
        contentDescription = null,
        Modifier.size(50.dp),
    )
    Text(
        stringResource(R.string.pick_image),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun Dish(
    dish: Dish,
    onAddToStopList: () -> Unit,
    onRemoveFromStopList: () -> Unit,
    onDelete: () -> Unit,
) = OutlinedCard {
    Row(
        modifier = if (dish.inStopList) {
            Modifier.background(MaterialTheme.colorScheme.errorContainer)
        } else {
            Modifier
        },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            dish.url,
            contentDescription = null,
            Modifier.size(170.dp),
            contentScale = ContentScale.Crop,
        )
        Column(Modifier.weight(1f), Arrangement.spacedBy(8.dp)) {
            Text(dish.name, style = MaterialTheme.typography.titleLarge)
            Text(dish.description, style = MaterialTheme.typography.bodyLarge)
            Text(
                stringResource(
                    R.string.weight_price_placeholder,
                    dish.category.name,
                    dish.weight,
                    dish.price
                ),
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Column {
            IconButton(onDelete) { Icon(Icons.Default.Delete, contentDescription = null) }
            IconButton(onClick = if (dish.inStopList) onRemoveFromStopList else onAddToStopList) {
                Icon(
                    painterResource(
                        if (dish.inStopList) {
                            R.drawable.baseline_playlist_add_24
                        } else {
                            R.drawable.baseline_playlist_remove_24
                        }
                    ), contentDescription = null
                )
            }
        }
    }
}