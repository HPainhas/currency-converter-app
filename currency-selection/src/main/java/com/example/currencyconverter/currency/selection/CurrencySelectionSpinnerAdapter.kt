package com.example.currencyconverter.currency.selection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.currencyconverter.util.Util

class CurrencySelectionSpinnerAdapter(
    private val context: Context,
    private val amount: String,
    private var currencyList: List<CurrencyItemViewModel>,
    private var shouldShowExchangeRate: Boolean,
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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

        Util.loadImage(
            viewHolder.flag,
            selectedCurrency.flag_url,
            Util.getProgressDrawable(viewHolder.flag.context)
        )

        viewHolder.symbol.text = selectedCurrency.symbol
        viewHolder.amount.text = context.getString(R.string.currency_selection_item_amount, amount)
        viewHolder.rate.visibility = if (shouldShowExchangeRate) View.VISIBLE else View.GONE

        if (viewHolder.rate.isVisible) {
            viewHolder.rate.text = getFormattedExchangeRate(selectedCurrency)
        }

        return view
    }

    private fun getFormattedExchangeRate(currencyItemViewModel: CurrencyItemViewModel): String {
        return context.getString(
            R.string.currency_selection_item_exchange_rate_conversion,
            currencyItemViewModel.symbol,
            currencyItemViewModel.rate.toString(),
            currencyItemViewModel.symbol,
        )
    }

    class CurrencyViewHolder(itemView: View) {
        val flag: ImageView
        val symbol: TextView
        val amount: TextView
        val rate: TextView

        init {
            flag = itemView.findViewById(R.id.currency_selection_item_flag) as ImageView
            symbol = itemView.findViewById(R.id.currency_selection_item_symbol) as TextView
            amount = itemView.findViewById(R.id.currency_selection_item_amount) as TextView
            rate = itemView.findViewById(R.id.currency_selection_item_exchange_rate) as TextView
        }
    }
}