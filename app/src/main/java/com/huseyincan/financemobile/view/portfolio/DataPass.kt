package com.huseyincan.financemobile.view.portfolio

import com.huseyincan.financemobile.data.model.SymbolPriceSave

object DataPass {
    public var data: ArrayList<SymbolPriceSave> = arrayListOf()

    fun addItem(item: SymbolPriceSave) {
        data.add(item)
    }

    fun clear() {
        data.clear()
    }
}