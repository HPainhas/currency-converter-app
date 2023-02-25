package com.example.currencyconverter.currency.selection

import android.content.Context
import android.util.TypedValue
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
    private val currencyList: List<Currency>,
    private var countryNameTextColor: Int,
    private var currencySymbolTextColor: Int,
    private var countryNameTextSize: Float,
    private var currencySymbolTextSize: Float,
    private var isDropdownIconVisible: Boolean
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.currency_selection_spinner_item, parent, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val selectedCurrency = getItem(position)
        val countryFlag = viewHolder.countryFlag
        val countryName = viewHolder.countryName
        val currencySymbol = viewHolder.currencySymbol
        val dropdownIcon = viewHolder.dropdownIcon

        countryName.text = context.getString(
            R.string.currency_selection_currency_country_name,
            selectedCurrency.countryName
        )
        countryName.setTextColor(countryNameTextColor)
        countryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, countryNameTextSize)

        currencySymbol.text = context.getString(
            R.string.currency_selection_currency_symbol,
            selectedCurrency.symbol
        )
        currencySymbol.setTextColor(currencySymbolTextColor)
        currencySymbol.setTextSize(TypedValue.COMPLEX_UNIT_SP, currencySymbolTextSize)

        countryFlag.setImageResource(
            Util.getCountryFlagImage(selectedCurrency.symbol)
        )

        dropdownIcon.visibility = if (isDropdownIconVisible) View.VISIBLE else View.GONE

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.currency_selection_spinner_dropdown_item, parent, false)
        val countryFlag = view.findViewById<ImageView>(R.id.currency_selection_spinner_dropdown_item_flag)
        val countryName = view.findViewById<TextView>(R.id.currency_selection_spinner_dropdown_item_country_name)
        val currencySymbol = view.findViewById<TextView>(R.id.currency_selection_spinner_dropdown_item_country_currency_symbol)

        val selectedCurrency = currencyList[position]

        countryFlag.setImageResource(
            Util.getCountryFlagImage(selectedCurrency.symbol)
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

    override fun getCount(): Int = currencyList.size

    override fun getItem(position: Int): Currency = currencyList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    fun getCountryNameTextColor(): Int {
        return countryNameTextColor
    }

    fun setCountryNameTextColor(color: Int) {
        this.countryNameTextColor = color
        notifyDataSetChanged()
    }

    fun getCurrencySymbolTextColor(): Int {
        return currencySymbolTextColor
    }

    fun setCurrencySymbolTextColor(color: Int) {
        this.currencySymbolTextColor = color
        notifyDataSetChanged()
    }

    fun getCountryNameTextSize(): Float {
        return countryNameTextSize
    }

    fun setCountryNameTextSize(size: Float) {
        this.countryNameTextSize = size
        notifyDataSetChanged()
    }

    fun getCurrencySymbolTextSize(): Float {
        return currencySymbolTextSize
    }

    fun setCurrencySymbolTextSize(size: Float) {
        this.currencySymbolTextSize = size
        notifyDataSetChanged()
    }

    fun getIsDropdownIconVisible(): Boolean {
        return isDropdownIconVisible
    }

    fun setIsDropdownIconVisible(visible: Boolean) {
        this.isDropdownIconVisible = visible
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) {
        val countryFlag: ImageView
        val countryName: TextView
        val currencySymbol: TextView
        val dropdownIcon: ImageView

        init {
            this.countryFlag = itemView.findViewById(R.id.currency_selection_spinner_item_flag) as ImageView
            this.countryName = itemView.findViewById(R.id.currency_selection_spinner_item_country_name) as TextView
            this.currencySymbol = itemView.findViewById(R.id.currency_selection_spinner_item_country_currency_symbol) as TextView
            this.dropdownIcon = itemView.findViewById(R.id.currency_selection_spinner_item_dropdown_icon) as ImageView
        }
    }
}