package com.example.currencyconverter.currency.selection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.currencyconverter.util.Currency
import com.example.currencyconverter.util.Util

class CurrencySelectionSpinnerAdapter(
    private val context: Context,
    private var currencyList: List<Currency>,
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

        selectedCurrency.imageName.let {
            Util.loadImage(
                viewHolder.flag,
                "$FLAGPEDIA_BASE_URL/$it",
                Util.getProgressDrawable(viewHolder.flag.context)
            )
        }

        viewHolder.symbol.text = context.getString(
            R.string.currency_selection_currency_name_symbol,
            selectedCurrency.countryName,
            selectedCurrency.symbol
        )

        // TODO - hide the dropdown icon on the spinner popup items

        return view
    }

    class CurrencyViewHolder(itemView: View) {
        val flag: ImageView
        val symbol: TextView

        init {
            flag = itemView.findViewById(R.id.currency_selection_item_flag) as ImageView
            symbol = itemView.findViewById(R.id.currency_selection_item_symbol) as TextView
        }
    }

    companion object {
        private const val FLAGPEDIA_BASE_URL = "https://flagpedia.net/data/flags/normal"
    }
}