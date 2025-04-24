package com.example.coincap.ui.compose.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.coincap.R
import com.example.coincap.ui.theme.HaitiDark

/**
 * Top bar of the listview screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetListTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.title_topbar),
                fontSize = 32.sp,
                color = HaitiDark,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}