package com.example.currencyconverter.currency.selection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.currencyconverter.util.Currency

class CurrencySelectionSpinnerAdapter(
    private val context: Context,
    private val currencyList: List<Currency>,
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = currencyList.size

    override fun getItem(position: Int): Any = currencyList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: CurrencyViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.currency_selection_item, parent, false)
            viewHolder = CurrencyViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as CurrencyViewHolder
        }

        val selectedCurrency = currencyList[position]

        viewHolder.countryFlag.setImageResource(
            getCountryFlagImage(selectedCurrency.countryCode)
        )

        viewHolder.countryName.text = context.getString(
            R.string.currency_selection_currency_country_name,
            selectedCurrency.countryName
        )
        viewHolder.currencySymbol.text = context.getString(
            R.string.currency_selection_currency_symbol,
            selectedCurrency.symbol
        )

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.currency_selection_dropdown_item, parent, false)
        val countryFlag = view.findViewById<ImageView>(R.id.currency_selection_dropdown_item_flag)
        val countryName = view.findViewById<TextView>(R.id.currency_selection_dropdown_item_country_name)
        val currencySymbol = view.findViewById<TextView>(R.id.currency_selection_dropdown_item_country_currency_symbol)

        val selectedCurrency = currencyList[position]

        countryFlag.setImageResource(
            getCountryFlagImage(selectedCurrency.countryCode)
        )

        countryName.text = context.getString(
            R.string.currency_selection_currency_country_name,
            selectedCurrency.countryName
        )
        currencySymbol.text = context.getString(
            R.string.currency_selection_currency_symbol,
            selectedCurrency.symbol
        )

        return view
    }

    class CurrencyViewHolder(itemView: View) {
        val countryFlag: ImageView
        val countryName: TextView
        val currencySymbol: TextView

        init {
            countryFlag = itemView.findViewById(R.id.currency_selection_item_flag) as ImageView
            countryName = itemView.findViewById(R.id.currency_selection_item_country_name) as TextView
            currencySymbol = itemView.findViewById(R.id.currency_selection_item_country_currency_symbol) as TextView
        }
    }

    private fun getCountryFlagImage(countryCode: String): Int =
        when (countryCode) {
            "AE" -> com.example.currencyconverter.brandkit.R.drawable.flag_ae
            "AU" -> com.example.currencyconverter.brandkit.R.drawable.flag_au
            "BR" -> com.example.currencyconverter.brandkit.R.drawable.flag_br
            "CA" -> com.example.currencyconverter.brandkit.R.drawable.flag_ca
            "CH" -> com.example.currencyconverter.brandkit.R.drawable.flag_ch
            "CN" -> com.example.currencyconverter.brandkit.R.drawable.flag_cn
            "EU" -> com.example.currencyconverter.brandkit.R.drawable.flag_eu
            "GB" -> com.example.currencyconverter.brandkit.R.drawable.flag_gb
            "HK" -> com.example.currencyconverter.brandkit.R.drawable.flag_hk
            "IN" -> com.example.currencyconverter.brandkit.R.drawable.flag_in
            "JP" -> com.example.currencyconverter.brandkit.R.drawable.flag_jp
            "MX" -> com.example.currencyconverter.brandkit.R.drawable.flag_mx
            "NO" -> com.example.currencyconverter.brandkit.R.drawable.flag_no
            "NZ" -> com.example.currencyconverter.brandkit.R.drawable.flag_nz
            "RU" -> com.example.currencyconverter.brandkit.R.drawable.flag_ru
            "SE" -> com.example.currencyconverter.brandkit.R.drawable.flag_se
            "SG" -> com.example.currencyconverter.brandkit.R.drawable.flag_sg
            "US" -> com.example.currencyconverter.brandkit.R.drawable.flag_us
            else -> 0
        }
}