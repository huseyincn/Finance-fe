package com.huseyincan.financemobile.data.model

import java.io.Serializable

data class SymbolPrice(
    val symbol: String,
    val price: String,
    var oldPrice: Double = 0.0
) : Serializable
