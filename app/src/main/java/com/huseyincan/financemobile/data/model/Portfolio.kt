package com.huseyincan.financemobile.data.model

import java.io.Serializable

data class Portfolio(
    val elements: List<SymbolPriceSave>,
    val userName: String,
    val revenue: Double
) : Serializable
