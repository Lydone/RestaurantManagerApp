package com.lydone.restaurantmanagerapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lydone.restaurantmanagerapp.R

@Composable
fun ErrorPlaceholder() {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.error), style = MaterialTheme.typography.titleLarge)
        Button(onClick = { /*TODO*/ }) {
            Text(stringResource(R.string.repeat))
        }
    }
}