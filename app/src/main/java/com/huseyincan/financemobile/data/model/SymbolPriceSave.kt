package com.huseyincan.financemobile.data.model

import java.io.Serializable

data class SymbolPriceSave(
    val symbol: String,
    val price: Double,
    val quantity : Int
)
