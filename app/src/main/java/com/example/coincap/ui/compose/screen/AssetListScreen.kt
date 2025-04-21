package com.example.coincap.ui.compose.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.coincap.vm.AssetListContract
import com.example.coincap.vm.AssetListViewModel

@Composable
fun AssetListScreen(
    eventHandler: EventHandler,
    onPreviewRequest: (AssetListContract.Effect.Preview) -> Unit,
    viewModel: AssetListViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val messageLoaded = stringResource(id = R.string.loaded)
    val messageError = stringResource(id = R.string.error)

    val state = viewModel.observe {
        when (it) {
            is AssetListContract.Effect.Loaded -> {
                Toast.makeText(context, messageLoaded, Toast.LENGTH_SHORT).show()
            }

            is AssetListContract.Effect.Preview -> {
                onPreviewRequest(it)
            }
        }
    }

    Scaffold(
        topBar = { AssetListTopBar() }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(color = PurpleGrey80)
        ) {
            when {
                state.isLoading -> Loader()
                state.isError -> Toast.makeText(context, messageError, Toast.LENGTH_SHORT).show()
                else -> AssetList(assets = state.assets) { asset ->
                    eventHandler.tapped(asset)
                }
            }
        }
    }
}


@Composable
fun AssetList(
    assets: List<Asset>,
    onItemClick: (Asset) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(assets) { index, asset ->
                if (index == 0) Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp8)))
                if (index == assets.size) Spacer(
                    modifier = Modifier.padding(
                        bottom = dimensionResource(
                            id = R.dimen.dp8
                        )
                    )
                )
                AssetListItem(asset = asset, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun AssetListItem(
    asset: Asset,
    onItemClick: (Asset) -> Unit
) {

    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp16)),
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.dp24),
                end = dimensionResource(id = R.dimen.dp24),
                top = dimensionResource(id = R.dimen.dp8),
                bottom = dimensionResource(id = R.dimen.dp8)
            )
            .fillMaxWidth()
            .clickable { onItemClick(asset) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(dimensionResource(id = R.dimen.dp16))
        ) {
            Image(
                painter = painterResource(id = getImageDrawable(asset.name.uppercase())),
                contentDescription = "Icon",
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.dp56))
                    .height(dimensionResource(id = R.dimen.dp56))
            )

            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.dp8))) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = asset.name,
                        fontSize = 20.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = Util.moneyFormatter(asset.priceUsd.toDouble()),
                        fontSize = 16.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp4)))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = asset.symbol,
                        fontSize = 16.sp,
                        color = DarkBlue
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = Util.changePercentageFormatter(asset.changePercent24Hr.toBigDecimal()),
                        fontSize = 16.sp,
                        color = if (asset.changePercent24Hr.startsWith("-")) ParadisePink else LimeGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


private fun getImageDrawable(name: String): Int {
    return when (name) {
        "BITCOIN" -> R.drawable.bitcoin
        "ETHEREUM" -> R.drawable.ethereum
        "XRP" -> R.drawable.xrp
        "TETHER" -> R.drawable.tether
        "BNB" -> R.drawable.bnb
        "CARDANO" -> R.drawable.cardano
        "AVALANCHE" -> R.drawable.avalanche
        "POLYGON" -> R.drawable.polygon
        else -> R.drawable.bitcoin
    }
}