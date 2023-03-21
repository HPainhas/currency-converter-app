package com.example.currencyconverter.currency.history.chart

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val markerViewLabel: TextView = findViewById(R.id.marker_view_label)

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        if (entry == null) {
            return
        }

        Log.d("TEST", "x = ${entry.x} and y = ${entry.y}")

        markerViewLabel.text = context.getString(
            R.string.currency_chart_marker_view_label,
            entry.y
        )
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xPos: Float, yPos: Float): MPPointF {
        // center the marker view above the selected point
        return MPPointF(-width / 2f, -height.toFloat())
    }
}