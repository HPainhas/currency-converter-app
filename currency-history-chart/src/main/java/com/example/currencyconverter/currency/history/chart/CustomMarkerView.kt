package com.example.currencyconverter.currency.history.chart

import android.content.Context
import android.widget.TextView
import com.example.currencyconverter.util.Util
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.util.SortedMap

class CustomMarkerView(
    context: Context,
    layoutResource: Int,
    private val rateByDateMap: SortedMap<Int, Pair<String, Float>>
) : MarkerView(context, layoutResource) {

    private val markerViewDate: TextView = findViewById(R.id.marker_view_date)
    private val markerViewRate: TextView = findViewById(R.id.marker_view_rate)

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        entry?.let {
            val index = entry.x.toInt()
            val date = rateByDateMap.values
                .elementAt(index)
                .first

            val formattedDate = Util.getFormattedDate("dd MMM yyyy", date)

            markerViewRate.text = context.getString(
                R.string.currency_chart_marker_view_rate,
                entry.y
            )

            markerViewDate.text = context.getString(
                R.string.currency_chart_marker_view_date,
                formattedDate
            )
        }

        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xPos: Float, yPos: Float): MPPointF {
        // Center the marker view above the selected point
        // return MPPointF(-width / 2f, -height.toFloat())

        // Shift the position of the MarkerView to the top of the chart
        return MPPointF(-(width / 2f), -yPos)
    }
}