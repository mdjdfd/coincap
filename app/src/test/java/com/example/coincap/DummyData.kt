package com.example.coincap

import com.example.coincap.rp.model.Asset

// Dummy data
fun getListOfAssets(): List<Asset>{
    return listOf(
        Asset(
            id = "bitcoin",
            rank = "1",
            symbol = "BTC",
            name = "Bitcoin",
            supply = "19853009.0000000000000000",
            maxSupply = "21000000.0000000000000000",
            marketCapUsd = "1678247502274.6639031463882585",
            volumeUsd24Hr = "6645181286.1273023935772949",
            priceUsd = "84533.6594706960493065",
            changePercent24Hr = "0.0399646289850810",
            vwap24Hr = "84790.2702539961004527",
            explorer = "https://blockchain.info/"
        ),
        Asset(
            id = "xrp",
            rank = "4",
            symbol = "XRP",
            name = "XRP",
            supply = "58338141684.0000000000000000",
            maxSupply = "100000000000.0000000000000000",
            marketCapUsd = "121716243019.7751221527257456",
            volumeUsd24Hr = "1119411947.4092914123715632",
            priceUsd = "2.0863921871059084",
            changePercent24Hr = "0.1012193326124151",
            vwap24Hr = "2.0907370213380788",
            explorer = "https://xrpcharts.ripple.com/#/graph/"
        ),
        Asset(
            id = "binance-coin",
            rank = "5",
            symbol = "BNB",
            name = "BNB",
            supply = "144006830.0000000000000000",
            maxSupply = "144006830.0000000000000000",
            marketCapUsd = "85285242305.4870083288146440",
            volumeUsd24Hr = "283321811.7635096542322435",
            priceUsd = "592.2305372980365468",
            changePercent24Hr = "1.9759841387199638",
            vwap24Hr = "585.6596439470656249",
            explorer = "https://etherscan.io/token/0xB8c77482e45F1F44dE1745F52C74426C631bDD52"
        )
    )
}