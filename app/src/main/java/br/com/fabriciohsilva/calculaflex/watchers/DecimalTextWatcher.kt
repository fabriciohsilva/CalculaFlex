package br.com.fabriciohsilva.calculaflex.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.DecimalFormat

class DecimalTextWatcher (editText: EditText, val totalDecimaLNumber: Int = 2): TextWatcher {

    private  val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)
    
    init {
        formatMoney(editTextWeakReference.get()!!.text)
    }//end init

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        formatMoney(editable)
    }//end override fun afterTextChanged

    private fun getTotalDecimalNumber(): String {
        val decimalNumber = StringBuilder()
        for (i in 1..totalDecimaLNumber) {
            decimalNumber.append("0")
        }//end for (i in 1..totalDecimaLNumber)

        return decimalNumber.toString()
    }//end private fun getTotalDecimalNumber()

    private fun formatMoney(editable: Editable) {
        val editText = editTextWeakReference.get() ?: return
        val cleanString = editable.toString().trim().replace(" ", "")
        editText.removeTextChangedListener(this)

        val number = Math.pow(10.toDouble(), totalDecimaLNumber.toDouble())

        val parsed = when (cleanString) {
            null -> BigDecimal(0)
            "" -> BigDecimal(0)
            "null" -> BigDecimal(0)
            else -> BigDecimal(cleanString.replace("\\D+".toRegex(), ""))
                .setScale(totalDecimaLNumber, BigDecimal.ROUND_FLOOR)
                .divide(BigDecimal(number.toInt()), BigDecimal.ROUND_FLOOR)
        }//end val parsed = when (cleanString)

        val dfnd = DecimalFormat("#,##0.${getTotalDecimalNumber()}")
        val formatted = dfnd.format(parsed)
        editText.setText(formatted.replace(',', '.'))
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)

    }//end private fun formatMoney

}//end class DecimalTextWatcher