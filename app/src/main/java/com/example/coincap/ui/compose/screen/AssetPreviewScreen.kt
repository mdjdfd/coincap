package com.example.coincap.ui.compose.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coincap.R
import com.example.coincap.rp.model.Asset
import com.example.coincap.ui.compose.effect.Loader
import com.example.coincap.ui.theme.DarkBlue
import com.example.coincap.ui.theme.LimeGreen
import com.example.coincap.ui.theme.ParadisePink
import com.example.coincap.ui.theme.PurpleGrey80
import com.example.coincap.util.EventHandler
import com.example.coincap.util.Util
import com.example.coincap.util.observe
import com.example.coincap.vm.AssetPreviewContract
import com.example.coincap.vm.AssetPreviewViewModel

@Composable
fun AssetPreviewScreen(
    id: String,
    eventHandler: EventHandler,
    onBackRequest: (AssetPreviewContract.Effect.Back) -> Unit
) {
    val context = LocalContext.current
    val viewModel =
        hiltViewModel<AssetPreviewViewModel, AssetPreviewViewModel.AssetPreviewViewModelFactory> {
            it.create(id)
        }

    val messageLoaded = stringResource(id = R.string.loaded)
    val messageError = stringResource(id = R.string.error)

    val state = viewModel.observe {
        when (it) {
            is AssetPreviewContract.Effect.Loaded -> {
                Toast.makeText(context, messageLoaded, Toast.LENGTH_SHORT).show()
            }

            is AssetPreviewContract.Effect.Back -> {
                onBackRequest(it)
            }
        }
    }


    Scaffold(
        topBar = {
            state.asset?.let {
                AssetPreviewTopBar(it.name) {
                    eventHandler.tapped(AssetPreviewContract.Event.BackPressed)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(color = PurpleGrey80)
        ) {
            when {
                state.isLoading -> Loader()
                state.isError -> Toast.makeText(context, messageError, Toast.LENGTH_SHORT).show()
                else -> state.asset?.let { AssetDetails(state.asset) }
            }
        }
    }
}


@Composable
fun AssetDetails(
    asset: Asset
) {

    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp16)),
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.dp24),
                end = dimensionResource(id = R.dimen.dp24),
                top = dimensionResource(id = R.dimen.dp24),
                bottom = dimensionResource(id = R.dimen.dp24)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(dimensionResource(id = R.dimen.dp16))
                .fillMaxSize()
        ) {
            InfoLine(key = "Price", value = Util.moneyFormatter(asset.priceUsd.toDouble()))
            InfoLine(
                key = "Change (24hr)",
                value = Util.changePercentageFormatter(asset.changePercent24Hr.toBigDecimal())
            )
            Spacer(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.dp16)))
            HorizontalDivider(color = Color.Blue, thickness = 1.dp)
            Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp16)))
            InfoLine(key = "Market Cap", value = Util.moneyFormatter(asset.priceUsd.toDouble()))
            InfoLine(key = "Volume (24hr)", value = Util.moneyFormatter(asset.priceUsd.toDouble()))
            InfoLine(key = "Supply", value = Util.moneyFormatter(asset.priceUsd.toDouble()).substring(1))
        }
    }
}


@Composable
fun InfoLine(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = dimensionResource(id = R.dimen.dp4),
                top = dimensionResource(id = R.dimen.dp4)

            )
    ) {
        Text(
            text = key,
            fontSize = 16.sp,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.weight(1f))

        if(key == "Change (24hr)"){
            Text(
                text = value,
                fontSize = 16.sp,
                color = if (value.startsWith("-")) ParadisePink else LimeGreen,
                fontWeight = FontWeight.Bold
            )
        }else{
            Text(
                text = value,
                fontSize = 16.sp,
                color = DarkBlue,
                fontWeight = FontWeight.Bold
            )
        }
    }
}