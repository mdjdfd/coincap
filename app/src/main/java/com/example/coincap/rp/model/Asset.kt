package com.example.coincap.rp.model

import com.google.gson.annotations.SerializedName

data class Asset(
    @field:SerializedName("symbol") val symbol: String,
    @field:SerializedName("volumeUsd24Hr") val volumeUsd24Hr: String,
    @field:SerializedName("marketCapUsd") val marketCapUsd: String,
    @field:SerializedName("priceUsd") val priceUsd: String,
    @field:SerializedName("vwap24Hr") val vwap24Hr: String,
    @field:SerializedName("changePercent24Hr") val changePercent24Hr: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("explorer") val explorer: String,
    @field:SerializedName("rank") val rank: String,
    @field:SerializedName("id") val id: String,
    @field:SerializedName("maxSupply") val maxSupply: String,
    @field:SerializedName("supply") val supply: String
)


fun buildAssetPreview() = Asset(
    id = "bitcoin",
    rank = "1",
    symbol = "BTC",
    name = "XRP",
    supply = "19852700.0000000000000000",
    maxSupply = "21000000.0000000000000000",
    marketCapUsd = "1689829509087.0331362341914300",
    volumeUsd24Hr = "7933480998.7357296036530150",
    priceUsd = "85118.3722660914201209",
    changePercent24Hr = "1.3673213340053536",
    vwap24Hr = "84542.0995965869373580",
    explorer = "https://blockchain.info/"
)
