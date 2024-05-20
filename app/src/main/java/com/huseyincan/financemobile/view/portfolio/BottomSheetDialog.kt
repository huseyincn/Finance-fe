package com.huseyincan.financemobile.view.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.model.SymbolPrice
import com.huseyincan.financemobile.data.model.SymbolPriceSave

class BottomSheetDialog : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomadddialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = view.findViewById(R.id.savetoport)
        val item = arguments?.getSerializable("item") as? SymbolPrice
        val price: TextInputEditText = view.findViewById(R.id.priceofbuy)
        if (item != null) {
            price.setText(item.price)
        }
        val quant: TextInputEditText = view.findViewById(R.id.quantityofbuy)
        quant.setText("1")
        button.setOnClickListener {
            if (item != null) {
                val data = SymbolPriceSave(
                    item.symbol,
                    price.text.toString().toDouble(),
                    quant.text.toString().toInt()
                )
                DataPass.addItem(data)
            }
            dismiss()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}